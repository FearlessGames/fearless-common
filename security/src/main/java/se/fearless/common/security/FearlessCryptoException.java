package se.fearless.common.security;


public class FearlessCryptoException extends RuntimeException {
	public FearlessCryptoException() {
    }

    public FearlessCryptoException(String message) {
        super(message);
    }

    public FearlessCryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    public FearlessCryptoException(Throwable cause) {
        super(cause);
    }

}
