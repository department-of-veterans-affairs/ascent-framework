package gov.va.ascent.framework.util;

import java.util.Collection;

import org.springframework.util.Assert;

/**
 * Utility class to make runtime assertions against parameters. Currently delegates many of its exposed calls to
 * Spring's internal Assert class, however using this custom Defense interface is prefered as opposed to directly
 * using Springs internal class to avoid unnecessarily coupling clients to the Spring API. The Spring dependency could
 * be removed in future releases without affecting clients.
 *
 * @author Jon Shrader, Jimmy Ray
 */
public final class Defense {

	/**
	 * Instantiates a new defense.
	 */
	private Defense() {
	}

	/**
	 * Assert that the provided object is an instance of the provided class.
	 *
	 * @param clazz the clazz
	 * @param obj the obj
	 * @see Assert#isInstanceOf(Class, Object)
	 */
	public static void isInstanceOf(final Class<?> clazz, final Object obj) {
		Assert.isInstanceOf(clazz, obj);
	}

	/**
	 * Assert a boolean expression, throwing an IllegalStateException with the specified message
	 * if the expression evaluates to false.
	 *
	 * @param expression the expression
	 * @param message the message
	 * @see Assert#state(boolean, String)
	 */
	public static void state(final boolean expression, final String message) {
		Assert.state(expression, message);
	}

	/**
	 * Assert a boolean expression, throwing an IllegalStateException with default message
	 * if the expression evaluates to false.
	 *
	 * @param expression the expression
	 * @see Assert#state(boolean, String)
	 */
	public static void state(final boolean expression) {
		Assert.state(expression, "[Assertion failed] - this state invariant must be true");
	}

	/**
	 * Assert that an object is null, using a default message if the object is not null.
	 *
	 * @param ref the ref
	 * @see Assert#isNull(Object, String)
	 */
	public static void isNull(final Object ref) {
		Assert.isNull(ref, "[Assertion failed] - the object argument must be null");
	}

	/**
	 * Assert that an object is null, using the provided message if the object is not null.
	 *
	 * @param ref the ref
	 * @param message the message
	 * @see Assert#isNull(Object, String)
	 */
	public static void isNull(final Object ref, final String message) {
		Assert.isNull(ref, message);
	}

	/**
	 * Assert that an object is not null, using a default message if the object is null.
	 *
	 * @param ref the ref
	 * @see Assert#isNull(Object, String)
	 */
	public static void notNull(final Object ref) {
		Assert.notNull(ref, "[Assertion failed] - this argument is required; it must not be null");
	}

	/**
	 * Assert that an object is not null, using the provided message if the object is null.
	 *
	 * @param ref the ref
	 * @param message the message
	 * @see Assert#isNull(Object, String)
	 */
	public static void notNull(final Object ref, final String message) {
		Assert.notNull(ref, message);
	}

	/**
	 * Assert that the given String contains valid text content;
	 * that is, it must not be null and must contain at least one non-whitespace character.
	 * Uses a default message if the text does not pass the assertion.
	 *
	 * @param text the text
	 * @see Assert#hasText(String, String)
	 */
	public static void hasText(final String text) {
		Assert.hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	/**
	 * Assert that the given String contains valid text content;
	 * that is, it must not be null and must contain at least one non-whitespace character.
	 * Uses a default message if the text does not pass the assertion.
	 *
	 * @param text the text
	 * @param message the message
	 * @see Assert#hasText(String, String)
	 */
	public static void hasText(final String text, final String message) {
		Assert.hasText(text, message);
	}

	/**
	 * Assert that a collection contains elements;
	 * that is, it must not be null and must contain at least one element.
	 * Uses a default message if the ref does not pass the assertion.
	 *
	 * @param ref the ref
	 * @see Assert#notEmpty(Collection, String)
	 */
	public static void notEmpty(final Collection<?> ref) {
		Assert.notEmpty(ref, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
	}

	/**
	 * Assert that a collection contains elements;
	 * that is, it must not be null and must contain at least one element.
	 * Uses the provided message if the ref does not pass the assertion.
	 *
	 * @param ref the ref
	 * @param message the message
	 * @see Assert#notEmpty(Collection, String)
	 */
	public static void notEmpty(final Collection<?> ref, final String message) {
		Assert.notEmpty(ref, message);
	}

	/**
	 * Assert that an array contains elements;
	 * that is, it must not be null and must contain at least one element.
	 * Uses the provided message if the ref does not pass the assertion.
	 *
	 * @param ref the ref
	 * @param message the message
	 * @see Assert#notEmpty(Object[], String)
	 */
	public static void notEmpty(final String[] ref, final String message) {
		Assert.notEmpty(ref, message);
	}

	/**
	 * Assert a non-null boolean expression,
	 * throwing an IllegalArgumentException if the expression is null or evaluates to false.
	 * Uses a default message if the expression does not pass the assertion.
	 *
	 * @param expression the expression
	 * @see Assert#notNull(Object, String)
	 * @see Assert#isTrue(boolean, String)
	 */
	public static void isTrue(final Boolean expression) {
		Assert.notNull(expression, "[Assertion failed] - this argument is required; it must not be null");
		Assert.isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	/**
	 * Assert a non-null boolean expression,
	 * throwing an IllegalArgumentException if the expression is null or evaluates to false.
	 * Uses the provided message if the expression does not pass the assertion.
	 *
	 * @param expression the expression
	 * @param message the message
	 * @see Assert#notNull(Object, String)
	 * @see Assert#isTrue(boolean, String)
	 */
	public static void isTrue(final Boolean expression, final String message) {
		Assert.notNull(expression, message);
		Assert.isTrue(expression, message);
	}

}
