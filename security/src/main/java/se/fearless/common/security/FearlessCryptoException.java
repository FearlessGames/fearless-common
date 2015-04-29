package se.fearless.common.security;


import java.security.NoSuchAlgorithmException;

public class FearlessCryptoException extends Exception {
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
