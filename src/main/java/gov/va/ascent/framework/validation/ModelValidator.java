package gov.va.ascent.framework.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.beans.factory.annotation.Value;

import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;
import gov.va.ascent.framework.util.Defense;

/**
 * Validators to wrap JSR-303 Validation API, allowing for ad-hoc validation
 * from Spring Web Flow.
 *
 * @author jimmyray
 * @version 2.0
 */

public class ModelValidator implements Serializable {
	private static final long serialVersionUID = -6019704406389010935L;

	private static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(ModelValidator.class);

	private static final String DEFAULT_RESOURCE_BUNDLE = "ValidationMessages";

	@Value("${ascent.validation.messages.user-resource-bundle}")
	private String jsr303Validator;

	/** The factory. */
	private transient ValidatorFactory factory;

	/** The validator. */
	private transient Validator validator;

	public ModelValidator() {
		if (StringUtils.isBlank(jsr303Validator)) {
			LOGGER.info("No additional JSR303 resource bundles specified, using only the default ValidationMessages resource bundle.");
			factory = Validation.buildDefaultValidatorFactory();
		} else {
			factory = Validation.byDefaultProvider()
					.configure()
					.messageInterpolator(
							new ResourceBundleMessageInterpolator(
									new PlatformResourceBundleLocator(jsr303Validator),
									new PlatformResourceBundleLocator(DEFAULT_RESOURCE_BUNDLE)))
					.buildValidatorFactory();
		}
		validator = factory.getValidator();
	}

	/**
	 * Reset transient fields on deserialization.
	 *
	 * @return this, but with the transient state restored.
	 */
	public Object readResolve() {
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		return this;
	}

	/**
	 * Validates a model based on defined constraints.
	 *
	 * @param <T>
	 *            the data type of the model to validate
	 * @param model
	 *            the model to validate
	 * @param messages
	 *            a map in which to store the resulting validation messages. The
	 *            list of keys in this map is also used as the list of
	 *            properties to validate when in properties mode.
	 * @param groups
	 *            [optional] var args
	 * @return true if the model is valid, false if it is not.
	 */
	public <T extends Serializable> boolean validateModel(final T model,
			final Map<String, List<ViolationMessageParts>> messages, final Class<?>... groups) {

		Defense.notNull(messages, "messages can not be null");

		boolean isValid = true;

		final Class<?>[] classes = this.getGroups(groups);

		final Set<ConstraintViolation<T>> constraintViolations = doValidate(model, classes);

		if (!constraintViolations.isEmpty()) {
			isValid = false;
			convertConstraintViolationsToMessages(model.getClass(), null, messages, constraintViolations);
		}
		return isValid;
	}

	/**
	 * Validates a map of model properties based on map keys and optional
	 * groups.
	 *
	 * @param <T>
	 *            the data type of the model to validate
	 * @param model
	 *            the model to validate
	 * @param messages
	 *            Map whose keys are properties to validate.
	 * @param groups
	 *            [optional] var args
	 * @return true is the property is valid, false if it is not.
	 */
	public <T extends Serializable> boolean validateModelProperties(
			final T model, final Map<String, List<ViolationMessageParts>> messages,
			final Class<?>... groups) {

		Defense.notNull(messages, "messages can not be null");

		boolean isValid = true;

		final Class<?>[] classes = this.getGroups(groups);

		Set<ConstraintViolation<T>> constraintViolations = null;

		for (final String key : messages.keySet()) {
			constraintViolations = doValidateProperty(model, key, classes);

			if (!constraintViolations.isEmpty()) {
				isValid = false;
				convertConstraintViolationsToMessages(model.getClass(), key, messages, constraintViolations);
			}
		}

		return isValid;
	}

	/**
	 * Convert constraint violations to messages.
	 *
	 * @param <T> the generic type
	 * @param modelClass the model class
	 * @param specifiedPropertyPathKey the specified property path key
	 * @param messages the messages
	 * @param constraintViolations the constraint violations
	 */
	private <T extends Serializable> void convertConstraintViolationsToMessages(
			final Class<?> modelClass, final String specifiedPropertyPathKey,
			final Map<String, List<ViolationMessageParts>> messages, final Set<ConstraintViolation<T>> constraintViolations) {

		ConstraintViolation<T> blankViolation = null;
		for (final ConstraintViolation<T> violation : constraintViolations) {
			if (violation.getMessageTemplate() == null || violation.getMessageTemplate().length() == 0) {
				blankViolation = violation;
			}
		}
		constraintViolations.remove(blankViolation);

		for (final ConstraintViolation<T> violation : constraintViolations) {
			fillMessage(violation, modelClass, specifiedPropertyPathKey, messages);
		}

	}

