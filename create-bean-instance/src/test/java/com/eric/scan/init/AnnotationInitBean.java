package com.eric.scan.init;

import com.eric.instance.annotation.Configuration;
import com.eric.instance.annotation.Value;
import jakarta.annotation.PostConstruct;

@Configuration
public class AnnotationInitBean {

    @Value("${app.title}")
    String appTitle;

    @Value("${app.version}")
    String appVersion;

    public String appName;

    @PostConstruct
    void init() {
        this.appName = this.appTitle + " / " + this.appVersion;
    }

}
