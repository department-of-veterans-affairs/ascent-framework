package gov.va.ascent.framework.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import gov.va.ascent.framework.audit.AuditLogger;
import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;
import gov.va.ascent.framework.messages.Message;
import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.rest.provider.BaseRestProviderAspect;
import gov.va.ascent.framework.validation.Validatable;
import gov.va.ascent.framework.validation.ViolationMessageParts;

/**
 * The Class ServiceValidationToMessageAspect is an aspect that performs validation (i.e. JSR 303) on the
 * standard service methods which are validatable, converting validation errors into Message objects in a consistent way.
 *
 * Standard service operations which are validatable are those which are...
 * (1) public
 * (2) return a ServiceResponse and
 * (3) have a single input that is of the type Validatable.
 *
 * @See gov.va.ascent.framework.service.ServiceResponse
 * @see gov.va.ascent.framework.validation.Validatable
 *
 * @author jshrader
 */
@Aspect
@Order(-9998)
public class ServiceValidationToMessageAspect extends BaseServiceAspect {

	private static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(ServiceValidationToMessageAspect.class);

	/**
	 * Around advice for{@link BaseServiceAspect#serviceImpl()} pointcut.
	 * <p>
	 * This method will execute JSR-303 validations on any {@link Validatable} parameter objects in the method signature.<br/>
	 * Any failed validations is added to the method's response object, and is audit logged.
	 *
	 * @param joinPoint
	 * @return Object
	 * @throws Throwable
	 */
	@Around("publicStandardServiceMethod() && serviceImpl()")
	public Object aroundAdvice(final ProceedingJoinPoint joinPoint) throws Throwable {

		ServiceResponse serviceResponse = null;

		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("ServiceValidationToMessageAspect executing around method:" + joinPoint.toLongString());
			}

			// fetch the request
			List<Object> serviceRequest = null;
			if (joinPoint.getArgs().length > 0) {
				serviceRequest = Arrays.asList(joinPoint.getArgs());
			}
			if (serviceRequest == null) {
				serviceRequest = new ArrayList<>();
			}

			// start creating the response
			final Map<String, List<ViolationMessageParts>> messages = new LinkedHashMap<>();

			if (joinPoint.getArgs().length > 0) {
				final MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
				serviceResponse = validateRequest(methodSignature, serviceRequest, messages);
			}

			if (serviceResponse == null) {
				serviceResponse = (ServiceResponse) joinPoint.proceed();
			}
		} catch (final Throwable throwable) {
			LOGGER.error("ServiceValidationToMessageAspect encountered " + throwable.getClass().getName()
					+ ": " + throwable.getMessage(), throwable);
			throw throwable;
		} finally {
			LOGGER.debug("ServiceValidationToMessageAspect after method was called.");
		}

		return serviceResponse;

	}

	/**
	 * Convert map to messages. This is exposed so services can call directly if they desire.
	 *
	 * @param serviceResponse the service response
	 * @param messages the messages
	 */
	protected static void convertMapToMessages(final ServiceResponse serviceResponse,
			final Map<String, List<ViolationMessageParts>> messages) {
		for (final Entry<String, List<ViolationMessageParts>> entry : messages.entrySet()) {
			for (final ViolationMessageParts fieldError : entry.getValue()) {
				serviceResponse.addMessage(MessageSeverity.ERROR, fieldError.getNewKey(), fieldError.getText());
			}
		}
		Collections.sort(serviceResponse.getMessages(), Comparator.comparing(Message::getKey));
	}

	/**
	 * Validate any validatable objects on the serviceRequest list.
	 *
	 * @param methodSignature
	 * @param serviceRequest
	 * @param messages
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private ServiceResponse validateRequest(MethodSignature methodSignature, List<Object> serviceRequest,
			Map<String, List<ViolationMessageParts>> messages)
			throws InstantiationException, IllegalAccessException {
		ServiceResponse serviceResponse = null;
		for (final Object objValidatable : serviceRequest) {
			if (objValidatable != null && objValidatable instanceof Validatable) {
				((Validatable) objValidatable).validate(messages);
			}
		}
		if (!messages.isEmpty()) {
			serviceResponse = (ServiceResponse) methodSignature.getMethod().getReturnType().newInstance();
			convertMapToMessages(serviceResponse, messages);
			AuditLogger.error(
					BaseRestProviderAspect.getDefaultAuditableInstance(methodSignature.getMethod()),
					serviceResponse.getMessages().stream().map(Message::toString).reduce("", String::concat));
		}
		return serviceResponse;
	}
}