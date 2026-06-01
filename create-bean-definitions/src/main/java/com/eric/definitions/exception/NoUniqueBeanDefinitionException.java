package com.eric.definitions.exception;

public class NoUniqueBeanDefinitionException extends BeanDefinitionException {

    public NoUniqueBeanDefinitionException() {

    }

    public NoUniqueBeanDefinitionException(String message) {
        super(message);
    }

}
