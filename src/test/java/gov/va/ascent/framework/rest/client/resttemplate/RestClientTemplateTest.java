package gov.va.ascent.framework.rest.client.resttemplate;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import gov.va.ascent.framework.service.ServiceResponse;

public class RestClientTemplateTest {
	
	private static final String DUMMY_ENDPOINT = "http://dummy.service/dummy/endpoint";
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
			responseEntity = restClientTemplate.executeURL(DUMMY_ENDPOINT, responseType);
		}catch(Exception e) {
			System.out.println("REST call is expected to fail");			
			assertTrue(e instanceof RestClientException);
		}
	}
}

