package icu.fordring.ref;

/**
 * @author fordring
 * @since 2022/8/10
 */
public class SkippedUpdateException extends RuntimeException{
    public SkippedUpdateException() {
    }

    public SkippedUpdateException(String message) {
        super(message);
    }

    public SkippedUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public SkippedUpdateException(Throwable cause) {
        super(cause);
    }

    public SkippedUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
