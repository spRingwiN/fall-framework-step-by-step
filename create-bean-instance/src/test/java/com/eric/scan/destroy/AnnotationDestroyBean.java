package com.eric.scan.destroy;

import com.eric.instance.annotation.Component;
import com.eric.instance.annotation.Value;
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
