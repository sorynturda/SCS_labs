package cpu8086.exception;

public class CPUException extends Exception {
    public CPUException(String message) {
        super(message);
    }

    public CPUException(String message, Throwable cause) {
        super(message, cause);
    }
}
