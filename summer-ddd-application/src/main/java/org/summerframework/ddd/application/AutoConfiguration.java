package org.summerframework.ddd.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { AutoConfiguration.class })
public class AutoConfiguration {

}
