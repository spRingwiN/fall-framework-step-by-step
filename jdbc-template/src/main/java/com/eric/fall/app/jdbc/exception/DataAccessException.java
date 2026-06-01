package com.eric.fall.app.jdbc.exception;

public class DataAccessException extends NestedRuntimeException {

    public DataAccessException() {

    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }

}
