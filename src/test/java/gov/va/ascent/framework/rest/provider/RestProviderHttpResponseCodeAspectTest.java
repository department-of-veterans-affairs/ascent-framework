package gov.va.ascent.framework.rest.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import gov.va.ascent.framework.aspect.PerformanceLoggingAspect;
import gov.va.ascent.framework.exception.AscentRuntimeException;
import gov.va.ascent.framework.transfer.jaxb.adapters.DateAdapterLoggingTestAppender;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import gov.va.ascent.framework.messages.Message;
import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.service.ServiceResponse;
@RunWith(MockitoJUnitRunner.class)
public class RestProviderHttpResponseCodeAspectTest {

	private Logger restProviderLog = (Logger) org.slf4j.LoggerFactory.getLogger(RestProviderHttpResponseCodeAspect.class);
	
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
		RestProviderHttpResponseCodeAspectLogAppender.events.clear();
		restProviderLog.setLevel(Level.DEBUG);
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
		RestProviderHttpResponseCodeAspectLogAppender.events.clear();
		restProviderLog.setLevel(Level.DEBUG);
	}

	@Test
	public void testConstructorWithParam() {
		MessagesToHttpStatusRulesEngine ruleEngine = new MessagesToHttpStatusRulesEngine();
		ruleEngine.addRule(
				new MessageSeverityMatchRule(MessageSeverity.FATAL, HttpStatus.INTERNAL_SERVER_ERROR)); 
		ruleEngine.addRule(
				new MessageSeverityMatchRule(MessageSeverity.ERROR, 
						HttpStatus.BAD_REQUEST));
		restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect(ruleEngine);
		assertNotNull(restProviderHttpResponseCodeAspect);
		
	}
	
	@Test
	public void testAroundAdvice() {
		try {
			restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
			assertNull(restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint));
		}catch(Throwable e) {
			
		}

	}

	@Test
	public void testAroundAdviceCatchAscentExceptionLogging()  {
		restProviderLog.setLevel(Level.ERROR);
		restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
		ResponseEntity<ServiceResponse> returnObject = null;
		try {
			when(proceedingJoinPoint.proceed()).thenThrow(new AscentRuntimeException());
			returnObject = restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint);
		} catch (Throwable throwable) {

		}
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, returnObject.getStatusCode());
		assertEquals("RestHttpResponseCodeAspect encountered uncaught exception in REST endpoint.",
				RestProviderHttpResponseCodeAspectLogAppender.events.get(0).getMessage());
	}

	@Test
	public void testAroundAdviceCatchExceptionLogging()  {
		restProviderLog.setLevel(Level.ERROR);
		restProviderHttpResponseCodeAspect = new RestProviderHttpResponseCodeAspect();
		ResponseEntity<ServiceResponse> returnObject = null;
		try {
			when(proceedingJoinPoint.proceed()).thenThrow(new Throwable("Unit Test Throwable converted to AscentRuntimException"));
			returnObject = restProviderHttpResponseCodeAspect.aroundAdvice(proceedingJoinPoint);
		} catch (Throwable throwable) {

		}
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, returnObject.getStatusCode());
		assertEquals("RestHttpResponseCodeAspect encountered uncaught exception in REST endpoint.",
				RestProviderHttpResponseCodeAspectLogAppender.events.get(0).getMessage());
		assertEquals("gov.va.ascent.framework.exception.AscentRuntimeException", RestProviderHttpResponseCodeAspectLogAppender.events.get(0).getThrowableProxy().getClassName());
	}

}
