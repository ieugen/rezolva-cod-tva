package ro.ieugen.tva.csv;

public class InvalidVatCodeFormat extends RuntimeException {

    public InvalidVatCodeFormat(String message) {
        super(message);
    }

    public InvalidVatCodeFormat(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidVatCodeFormat(Throwable cause) {
        super(cause);
    }

    public InvalidVatCodeFormat(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
