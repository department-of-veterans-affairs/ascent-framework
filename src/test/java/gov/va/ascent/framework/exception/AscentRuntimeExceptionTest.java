package gov.va.ascent.framework.exception;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.hamcrest.CoreMatchers.containsString;

public class AscentRuntimeExceptionTest {

    @BeforeClass
    public static void setUp(){
        System.setProperty("server.name", "Test Server");
    }

    @Test
    public void instantiateBaseAscentExceptions() throws Exception {
        AscentRuntimeException ascentRuntimeException = new AscentRuntimeException();


        Assert.assertEquals("Test Server", ascentRuntimeException.getServerName());
        Assert.assertThat(ascentRuntimeException.getMessage(), containsString("Unique ID: ["));
        Assert.assertTrue(ascentRuntimeException.getUniqueId().matches("[0-9]+"));
        // length of 12 because current time in millis should be longer than 12 digits
        Assert.assertTrue(ascentRuntimeException.getUniqueId().length() > 12);
        Assert.assertEquals(null, ascentRuntimeException.getCleanMessage());
    }

    @Test
    public void getMessageTestServerName() throws Exception {
        AscentRuntimeException ascentRuntimeException = new AscentRuntimeException();

        Assert.assertEquals("Unique ID: [" + ascentRuntimeException.getUniqueId()
                + "] Server Name: [Test Server] "
                , ascentRuntimeException.getMessage());

    }

    @Test
    public void getMessageTestServerNameNull() throws Exception {
        //setup
        //do crazy reflection to make server name null
        Field field = AscentRuntimeException.class.getDeclaredField("SERVER_NAME");
        Field modifiersField = Field.class.getDeclaredField( "modifiers" );
        boolean isModifierAccessible = modifiersField.isAccessible();
        modifiersField.setAccessible( true );
        modifiersField.setInt( field, field.getModifiers() & ~Modifier.FINAL );
        boolean isAccessible = field.isAccessible();
        field.setAccessible( true );
        field.set(null, null );

        AscentRuntimeException ascentRuntimeException = new AscentRuntimeException();

        Assert.assertEquals("Unique ID: [" + ascentRuntimeException.getUniqueId() + "] "
                , ascentRuntimeException.getMessage());

        // Reset server name to Test Server
        field.set(null, "Test Server");
    }

    @Test
    public void getMessageTestCategoryNull() throws Exception {
        AscentRuntimeException ascentRuntimeException = new AscentRuntimeException();
        Assert.assertEquals("Unique ID: ["
                        + ascentRuntimeException.getUniqueId() + "] Server Name: [Test Server] "
                , ascentRuntimeException.getMessage());

    }

    @Test
    public void getSuperCauseTest() throws Exception {
        Throwable cause = new Throwable("test");
        AscentRuntimeException ascentRuntimeException = new AscentRuntimeException(cause);
        Assert.assertEquals("Unique ID: ["
                        + ascentRuntimeException.getUniqueId()
                        + "] Server Name: [Test Server] java.lang.Throwable: test"
                , ascentRuntimeException.getMessage());

    }

    @Test
    public void getMessageCauseAndMessageTest() throws Exception {
        Throwable cause = new Throwable("test");
        AscentRuntimeException ascentRuntimeException = new AscentRuntimeException("Test Message", cause);
        Assert.assertEquals("Unique ID: ["
                        + ascentRuntimeException.getUniqueId()
                        + "] Server Name: [Test Server] Test Message"
                , ascentRuntimeException.getMessage());

    }

    @Test
    public void getCleanMessageTest() throws Exception {
        Throwable cause = new Throwable("test");
        AscentRuntimeException ascentRuntimeException = new AscentRuntimeException(cause);
        Assert.assertEquals("java.lang.Throwable: test", ascentRuntimeException.getCleanMessage());

    }

}
