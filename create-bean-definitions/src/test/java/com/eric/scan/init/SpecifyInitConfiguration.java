package com.eric.scan.init;

import com.eric.definitions.annotation.Bean;
import com.eric.definitions.annotation.Configuration;
import com.eric.definitions.annotation.Value;

@Configuration
public class SpecifyInitConfiguration {

    @Bean(initMethod = "init")
    SpecifyInitBean createSpecifyInitBean(@Value("${app.title}") String appTitle, @Value("${app.version}") String appVersion) {
        return new SpecifyInitBean(appTitle, appVersion);
    }

}
