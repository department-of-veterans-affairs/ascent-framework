package gov.va.ascent.framework.rest.provider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import gov.va.ascent.framework.audit.RequestResponseLogSerializer;
import gov.va.ascent.framework.rest.client.resttemplate.RestClientTemplate;

@Configuration
@ComponentScan("gov.va.ascent.framework.audit")
@EnableAsync
public class RestProviderSpringConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public RequestResponseLogSerializer requestResponseAsyncLogging() {
        return new RequestResponseLogSerializer();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public RestClientTemplate restClientTemplate(){
        return new RestClientTemplate();
    }    
}
