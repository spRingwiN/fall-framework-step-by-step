package com.eric.scan.proxy;

import com.eric.processor.annotation.Autowired;
import com.eric.processor.annotation.Component;

@Component
public class InjectProxyOnPropertyBean {

    @Autowired
    public OriginBean injected;

}
