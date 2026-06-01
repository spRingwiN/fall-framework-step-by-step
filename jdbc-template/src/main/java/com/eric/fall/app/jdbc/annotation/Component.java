package com.eric.fall.app.jdbc.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    /**
     * Bean name,
     * default to simple class name with first-letter-lowercase
     * @return
     */
    String value() default "";

}
