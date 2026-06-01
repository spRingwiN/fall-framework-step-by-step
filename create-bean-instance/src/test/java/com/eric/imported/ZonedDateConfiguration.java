package com.eric.imported;

import com.eric.instance.annotation.Bean;
import com.eric.instance.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
public class ZonedDateConfiguration {

    @Bean
    ZonedDateTime startZoneDateTime() {
        return ZonedDateTime.now();
    }

}
