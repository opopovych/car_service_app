package mate.academy.carservice.exception;

public class TelegramNotificationException extends RuntimeException {
    public TelegramNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
