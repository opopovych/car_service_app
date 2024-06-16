package mate.academy.carservice.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.carservice.auth.dto.UserRegisterRequestDto;
import mate.academy.carservice.auth.dto.UserRegisterResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:database/users/delete-users-from-the-users-table.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class AuthControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext webApplicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Register new user")
    @Sql(scripts = {"classpath:database/users/delete-users-from-the-users-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void registerNewUser_ValidRequest_Success() throws Exception {
        //given
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto()
                .setEmail("email@example.com")
                .setPassword("Password1234$")
                .setFirstName("FirstName")
                .setLastName("LastName");

        UserRegisterResponseDto expectedResponseDto = new UserRegisterResponseDto()
                .setId(1L)
                .setEmail("email@example.com");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //when
        MvcResult mvcResult = mockMvc.perform(
                post("/auth/registration")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        //then
        UserRegisterResponseDto actualResponseDto = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(),
                        UserRegisterResponseDto.class);

        Assertions.assertEquals(expectedResponseDto.getEmail(), actualResponseDto.getEmail());
    }
}
