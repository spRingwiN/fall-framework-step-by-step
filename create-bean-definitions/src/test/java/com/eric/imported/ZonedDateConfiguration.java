package com.eric.imported;

import com.eric.definitions.annotation.Bean;
import com.eric.definitions.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
public class ZonedDateConfiguration {

    @Bean
    ZonedDateTime startZoneDateTime() {
        return ZonedDateTime.now();
    }

}
