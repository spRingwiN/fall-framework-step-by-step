package com.eric.scan.init;

import com.eric.definitions.annotation.Configuration;
import com.eric.definitions.annotation.Value;

@Configuration
public class AnnotationInitBean {

    @Value("${app.title}")
    String appTitle;

    @Value("${app.version}")
    String appVersion;

    public String appName;

    void init() {
        this.appName = this.appTitle + "/" + this.appVersion;
    }

}
