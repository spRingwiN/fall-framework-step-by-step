package com.eric.scan.destroy;

import com.eric.instance.annotation.Configuration;
import com.eric.instance.annotation.Value;

@Configuration
public class SpecifyDestroyConfiguration {

    SpecifyDestroyBean createSpecifyDestroyBean(@Value("${app.title}") String appTitle) {
        return new SpecifyDestroyBean(appTitle);
    }

}
