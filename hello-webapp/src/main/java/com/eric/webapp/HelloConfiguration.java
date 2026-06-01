package com.eric.webapp;

import com.eric.fall.annotation.ComponentScan;
import com.eric.fall.annotation.Configuration;
import com.eric.fall.annotation.Import;
import com.eric.fall.jdbc.JdbcConfiguration;
import com.eric.fall.web.WebMvcConfiguration;

@ComponentScan
@Configuration
@Import({JdbcConfiguration.class, WebMvcConfiguration.class})
public class HelloConfiguration {
}
