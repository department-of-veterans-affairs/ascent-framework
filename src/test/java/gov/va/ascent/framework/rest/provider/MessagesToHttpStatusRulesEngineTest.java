package gov.va.ascent.framework.rest.provider;

import gov.va.ascent.framework.messages.HttpStatusForMessage;
import gov.va.ascent.framework.messages.Message;
import gov.va.ascent.framework.messages.MessageSeverity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static gov.va.ascent.framework.rest.provider.MessagesToHttpStatusRulesEngine.evalMessagesAgainstRules;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
		assertEquals(HttpStatus.UNAUTHORIZED, messagesToHttpStatusRulesEngine.messagesToHttpStatus(messagesInResponse));
	}

	@Test
	public void testAddRule() throws Exception {
		messagesToHttpStatusRulesEngine.addRule(null);
		messagesInResponse.clear();
		assertNull(messagesToHttpStatusRulesEngine.messagesToHttpStatus(messagesInResponse));
	}

	@Test
	public void testEvalMessagesAgainstRulesFor5xxErrors() throws Exception {
		Message message = new Message();
		message.setStatus(HttpStatusForMessage.INTERNAL_SERVER_ERROR);

		Message errMessage = new Message(MessageSeverity.ERROR, "ErrorKey", "Error Text");
		MessageKeySeverityMatchRule errorRule = new MessageKeySeverityMatchRule(errMessage, HttpStatus.UNAUTHORIZED);
		Set<MessagesToHttpStatusRule> setOfRules = new LinkedHashSet<MessagesToHttpStatusRule>();
		setOfRules.add(errorRule);

		assertTrue(HttpStatus.INTERNAL_SERVER_ERROR
				.equals(evalMessagesAgainstRules(Arrays.asList(new Message[] { message }), setOfRules)));
	}

	@Test
	public void testEvalMessagesAgainstRulesFor4xxErrors() throws Exception {
		Message message = new Message();
		message.setStatus(HttpStatusForMessage.BAD_REQUEST);

		Message errMessage = new Message(MessageSeverity.ERROR, "ErrorKey", "Error Text");
		MessageKeySeverityMatchRule errorRule = new MessageKeySeverityMatchRule(errMessage, HttpStatus.UNAUTHORIZED);
		Set<MessagesToHttpStatusRule> setOfRules = new LinkedHashSet<MessagesToHttpStatusRule>();
		setOfRules.add(errorRule);

		assertTrue(HttpStatus.BAD_REQUEST.equals(evalMessagesAgainstRules(Arrays.asList(new Message[] { message }), setOfRules)));
	}

	@Test
	public void testEvalMessagesAgainstRulesFor5xxAnd4xxErrors() throws Exception {
		Message message1 = new Message();
		message1.setStatus(HttpStatusForMessage.INTERNAL_SERVER_ERROR);

		Message message2 = new Message();
		message2.setStatus(HttpStatusForMessage.BAD_REQUEST);

		Message errMessage = new Message(MessageSeverity.ERROR, "ErrorKey", "Error Text");
		MessageKeySeverityMatchRule errorRule = new MessageKeySeverityMatchRule(errMessage, HttpStatus.UNAUTHORIZED);
		Set<MessagesToHttpStatusRule> setOfRules = new LinkedHashSet<MessagesToHttpStatusRule>();
		setOfRules.add(errorRule);

		assertTrue(HttpStatus.BAD_REQUEST
				.equals(evalMessagesAgainstRules(Arrays.asList(new Message[] { message1, message2 }), setOfRules)));
	}

}
