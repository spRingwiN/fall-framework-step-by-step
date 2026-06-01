package com.eric.scan.destroy;

import com.eric.definitions.annotation.Component;
import com.eric.definitions.annotation.Value;
import jakarta.annotation.PreDestroy;

@Component
public class AnnotationDestroyBean {

    @Value("${app.title}")
    public String appTitle;

    @PreDestroy
    void destroy() {
        this.appTitle = null;
    }

}
