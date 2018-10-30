package gov.va.ascent.framework.rest.client.resttemplate;

import gov.va.ascent.framework.service.ServiceResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import static org.junit.Assert.assertTrue;

public class RestClientTemplateTest {
	
	private static final String DUMMY_ENDPOINT = "https://jsonplaceholder.typicode.com/posts/1";
	private ParameterizedTypeReference<ServiceResponse> responseType = new ParameterizedTypeReference<ServiceResponse>() {};
	private ResponseEntity<ServiceResponse> responseEntity;
	private RestClientTemplate restClientTemplate;
	private AnnotationConfigWebApplicationContext context;
	

    
	@Before
	public void setUp() throws Exception {
        context = new AnnotationConfigWebApplicationContext();
        context.register(TestRestAutoConfiguration.class);
        context.refresh();
	}

	@After
	public void tearDown() throws Exception {
		context.close();
	}

	@Test
	public void testExecuteURL() {
		try {
			restClientTemplate = this.context.getBean(RestClientTemplate.class);
			setResponseEntity(restClientTemplate.executeURL(DUMMY_ENDPOINT, responseType));
		}catch(Exception e) {
			System.out.println("REST call is expected to fail");			
			assertTrue(e instanceof RestClientException);
		}
	}

	public ResponseEntity<ServiceResponse> getResponseEntity() {
		return responseEntity;
	}

	public void setResponseEntity(ResponseEntity<ServiceResponse> responseEntity) {
		this.responseEntity = responseEntity;
	}
}

