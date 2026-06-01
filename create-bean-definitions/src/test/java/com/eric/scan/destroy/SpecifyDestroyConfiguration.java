package com.eric.scan.destroy;

import com.eric.definitions.annotation.Configuration;
import com.eric.definitions.annotation.Value;

@Configuration
public class SpecifyDestroyConfiguration {

    SpecifyDestroyBean createSpecifyDestroyBean(@Value("${app.title}") String appTitle) {
        return new SpecifyDestroyBean(appTitle);
    }

}
