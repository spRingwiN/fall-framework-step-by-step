package com.eric.scan.nested;

import com.eric.instance.annotation.Component;

@Component
public class OuterBean {

    @Component
    public static class NestedBean {

    }

}
