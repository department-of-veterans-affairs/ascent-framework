package gov.va.ascent.framework.log;

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import gov.va.ascent.framework.exception.AscentRuntimeException;

/**
 * This class wraps the SLF4J logger to add logging enhancements for the Ascent platform.
 * <p>
 * If a future upgrade of SLF4J changes the Logger interface, changes will be required in the AscentLogger class.
 *
 * @author aburkholder
 */
public final class AscentLoggerFactory {

	/**
	 * Do not instantiate.
	 */
	private AscentLoggerFactory() {
		throw new AscentRuntimeException("AscentLoggerFactory is a static class. Do not instantiate it.");
	}

	/**
	 * Gets a SLF4J-compliant logger, enhanced for Ascent applications, for the specified class.
	 *
	 * @param clazz the Class for which logging is desired
	 * @return AscentLogger
	 * @see org.slf4j.LoggerFactory#getLogger(Class)
	 */
	public static final AscentLogger getLogger(Class<?> clazz) {
		return AscentLogger.getLogger(LoggerFactory.getLogger(clazz));
	}

	/**
	 * Gets a SLF4J-compliant logger, enhanced for Ascent applications, for the specified name.
	 *
	 * @param name the name under which logging is desired
	 * @return AscentLogger
	 * @see org.slf4j.LoggerFactory#getLogger(String)
	 */
	public static final AscentLogger getLogger(String name) {
		return AscentLogger.getLogger(LoggerFactory.getLogger(name));
	}

	/**
	 * Get the implementation of the logger factory that is bound to SLF4J, that serves as the basis for AscentLoggerFactory.
	 *
	 * @return ILoggerFactory an instance of the bound factory implementation
	 * @see org.slf4j.LoggerFactory#getILoggerFactory()
	 */
	public static final ILoggerFactory getBoundFactory() {
		return LoggerFactory.getILoggerFactory();
	}

}
