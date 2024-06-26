package mate.academy.carservice.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import mate.academy.carservice.dto.car.CarDtoRequest;
import mate.academy.carservice.dto.car.CarDtoResponse;
import mate.academy.carservice.dto.car.UpdateCarInfoRequestDto;
import mate.academy.carservice.model.Car;
import mate.academy.carservice.model.CarType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

    }

    @WithMockUser(roles = "MANAGER")
    @Test
    @DisplayName("Create car and save to DB")
    @Sql(scripts = "classpath:database/cars/add-cars-to-the-cars-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cars/delete-cars-from-the-cars-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCar_validRequest_success() throws Exception {
        CarDtoRequest carDtoRequest = createTestCarDtoRequest();

        String jsonContent = objectMapper.writeValueAsString(carDtoRequest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/cars")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CarDtoResponse actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CarDtoResponse.class);
        Assertions.assertAll("actual",
                () -> assertNotNull(actual.getBrand()),
                () -> assertEquals(actual.getModel(), carDtoRequest.getModel()));
    }

    @Test
    @DisplayName("Get all cars")
    @Sql(scripts = "classpath:database/cars/add-cars-to-the-cars-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cars/delete-cars-from-the-cars-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllCars_validRequest_success() throws Exception {
        CarDtoRequest carDtoRequest = createTestCarDtoRequest();
        CarDtoRequest carDtoRequest1 = createSecondTestCarDtoRequest();
        List<CarDtoRequest> cars = List.of(carDtoRequest, carDtoRequest1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cars")
                )
                .andExpect(status().isOk())
                .andReturn();

        List<CarDtoResponse> actualCars = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<CarDtoResponse>>() {
                }
        );
        Assertions.assertEquals(cars.size(), actualCars.size());
    }

    @Test
    @DisplayName("Get car by id")
    @Sql(scripts = "classpath:database/cars/add-cars-to-the-cars-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cars/delete-cars-from-the-cars-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCarById_validRequest_success() throws Exception {
        long id = 1L;
        CarDtoRequest carDtoRequest = createTestCarDtoRequest();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cars/1")
                )
                .andExpect(status().isOk())
                .andReturn();
        CarDtoRequest actualCar = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CarDtoRequest.class);
        EqualsBuilder.reflectionEquals(carDtoRequest, actualCar);
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    @DisplayName("Update car info by id")
    @Sql(scripts = "classpath:database/cars/add-cars-to-the-cars-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cars/delete-cars-from-the-cars-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCarInfo_ValidRequest_Success() throws Exception {
        //given
        long carId = 1L;
        UpdateCarInfoRequestDto updateCarInfoRequestDto = new UpdateCarInfoRequestDto()
                .setModel("NewModel")
                .setBrand("NewBrand")
                .setType(CarType.SEDAN)
                .setInventory(10)
                .setDailyFee(new BigDecimal("10.00"));

        Car expectedCar = new Car()
                .setModel(updateCarInfoRequestDto.getModel())
                .setBrand(updateCarInfoRequestDto.getBrand())
                .setType(updateCarInfoRequestDto.getType())
                .setInventory(updateCarInfoRequestDto.getInventory())
                .setDailyFee(updateCarInfoRequestDto.getDailyFee());
        CarDtoResponse expectedCarDto = createTestCarDtoResponse(expectedCar);

        String jsonContent = objectMapper.writeValueAsString(updateCarInfoRequestDto);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/cars/{carId}", carId)
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        //then
        CarDtoResponse actualCarDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CarDtoResponse.class);

        EqualsBuilder.reflectionEquals(expectedCarDto, actualCarDto,
                "id");
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    @DisplayName("Delete car")
    @Sql(scripts = "classpath:database/cars/add-cars-to-the-cars-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cars/delete-cars-from-the-cars-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCar_validRequest_success() throws Exception {
        long carId = 1L;

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/cars/{carId}", carId))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private CarDtoRequest createTestCarDtoRequest() {
        CarDtoRequest carDtoRequest = new CarDtoRequest()
                .setModel("Toyota")
                .setBrand("Camry")
                .setType(CarType.SUV)
                .setInventory(1)
                .setDailyFee(BigDecimal.valueOf(50));
        return carDtoRequest;
    }

    private CarDtoRequest createSecondTestCarDtoRequest() {
        CarDtoRequest carDtoRequest = new CarDtoRequest()
                .setModel("Audi")
                .setBrand("A6")
                .setType(CarType.SUV)
                .setInventory(3)
                .setDailyFee(BigDecimal.valueOf(150));
        return carDtoRequest;
    }

    private CarDtoResponse createTestCarDtoResponse(Car car) {
        CarDtoResponse carDtoResponse = new CarDtoResponse()
                .setModel(car.getModel())
                .setBrand(car.getBrand())
                .setType(car.getType());
        return carDtoResponse;
    }

    private Car createTestCar(CarDtoRequest carDtoRequest) {
        Car car = new Car()
                .setModel(carDtoRequest.getModel())
                .setBrand(carDtoRequest.getBrand())
                .setType(carDtoRequest.getType())
                .setInventory(carDtoRequest.getInventory())
                .setDailyFee(carDtoRequest.getDailyFee());
        return car;
    }

    private UpdateCarInfoRequestDto createTestUpdateCar() {
        UpdateCarInfoRequestDto carDtoRequestUpdate = new UpdateCarInfoRequestDto()
                .setModel("Ducato")
                .setBrand("Fiat")
                .setType(CarType.SUV)
                .setInventory(1)
                .setDailyFee(BigDecimal.valueOf(100));
        return carDtoRequestUpdate;
    }

}
