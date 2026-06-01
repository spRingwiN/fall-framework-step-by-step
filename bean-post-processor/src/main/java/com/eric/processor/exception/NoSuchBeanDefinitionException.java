package com.eric.processor.exception;

public class NoSuchBeanDefinitionException extends BeanDefinitionException {

    public NoSuchBeanDefinitionException() {

    }

    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }

}
