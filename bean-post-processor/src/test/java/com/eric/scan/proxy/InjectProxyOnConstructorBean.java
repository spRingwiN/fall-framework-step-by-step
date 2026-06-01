package com.eric.scan.proxy;


import com.eric.processor.annotation.Autowired;
import com.eric.processor.annotation.Component;

@Component
public class InjectProxyOnConstructorBean {

    public final OriginBean injected;

    public InjectProxyOnConstructorBean(@Autowired OriginBean injected) {
        this.injected = injected;
    }

}
