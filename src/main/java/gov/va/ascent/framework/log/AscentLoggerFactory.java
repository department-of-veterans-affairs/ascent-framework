package gov.va.ascent.framework.log;

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
	 */
	public static final AscentLogger getLogger(Class<?> clazz) {
		return AscentLogger.getLogger(LoggerFactory.getLogger(clazz));
	}

}
