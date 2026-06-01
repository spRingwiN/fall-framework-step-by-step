package com.eric.scan.primary;

import com.eric.instance.annotation.Bean;
import com.eric.instance.annotation.Configuration;
import com.eric.instance.annotation.Primary;

@Configuration
public class PrimaryConfiguration {

    @Bean
    @Primary
    DogBean husky() {
        return new DogBean("Husky");
    }

    @Bean
    DogBean teddy() {
        return new DogBean("Teddy");
    }

}
