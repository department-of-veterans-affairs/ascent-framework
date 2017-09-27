package gov.va.ascent.framework.swagger;

import org.junit.Assert;
import org.junit.Test;

public class SwaggerResponseMessagesTest {

    private static final String RESPONSE_200_MESSAGE = "A Response which indicates a successful Request."
            + "  The Response may contain \"messages\" that could describe warnings or further information.";

    private static final String RESPONSE_403_MESSAGE = "The request is not authorized."
            +"  Please verify credentials used in the request.";

    private static final String RESPONSE_400_MESSAGE = "There was an error encountered processing the Request."
            + "  Response will contain a  \"messages\" element that will provide further information on the error."
            + "  This request shouldn't be retried until corrected.";

    private static final String RESPONSE_500_MESSAGE = "There was an error encountered processing the Request."
            + "  Response will contain a  \"messages\" element that will provide further information on the error."
            + "  Please retry.  If problem persists, please contact support with a copy of the Response.";

    SwaggerResponseMessages swaggerResponseMessages = new SwaggerResponseMessagesTestImpl();


    @Test
    public void response200MessageTextTest() throws Exception {
        Assert.assertEquals(RESPONSE_200_MESSAGE, this.swaggerResponseMessages.MESSAGE_200);
    }

    @Test
    public void response403MessageTextTest() throws Exception {
        Assert.assertEquals(RESPONSE_403_MESSAGE, this.swaggerResponseMessages.MESSAGE_403);
    }

    @Test
    public void response400MessageTextTest() throws Exception {
        Assert.assertEquals(RESPONSE_400_MESSAGE, this.swaggerResponseMessages.MESSAGE_400);
    }

    @Test
    public void response500MessageTextTest() throws Exception {
        Assert.assertEquals(RESPONSE_500_MESSAGE, this.swaggerResponseMessages.MESSAGE_500);
    }

    class SwaggerResponseMessagesTestImpl implements SwaggerResponseMessages {

    }
}
