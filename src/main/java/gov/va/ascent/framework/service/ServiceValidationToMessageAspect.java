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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import gov.va.ascent.framework.audit.AuditLogger;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceValidationToMessageAspect.class);

	@Around("publicStandardServiceMethod() && serviceImpl()")
	public Object aroundAdvice(final ProceedingJoinPoint joinPoint) throws Throwable {

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
		ServiceResponse serviceResponse = null;
		final Map<String, List<ViolationMessageParts>> messages = new LinkedHashMap<>();

		if (joinPoint.getArgs().length > 0) {
			final MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
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
		}

		try {
			if (serviceResponse == null) {
				serviceResponse = (ServiceResponse) joinPoint.proceed();
			}
		} catch (final Throwable throwable) {
			LOGGER.error("ServiceValidationToMessageAspect encountered uncaught exception. Throwable Cause.",
					throwable.getCause());
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
	public static void convertMapToMessages(final ServiceResponse serviceResponse,
			final Map<String, List<ViolationMessageParts>> messages) {
		for (final Entry<String, List<ViolationMessageParts>> entry : messages.entrySet()) {
			for (final ViolationMessageParts fieldError : entry.getValue()) {
				serviceResponse.addMessage(MessageSeverity.ERROR, fieldError.getNewKey(), fieldError.getText());
			}
		}
		Collections.sort(serviceResponse.getMessages(), Comparator.comparing(Message::getKey));
	}

}