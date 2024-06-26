package mate.academy.carservice.service.notification;

import mate.academy.carservice.model.Rental;

public interface NotificationService {
    void sendNewRentalNotification(Rental rental);

    void sendOverdueRentalsNotification();

    void sendSuccessfulPaymentNotification(long rentalId);
}
