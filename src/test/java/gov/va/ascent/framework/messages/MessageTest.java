package gov.va.ascent.framework.messages;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MessageTest {

    @Test
    public void testEmptyConstructor() throws Exception {
        Message message = new Message();
        assertNull(message.getKey());
        assertNull(message.getSeverity());
        assertNull(message.getText());
    }

    @Test
    public void testSeverityKeyConstructor() throws Exception {
        Message message = new Message(MessageSeverity.ERROR, "UnitTestKey");
        assertEquals(MessageSeverity.ERROR, message.getSeverity());
        assertEquals("UnitTestKey", message.getKey());
    }

    @Test
    public void testSeverityKeyTextConstructor() throws Exception {
        Message message = new Message(MessageSeverity.WARN, "UnitTestKey", "TextMsg");
        assertEquals(MessageSeverity.WARN, message.getSeverity());
        assertEquals("UnitTestKey", message.getKey());
        assertEquals("TextMsg", message.getText());
    }

    @Test
    public void testSetters() throws Exception {
        Message message = new Message(MessageSeverity.WARN, "UnitTestKey", "TextMsg");
        assertEquals(MessageSeverity.WARN, message.getSeverity());
        assertEquals("UnitTestKey", message.getKey());
        assertEquals("TextMsg", message.getText());
        message.setKey("UpdatedKey");
        message.setSeverity(MessageSeverity.FATAL);
        message.setText("UpdatedText");
        assertEquals(MessageSeverity.FATAL, message.getSeverity());
        assertEquals("UpdatedKey", message.getKey());
        assertEquals("UpdatedText", message.getText());
    }

    @Test
    public void testEquals() throws Exception {
        Message message1 = new Message(MessageSeverity.INFO, "UnitTestKey", "TextMsg");
        Message message2 = new Message(MessageSeverity.INFO, "UnitTestKey", "Not included in equals determination");
        assertTrue(message1.equals(message2));
    }

    @Test
    public void testMessageSeverityValueOf() throws Exception {

        assertEquals(MessageSeverity.WARN, MessageSeverity.fromValue("WARN"));
        assertEquals("WARN", MessageSeverity.WARN.value());
    }

}
