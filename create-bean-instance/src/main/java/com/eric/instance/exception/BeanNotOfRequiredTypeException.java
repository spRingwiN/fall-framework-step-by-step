package com.eric.instance.exception;

public class BeanNotOfRequiredTypeException extends BeansException {
    public BeanNotOfRequiredTypeException() {
    }
    public BeanNotOfRequiredTypeException(String message) {
        super(message);
    }
}
