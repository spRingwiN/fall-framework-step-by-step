package com.eric.scan.nested;

import com.eric.definitions.annotation.Component;

@Component
public class OuterBean {

    @Component
    public static class NestedBean {

    }

}
