package gov.va.ascent.framework.swagger;

/**
 * The Interface SwaggerResponseMessages contains response messages for documenting into
 * the swagger documentation.  This is to ensure consistent messaging (and ideally behavior)
 * for services.
 * 
 * @author akulkarn
 */
//akulkarn: interface constants is an approved pattern here, this rule should be disabled, but @SuppressWarnings doesn't work
//CHECKSTYLE:OFF
public interface SwaggerResponseMessages {
//CHECKSTYLE:ON
	/** The Constant MESSAGE_200. */
	static final String MESSAGE_200 = "A Response which indicates a successful Request.  The Response may contain \"messages\" that could describe warnings or further information.";
	
	/** The Constant MESSAGE_403. */
	static final String MESSAGE_403 = "The request is not authorized.  Please verify credentials used in the request.";
	
	/** The Constant MESSAGE_400. */
	static final String MESSAGE_400 = "There was an error encountered processing the Request.  Response will contain a  \"messages\" element that will provide further information on the error.  This request shouldn't be retried until corrected.";
	
	/** The Constant MESSAGE_500. */
	static final String MESSAGE_500 = "There was an error encountered processing the Request.  Response will contain a  \"messages\" element that will provide further information on the error.  Please retry.  If problem persists, please contact support with a copy of the Response.";
	

}