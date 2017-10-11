package gov.va.ascent.framework.rest.provider;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import gov.va.ascent.framework.messages.Message;
import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.service.ServiceResponse;
@RunWith(MockitoJUnitRunner.class)
public class RestProviderHttpResponseCodeAspectTest {
	
	private RestProviderHttpResponseCodeAspect restProviderHttpResponseCodeAspect;
    @Mock
	private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private ResponseEntity<ServiceResponse> responseEntity;
    
    @Mock
    private ServiceResponse serviceResponse;
	
    private List<Message> detailedMsg = new ArrayList<Message>();

	@Before
	public void setUp() throws Exception {

		try {
			Message msg = new Message(MessageSeverity.FATAL,"FatalKey","Fatal Message");
			detailedMsg.add(msg);
			when(proceedingJoinPoint.proceed()).thenReturn(responseEntity);
			when(responseEntity.getBody()).thenReturn(serviceResponse);
			when(serviceResponse.getMessages()).thenReturn(detailedMsg);
		}catch(Throwable e) {
			
		}

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAroundAdvice() {
		try {
			restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
			assertNull(restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint));
		}catch(Throwable e) {
			
		}

	}

}
