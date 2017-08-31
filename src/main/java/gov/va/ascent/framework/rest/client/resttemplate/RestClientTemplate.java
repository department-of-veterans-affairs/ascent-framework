package gov.va.ascent.framework.rest.client.resttemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClientTemplate {
	/**
	 * REST client class that uses Spring RestTemplate to make service call
	 */
  
	@Autowired
    private RestTemplate restTemplate;	

	public <T> ResponseEntity<T> executeURL (String url, ParameterizedTypeReference<T> responseType) {
		ResponseEntity<T> exchange =  this.restTemplate.exchange(url, HttpMethod.GET, null, responseType);
		return exchange;
	}
}