	/**
	 * Puts constraint violation message on the messages list.
	 *
	 * @param violation
	 * @param modelClass
	 * @param specifiedPropertyPathKey
	 * @param messages
	 */
	private void fillMessage(final ConstraintViolation<?> violation, final Class<?> modelClass, final String specifiedPropertyPathKey,
			final Map<String, List<ViolationMessageParts>> messages) {
		// construct the key we will use for the property path
		// (used to consolidate all potential violations for this field)
		String propertyPathKey = null;
		if (specifiedPropertyPathKey == null) {
			if (violation.getPropertyPath().toString().isEmpty()) {
				propertyPathKey = modelClass.getSimpleName();
			} else {
				propertyPathKey = violation.getPropertyPath().toString();
			}
		} else {
			propertyPathKey = specifiedPropertyPathKey;
		}

		// construct the message parts object
		// (used to contain all aspects of the violation message)
		final ViolationMessageParts violationMessageParts = new ViolationMessageParts();
		String replacement = violation.getMessageTemplate();
		replacement = StringUtils.replaceAll(replacement, "\\{", "");
		replacement = StringUtils.replaceAll(replacement, "\\}", "");
		violationMessageParts.setOriginalKey(replacement);

		replacement = convertKeyToNodepathStyle(propertyPathKey, violation.getMessageTemplate());
		replacement = StringUtils.replaceAll(replacement, "\\{", "");
		replacement = StringUtils.replaceAll(replacement, "\\}", "");
		violationMessageParts.setNewKey(replacement);
		violationMessageParts.setText(violation.getMessage());

		LOGGER.debug("ViolationMessageParts: {}", ReflectionToStringBuilder.toString(violationMessageParts));

		// associate the violation and it's message parts with the property path
		List<ViolationMessageParts> messagePartsForPropertyPath;
		if (messages.containsKey(propertyPathKey)) {
			messagePartsForPropertyPath = messages.get(propertyPathKey);
		} else {
			messagePartsForPropertyPath = new ArrayList<>();
			messages.put(propertyPathKey, messagePartsForPropertyPath);
		}
		messagePartsForPropertyPath.add(violationMessageParts);
	}

	/**
	 * Removes the impl from message template. Strips out stuff like "org.hibernate.validator" and ".message" and "javax.validation"
	 *
	 * Our own custom validators are expected to follow this same exact standard JSR 303 pattern. Essentially...
	 * {<PACKAGE>.<RULE_OR_REASON>.message}
	 *
	 * We do this because once this validator runs and interpolates the message from the product impl,
	 * that key is used and no longer useful for us. We want to assign a new key that reflects the error
	 * type and the field type for override by downstream apps. If the message key isn't seen as a product
	 * key (aka it's an internal custom key) then we don't alter it.
	 *
	 * @param propertyKey the property key
	 * @param messageTemplate the message template
	 * @return the string
	 */
	public static String convertKeyToNodepathStyle(final String propertyKey, final String messageTemplate) {
		final String returnKey = messageTemplate;
		if (messageTemplate != null) {
			final String[] parts = messageTemplate.split("\\.");
			if (parts.length > 1 && parts[parts.length - 1].contains("message")) {
				if (StringUtils.isEmpty(propertyKey)) {
					return parts[parts.length - 2];
				} else {
					return propertyKey + "." + parts[parts.length - 2];
				}
			}
		}
		return returnKey;
	}

	/**
	 * Validates a model based on defined constraints. Invokes validate(model,
	 * groups) on the nested validator.
	 *
	 * @param <T>
	 *            the data type of the model to validate
	 * @param model
	 *            the model to validate
	 * @param groups
	 *            [optional] var args
	 * @return a set of validation failures.
	 */
	public <T extends Serializable> Set<ConstraintViolation<T>> doValidate(
			final T model, final Class<?>... groups) {
		return this.validator.validate(model, groups);
	}

	/**
	 * Validates a model based on defined constraints. Invokes validate(model,
	 * key groups) on the nested validator.
	 *
	 * @param <T>
	 *            the data type of the model to validate
	 * @param model
	 *            the model to validate
	 * @param key
	 *            the name of the property to validate.
	 * @param groups
	 *            [optional] var args
	 * @return a set of validation failures.
	 */
	public <T extends Serializable> Set<ConstraintViolation<T>> doValidateProperty(
			final T model, final String key, final Class<?>... groups) {
		return this.validator.validateProperty(model, key, groups);
	}

	/**
	 * Gets the from array.
	 *
	 * @param groups the groups
	 * @return the groups
	 */
	private Class<?>[] getGroups(final Class<?>... groups) {
		Class<?>[] classes = null;
		if (null == groups) {
			classes = new Class[1];
			classes[0] = Default.class;
		} else {
			classes = groups.clone();
		}
		return classes;
	}

	/**
	 * Model validation modes.
	 */
	public enum Modes {
		/**
		 * Validate the model as a whole.
		 */
		MODEL,
		/**
		 * validate a specified list of properties.
		 */
		PROPERTIES;
	}
}
