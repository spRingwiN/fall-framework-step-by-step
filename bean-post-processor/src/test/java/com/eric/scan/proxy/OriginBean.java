package com.eric.scan.proxy;

import com.eric.processor.annotation.Component;
import com.eric.processor.annotation.Value;

@Component
public class OriginBean {

    @Value("${app.title}")
    public String name;

    public String version;

    @Value("${app.version}")
    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }
    public String getVersion() {
        System.out.println("setVersion: " + this.version);
        System.out.println(this.getClass().getSimpleName());
        return version;
    }




}
