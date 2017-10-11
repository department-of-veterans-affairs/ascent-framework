package gov.va.ascent.framework.rest.provider;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import gov.va.ascent.framework.messages.Message;
import gov.va.ascent.framework.messages.MessageSeverity;

public class MessagesToHttpStatusRulesEngineTest {

	MessagesToHttpStatusRulesEngine messagesToHttpStatusRulesEngine;
	List<Message> messagesInResponse = new ArrayList<Message>();
	
	@Before
	public void setUp() throws Exception {
		messagesToHttpStatusRulesEngine = new MessagesToHttpStatusRulesEngine();
		Message errMessage = new Message(MessageSeverity.ERROR, "ErrorKey", "Error Text");
		messagesInResponse.add(errMessage);
	}

	@After
	public void tearDown() throws Exception {
		messagesInResponse.clear();
	}

	@Test
	public void testMessagesToHttpStatus() {
		Message errMessage = new Message(MessageSeverity.ERROR, "ErrorKey", "Error Text");		
		MessageKeySeverityMatchRule errorRule = new MessageKeySeverityMatchRule(errMessage, HttpStatus.UNAUTHORIZED);
		messagesToHttpStatusRulesEngine.addRule(errorRule);
		assertEquals(HttpStatus.UNAUTHORIZED,messagesToHttpStatusRulesEngine.messagesToHttpStatus(messagesInResponse));
	}


}
