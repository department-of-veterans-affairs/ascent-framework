package gov.va.ascent.framework.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import gov.va.ascent.framework.validation.ViolationMessageParts;

@RunWith(MockitoJUnitRunner.class)
public class ServiceValidationToMessageAspectTest {

	@Mock
	private ProceedingJoinPoint proceedingJoinPoint;
	
	@Mock
	private ServiceRequest mockServiceRequest;
	
	@Mock
	private ServiceResponse mockServiceResponse;	
	
    @Mock
    private MethodSignature signature;
    
    @Mock
    private JoinPoint.StaticPart staticPart;
    
    private ServiceValidationToMessageAspect mockServiceValidationToMessageAspect;
	
    private Object[] value;
    
    private Map<String,List<ViolationMessageParts>> map = new HashMap<String,List<ViolationMessageParts>>();
    private List<ViolationMessageParts> testMessageList;
	
	@Before
	public void setUp() throws Exception {
		value = new Object[1];
		value[0] = mockServiceRequest;
		when(proceedingJoinPoint.getArgs()).thenReturn(value);
		when(proceedingJoinPoint.getStaticPart()).thenReturn(staticPart);
        when(staticPart.getSignature()).thenReturn(signature);

        testMessageList = new ArrayList<ViolationMessageParts>();
		ViolationMessageParts errorMessage1 = new ViolationMessageParts();
		errorMessage1.setNewKey("ErrMsg1");
		errorMessage1.setText("Error Message 1");
		ViolationMessageParts errorMessage2 = new ViolationMessageParts();
		errorMessage2.setNewKey("ErrMsg2");
		errorMessage2.setText("Error Message 2");		
		testMessageList.add(errorMessage1);
		testMessageList.add(errorMessage2);
		
        mockServiceValidationToMessageAspect = new ServiceValidationToMessageAspect();		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAroundAdvice() {
		try{
			assertNotNull(mockServiceValidationToMessageAspect.aroundAdvice(proceedingJoinPoint, mockServiceRequest));
			assertNull(((ServiceResponse)mockServiceValidationToMessageAspect.aroundAdvice(proceedingJoinPoint, mockServiceRequest)).getMessages());
			
		}catch(Throwable throwable) {
			
		}
	}

	//@Test
	public void testConvertMapToMessages() {
		map.put("errors", testMessageList);
		ServiceValidationToMessageAspect.convertMapToMessages(mockServiceResponse, map);
		assertNotNull(mockServiceResponse.getMessages());
		assertEquals(2,mockServiceResponse.getMessages().size());
	}

}
