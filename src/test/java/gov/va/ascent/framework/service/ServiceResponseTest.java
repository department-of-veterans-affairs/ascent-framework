package gov.va.ascent.framework.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.va.ascent.framework.validation.ViolationMessageParts;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gov.va.ascent.framework.messages.Message;
import gov.va.ascent.framework.messages.MessageSeverity;

public class ServiceResponseTest {

	private ServiceResponse mockServiceResponse;
	private List<Message> testMessages = new ArrayList<Message>();
	Message infoMessage;
	Message warnMessage;
	Message errorMessage;
	Message fatalMessage;
	
	@Before
	public void setUp() throws Exception {
		mockServiceResponse = new ServiceResponse();
		infoMessage = new Message(MessageSeverity.INFO, "InfoKey", "Dummy info text");
		warnMessage = new Message(MessageSeverity.WARN, "WarnKey", "Dummy warning text");
		errorMessage = new Message(MessageSeverity.ERROR, "ErrorKey", "Dummy error text");
		fatalMessage = new Message(MessageSeverity.FATAL, "FatalKey", "Dummy fatal text");
		addTestMessages();
	}

	private void addTestMessages() {
		testMessages.add(infoMessage);
		testMessages.add(warnMessage);
		testMessages.add(errorMessage);
		testMessages.add(fatalMessage);
	}
	
	@After
	public void tearDown() throws Exception {
		testMessages.clear();
	}

	@Test
	public void testAddMessage() {
		mockServiceResponse.addMessage(MessageSeverity.INFO, "InfoKey", "Dummy info text");
		assertNotNull(mockServiceResponse.validate(null));

		assertNotNull(mockServiceResponse.getMessages());
		assertEquals(1,mockServiceResponse.getMessages().size());
		
	}

	@Test
	public void testAddMessages() {
		mockServiceResponse.addMessages(testMessages);
		assertNotNull(mockServiceResponse.getMessages());
		Map<String, List<ViolationMessageParts>> messages = new HashMap<>();
		assertNotNull(mockServiceResponse.validate(messages));
		assertEquals(4,mockServiceResponse.getMessages().size());
	}

	@Test
	public void testGetMessages() {
		mockServiceResponse.addMessages(testMessages);
		assertNotNull(mockServiceResponse.getMessages());
		assertEquals(4,mockServiceResponse.getMessages().size());		
	}

	@Test
	public void testSetMessages() {
		mockServiceResponse.setMessages(testMessages);
		ServiceResponse serviceResponseForEqualsTest = new ServiceResponse();
		assertFalse(mockServiceResponse.equals(serviceResponseForEqualsTest));
		serviceResponseForEqualsTest.setMessages(testMessages);
		assertTrue(mockServiceResponse.equals(serviceResponseForEqualsTest));
		assertNotNull(mockServiceResponse.getMessages());
		assertEquals(4,mockServiceResponse.getMessages().size());
	}

	@Test
	public void testHasFatals() {
		mockServiceResponse.setMessages(testMessages);
		assertTrue(mockServiceResponse.hasFatals());
	}

	@Test
	public void testHasErrors() {
		mockServiceResponse.setMessages(testMessages);
		assertTrue(mockServiceResponse.hasErrors());
	}

	@Test
	public void testHasWarnings() {
		mockServiceResponse.setMessages(testMessages);
		assertTrue(mockServiceResponse.hasWarnings());
	}

	@Test
	public void testHasInfos() {
		mockServiceResponse.setMessages(testMessages);
		assertTrue(mockServiceResponse.hasInfos());
	}

}
