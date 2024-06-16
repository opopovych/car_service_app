package mate.academy.carservice.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import mate.academy.carservice.dto.payment.PaymentRequest;
import mate.academy.carservice.model.Rental;
import mate.academy.carservice.model.payment.Payment;
import mate.academy.carservice.model.payment.Status;
import mate.academy.carservice.model.payment.Type;
import mate.academy.carservice.repo.RentalRepository;
import mate.academy.carservice.security.AuthenticationService;
import mate.academy.carservice.service.PaymentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment management", description = "Endpoints for managing cars")
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentsController {
    private final PaymentService paymentService;
    private final RentalRepository rentalRepository;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Get payment", description = "Endpoint for getting payments")
    @GetMapping("/")
    public List<Payment> getPayments(@RequestParam("user_id") Long userId) {
        return paymentService.getPaymentsByUserId(userId);
    }

    @Operation(summary = "Create payment session", description = "Endpoint for creating "
            + "payment session")
    @PostMapping("/")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Transactional
    public Map<String, Object> createPaymentSession(@RequestBody PaymentRequest paymentRequest) {
        Payment payment = new Payment();
        Rental rental = rentalRepository.findById(paymentRequest.getRentalId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid rental ID"));
        payment.setRental(rental);
        payment.setSessionUrl(paymentRequest.getSessionUrl());
        payment.setAmountToPay(paymentRequest.getAmountToPay());
        payment.setStatus(Status.PENDING);
        payment.setType(Type.PAYMENT);

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(paymentRequest.getSessionUrl() + "/success")
                .setCancelUrl(paymentRequest.getSessionUrl() + "/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(paymentRequest
                                                        .getAmountToPay()
                                                        .movePointRight(2)
                                                        .longValue())
                                                .setProductData(
                                                        SessionCreateParams
                                                                .LineItem
                                                                .PriceData
                                                                .ProductData
                                                                .builder()
                                                                .setName("Car Rental Payment")
                                                                .build())
                                                .build())
                                .build())
                .build();

        try {
            Session session = Session.create(params);
            payment.setSessionId(session.getId());
            paymentService.createPaymentSession(payment);

            Map<String, Object> response = new HashMap<>();
            response.put("sessionId", session.getId());
            return response;
        } catch (StripeException e) {
            e.printStackTrace();
            // Handle exception appropriately
            return new HashMap<>();
        }
    }

    @Operation(summary = "Successful page for redirection",
            description = "Successful page for redirection")
    @GetMapping("/success/")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String handleSuccess(@RequestParam("session_id") String sessionId) {
        paymentService.updatePaymentStatus(sessionId, Status.PAID);
        return "Payment successful!";
    }

    @Operation(summary = "Cancel page for redirection",
            description = "Cancel page for redirection")
    @GetMapping("/cancel/")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String handleCancel() {
        return "Payment was canceled!";
    }
}
