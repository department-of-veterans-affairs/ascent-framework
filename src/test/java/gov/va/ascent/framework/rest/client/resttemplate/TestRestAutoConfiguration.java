package gov.va.ascent.framework.rest.client.resttemplate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestRestAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RestClientTemplate restClientTemplate(){
        return new RestClientTemplate();
    }    
        
	@Bean
    RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
	}
}