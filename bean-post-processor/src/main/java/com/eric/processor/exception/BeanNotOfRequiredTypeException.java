package com.eric.processor.exception;

public class BeanNotOfRequiredTypeException extends BeansException {
    public BeanNotOfRequiredTypeException() {
    }
    public BeanNotOfRequiredTypeException(String message) {
        super(message);
    }
}
