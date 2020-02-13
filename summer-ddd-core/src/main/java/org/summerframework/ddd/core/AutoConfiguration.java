package org.summerframework.ddd.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.summerframework.ddd.core.command.CommandBus;
import org.summerframework.ddd.core.command.CommandContext;
import org.summerframework.ddd.core.command.standard.CloudCommandBus;
import org.summerframework.ddd.core.command.standard.EmptyCommandContext;

@Configuration
@ComponentScan(basePackageClasses = { AutoConfiguration.class })
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate defaultRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3000);
        requestFactory.setReadTimeout(3000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(CommandContext.class)
    public CommandContext emptyCommandContext() {
        return new EmptyCommandContext();
    }

    @Bean
    @ConditionalOnMissingBean(CommandBus.class)
    public CommandBus cloudCommandBus(RestTemplate restTemplate, CommandContext commandContext,
            @Value("${summerframework.gatewayUrl}") String gatewayUrl) {
        return new CloudCommandBus(restTemplate, commandContext, gatewayUrl);
    }

}
