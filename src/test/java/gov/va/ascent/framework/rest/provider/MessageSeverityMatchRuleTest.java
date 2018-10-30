package gov.va.ascent.framework.rest.provider;

import gov.va.ascent.framework.messages.Message;
import gov.va.ascent.framework.messages.MessageSeverity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MessageSeverityMatchRuleTest {
	
	MessageSeverityMatchRule messageSeverityMatchRule;
	Set<Message> messagesToEval = new HashSet<Message>();

	@Before
	public void setUp() throws Exception {
		Message errMessage = new Message(MessageSeverity.ERROR, "ErrorKey", "Error Text");
		Message fatalMessage = new Message(MessageSeverity.FATAL, "FatalKey", "Fatal Error Text");
		Message warnMessage = new Message(MessageSeverity.WARN, "WarnKey", "Warn Text");
		Message infoMessage = new Message(MessageSeverity.INFO, "InfoKey", "Info Text");
		messagesToEval.add(errMessage);
		messagesToEval.add(fatalMessage);
		messagesToEval.add(warnMessage);
		messagesToEval.add(infoMessage);		
	}

	@After
	public void tearDown() throws Exception {
		messagesToEval.clear();
	}

	@Test
	public void testEval() {
		messageSeverityMatchRule = new MessageSeverityMatchRule(MessageSeverity.ERROR,HttpStatus.UNAUTHORIZED);
		assertEquals(HttpStatus.UNAUTHORIZED,messageSeverityMatchRule.eval(messagesToEval));
	}

	@Test
	public void testEvalMessagesNull() {
		messageSeverityMatchRule = new MessageSeverityMatchRule(MessageSeverity.ERROR,HttpStatus.UNAUTHORIZED);
		assertEquals(null,messageSeverityMatchRule.eval(null));
	}

	@Test
	public void testEvalMessagesEmpty() {
		messageSeverityMatchRule = new MessageSeverityMatchRule(MessageSeverity.ERROR,HttpStatus.UNAUTHORIZED);
		assertEquals(null,messageSeverityMatchRule.eval(new HashSet<Message>()));
	}

	@Test
	public void testEvalMessagesDoNotMatch() {
		Message msg = new Message(MessageSeverity.INFO, "InfoKey", "Info Text");
		Set<Message> messages = new HashSet<>();
		messages.add(msg);
		messageSeverityMatchRule = new MessageSeverityMatchRule(MessageSeverity.ERROR,HttpStatus.UNAUTHORIZED);
		assertEquals(null,messageSeverityMatchRule.eval(messages));
	}

	@Test
	public void testToString() {
		messageSeverityMatchRule = new MessageSeverityMatchRule(MessageSeverity.ERROR,HttpStatus.UNAUTHORIZED);
		assertNotNull(messageSeverityMatchRule.toString());
	}

}
