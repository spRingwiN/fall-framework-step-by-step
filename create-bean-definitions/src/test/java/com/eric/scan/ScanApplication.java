package com.eric.scan;

import com.eric.definitions.annotation.ComponentScan;
import com.eric.definitions.annotation.Import;
import com.eric.imported.LocalDateConfiguration;
import com.eric.imported.ZonedDateConfiguration;

@ComponentScan
@Import({ ZonedDateConfiguration.class, LocalDateConfiguration.class })
public class ScanApplication {
}
