package com.eric.scan.primary;

import com.eric.definitions.annotation.Bean;
import com.eric.definitions.annotation.Configuration;
import com.eric.definitions.annotation.Primary;

@Configuration
public class PrimaryConfiguration {

    @Bean
    @Primary
    DogBean husky() {
        return new DogBean("husky");
    }

    @Bean
    DogBean teddy() {
        return new DogBean("teddy");
    }

}
