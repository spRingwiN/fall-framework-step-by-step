package com.eric.fall.app.utils;

import com.eric.definitions.annotation.Component;

import java.lang.annotation.*;

@Component
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CustomComponent {

    String value() default "";

}
