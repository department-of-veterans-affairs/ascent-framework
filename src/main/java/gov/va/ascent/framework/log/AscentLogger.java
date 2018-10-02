package gov.va.ascent.framework.log;

/**
 * A logger for the Ascent platform that extends and enhances org.slf4j.Logger.
 * <p>
 * Get an instance of this logger with the static {@link #getLogger(Logger)} method.
 * <p>
 * If the SLF4J Logger interface changes, so should this class.
 *
 * @author aburkholder
 */
public class AscentLogger extends AscentBaseLogger {

	/**
	 * Private constructor to control instantiation.
	 *
	 * @param logger an org.slf4j.Logger.Logger
	 */
	private AscentLogger(org.slf4j.Logger logger) {
		super(logger);
	}

	/**
	 * Get an AscentLogger.
	 *
	 * @param logger an org.slf4j.Logger.Logger
	 * @return AscentLogger
	 */
	public static final AscentLogger getLogger(org.slf4j.Logger logger) {
		return new AscentLogger(logger);
	}

	/**
	 * Get the interface implementation of the underlying logger (currently the SLF4J instance).
	 *
	 * @return org.slf4j.Logger
	 */
	public org.slf4j.Logger getLoggerInterfaceImpl() {
		return super.logger;
	}

	/**
	 * Get the actual bound implementation of the logger (currently the logback logger).
	 *
	 * @return
	 */
	public ch.qos.logback.classic.Logger getLoggerBoundImpl() {
		// Could also do something like this too (remove superfluous spaces) ...
		// C l a s s < ? > impl C l a s s =
		// S t a t i c Logger Binder. get Singleton ( ). get Logger Factory ( ).
		// get Logger ( super. logger. getName ( ) ). get Class ( ) ;
		// return impl C l a s s. cast ( super. logger ) ;
		return (ch.qos.logback.classic.Logger) super.logger;
	}

	/**
	 * Log a message having an ASCII Art banner at the DEBUG level.
	 *
	 * @param banner the banner
	 * @param msg the message string to be logged
	 * @since 1.4
	 */
	public void debug(AscentLogBanner banner, String msg) {
		logger.debug(banner.getBanner());
		logger.debug(msg);
	}

	/**
	 * Log a message having an ASCII Art banner at the DEBUG level according to the specified format
	 * and argument.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the DEBUG level.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arg the argument
	 */
	public void debug(AscentLogBanner banner, String format, Object arg) {
		logger.debug(banner.getBanner());
		logger.debug(format, arg);
	}

	/**
	 * Log a message having an ASCII Art banner at the DEBUG level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the DEBUG level.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void debug(AscentLogBanner banner, String format, Object arg1, Object arg2) {
		logger.debug(banner.getBanner());
		logger.debug(format, arg1, arg2);
	}

	/**
	 * Log a message having an ASCII Art banner at the DEBUG level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the DEBUG level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for DEBUG. The variants taking
	 * {@link #debug(String, Object) one} and {@link #debug(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void debug(AscentLogBanner banner, String format, Object... arguments) {
		logger.debug(banner.getBanner());
		logger.debug(format, arguments);
	}

	/**
	 * Log an exception (throwable) having an ASCII Art banner at the DEBUG level with an
	 * accompanying message.
	 *
	 * @param bannerText the short text to be converted to ASCII art
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void debug(AscentLogBanner banner, String msg, Throwable t) {
		logger.debug(banner.getBanner());
		logger.debug(msg, t);
	}

	/**
	 * Log a message having an ASCII Art banner at the INFO level.
	 *
	 * @param banner the banner
	 * @param msg the message string to be logged
	 */
	public void info(AscentLogBanner banner, String msg) {
		logger.info(banner.getBanner());
		logger.info(msg);
	}

	/**
	 * Log a message having an ASCII Art banner at the INFO level according to the specified format
	 * and argument.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the INFO level.
	 *
	 * @param bannerthe banner
	 * @param format the format string
	 * @param arg the argument
	 */
	public void info(AscentLogBanner banner, String format, Object arg) {
		logger.info(banner.getBanner());
		logger.info(format, arg);
	}

	/**
	 * Log a message having an ASCII Art banner at the INFO level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the INFO level.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void info(AscentLogBanner banner, String format, Object arg1, Object arg2) {
		logger.info(banner.getBanner());
		logger.info(format, arg1, arg2);
	}

	/**
	 * Log a message having an ASCII Art banner at the INFO level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the INFO level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for INFO. The variants taking
	 * {@link #info(String, Object) one} and {@link #info(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void info(AscentLogBanner banner, String format, Object... arguments) {
		logger.info(banner.getBanner());
		logger.info(format, arguments);
	}

	/**
	 * Log an exception (throwable) having an ASCII Art banner at the INFO level with an
	 * accompanying message.
	 *
	 * @param banner the banner
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void info(AscentLogBanner banner, String msg, Throwable t) {
		logger.info(banner.getBanner());
		logger.info(msg, t);
	}

	/**
	 * Log a message having an ASCII Art banner at the WARN level.
	 *
	 * @param banner the banner
	 * @param msg the message string to be logged
	 */
	public void warn(AscentLogBanner banner, String msg) {
		logger.warn(banner.getBanner());
		logger.warn(msg);
	}

	/**
	 * Log a message having an ASCII Art banner at the WARN level according to the specified format
	 * and argument.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the WARN level.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arg the argument
	 */
	public void warn(AscentLogBanner banner, String format, Object arg) {
		logger.warn(banner.getBanner());
		logger.warn(format, arg);
	}

	/**
	 * Log a message having an ASCII Art banner at the WARN level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the WARN level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for WARN. The variants taking
	 * {@link #warn(String, Object) one} and {@link #warn(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void warn(AscentLogBanner banner, String format, Object... arguments) {
		logger.warn(banner.getBanner());
		logger.warn(format, arguments);
	}

	/**
	 * Log a message having an ASCII Art banner at the WARN level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the WARN level.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void warn(AscentLogBanner banner, String format, Object arg1, Object arg2) {
		logger.warn(banner.getBanner());
		logger.warn(format, arg1, arg2);
	}

	/**
	 * Log an exception (throwable) having an ASCII Art banner at the WARN level with an
	 * accompanying message.
	 *
	 * @param banner the banner
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void warn(AscentLogBanner banner, String msg, Throwable t) {
		logger.warn(banner.getBanner());
		logger.warn(msg, t);
	}

	/**
	 * Log a message having an ASCII Art banner at the ERROR level.
	 *
	 * @param banner the banner
	 * @param msg the message string to be logged
	 */
	public void error(AscentLogBanner banner, String msg) {
		logger.error(banner.getBanner());
		logger.error(msg);
	}

	/**
	 * Log a message having an ASCII Art banner at the ERROR level according to the specified format
	 * and argument.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arg the argument
	 */
	public void error(AscentLogBanner banner, String format, Object arg) {
		logger.error(banner.getBanner());
		logger.error(format, arg);
	}

	/**
	 * Log a message having an ASCII Art banner at the ERROR level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void error(AscentLogBanner banner, String format, Object arg1, Object arg2) {
		logger.error(banner.getBanner());
		logger.error(format, arg1, arg2);
	}

	/**
	 * Log a message having an ASCII Art banner at the ERROR level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the ERROR level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for ERROR. The variants taking
	 * {@link #error(String, Object) one} and {@link #error(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void error(AscentLogBanner banner, String format, Object... arguments) {
		logger.error(banner.getBanner());
		logger.error(format, arguments);
	}

	/**
	 * Log an exception (throwable) having an ASCII Art banner at the ERROR level with an
	 * accompanying message.
	 *
	 * @param banner the banner
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void error(AscentLogBanner banner, String msg, Throwable t) {
		logger.error(banner.getBanner());
		logger.error(msg, t);
	}

	//
	//
	// =======================================================================
	// =======================================================================
	// ======= ­                                                        =======
	// ======= Everything below here is copied out of org.slf4j.Logger =======
	// ======= with relevant code block added.                         =======
	// =======                                                         =======
	// ======= If the SLF4J Logger interface changes, so should this.  =======
	// =======                                                         =======
	// =======================================================================
	// =======================================================================
	//
	//

	/**
	 * Return the name of this <code>Logger</code> instance.
	 *
	 * @return name of this logger instance
	 */
	public String getName() {
		return logger.getName();
	}

	/**
	 * Is the logger instance enabled for the TRACE level?
	 *
	 * @return True if this Logger is enabled for the TRACE level,
	 *         false otherwise.
	 * @since 1.4
	 */
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	/**
	 * Log a message at the TRACE level.
	 *
	 * @param msg the message string to be logged
	 * @since 1.4
	 */
	public void trace(String msg) {
		logger.trace(msg);
	}

	/**
	 * Log a message at the TRACE level according to the specified format
	 * and argument.
	 * <p/>
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the TRACE level.
	 * </p>
	 *
	 * @param format the format string
	 * @param arg the argument
	 * @since 1.4
	 */
	public void trace(String format, Object arg) {
		logger.trace(format, arg);
	}

	/**
	 * Log a message at the TRACE level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the TRACE level.
	 * </p>
	 *
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 * @since 1.4
	 */
	public void trace(String format, Object arg1, Object arg2) {
		logger.trace(format, arg1, arg2);
	}

	/**
	 * Log a message at the TRACE level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the TRACE level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for TRACE. The variants taking {@link #trace(String, Object) one} and
	 * {@link #trace(String, Object, Object) two} arguments exist solely in order to avoid this hidden cost.
	 * </p>
	 *
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 * @since 1.4
	 */
	public void trace(String format, Object... arguments) {
		logger.trace(format, arguments);
	}

	/**
	 * Log an exception (throwable) at the TRACE level with an
	 * accompanying message.
	 *
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 * @since 1.4
	 */
	public void trace(String msg, Throwable t) {
		logger.trace(msg, t);
	}

	/**
	 * Similar to {@link #isTraceEnabled()} method except that the
	 * marker data is also taken into account.
	 *
	 * @param marker The marker data to take into consideration
	 * @return True if this Logger is enabled for the TRACE level,
	 *         false otherwise.
	 *
	 * @since 1.4
	 */
	public boolean isTraceEnabled(org.slf4j.Marker marker) {
		return logger.isTraceEnabled(marker);
	}

	/**
	 * Log a message with the specific Marker at the TRACE level.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param msg the message string to be logged
	 * @since 1.4
	 */
	public void trace(org.slf4j.Marker marker, String msg) {
		logger.trace(marker, msg);
	}

	/**
	 * This method is similar to {@link #trace(String, Object)} method except that the
	 * marker data is also taken into consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arg the argument
	 * @since 1.4
	 */
	public void trace(org.slf4j.Marker marker, String format, Object arg) {
		logger.trace(marker, format, arg);
	}

	/**
	 * This method is similar to {@link #trace(String, Object, Object)}
	 * method except that the marker data is also taken into
	 * consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 * @since 1.4
	 */
	public void trace(org.slf4j.Marker marker, String format, Object arg1, Object arg2) {
		logger.trace(marker, format, arg1, arg2);
	}

	/**
	 * This method is similar to {@link #trace(String, Object...)}
	 * method except that the marker data is also taken into
	 * consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param argArray an array of arguments
	 * @since 1.4
	 */
	public void trace(org.slf4j.Marker marker, String format, Object... argArray) {
		logger.trace(marker, format, argArray);
	}

	/**
	 * This method is similar to {@link #trace(String, Throwable)} method except that the
	 * marker data is also taken into consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 * @since 1.4
	 */
	public void trace(org.slf4j.Marker marker, String msg, Throwable t) {
		logger.trace(marker, msg, t);
	}

	/**
	 * Is the logger instance enabled for the DEBUG level?
	 *
	 * @return True if this Logger is enabled for the DEBUG level,
	 *         false otherwise.
	 */
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	/**
	 * Log a message at the DEBUG level.
	 *
	 * @param msg the message string to be logged
	 */
	public void debug(String msg) {
		logger.debug(msg);
	}

	/**
	 * Log a message at the DEBUG level according to the specified format
	 * and argument.
	 * <p/>
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the DEBUG level.
	 * </p>
	 *
	 * @param format the format string
	 * @param arg the argument
	 */
	public void debug(String format, Object arg) {
		logger.debug(format, arg);
	}

	/**
	 * Log a message at the DEBUG level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the DEBUG level.
	 * </p>
	 *
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void debug(String format, Object arg1, Object arg2) {
		logger.debug(format, arg1, arg2);
	}

	/**
	 * Log a message at the DEBUG level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the DEBUG level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for DEBUG. The variants taking
	 * {@link #debug(String, Object) one} and {@link #debug(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.
	 * </p>
	 *
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void debug(String format, Object... arguments) {
		logger.debug(format, arguments);
	}

	/**
	 * Log an exception (throwable) at the DEBUG level with an
	 * accompanying message.
	 *
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void debug(String msg, Throwable t) {
		logger.debug(msg, t);
	}

	/**
	 * Similar to {@link #isDebugEnabled()} method except that the
	 * marker data is also taken into account.
	 *
	 * @param marker The marker data to take into consideration
	 * @return True if this Logger is enabled for the DEBUG level,
	 *         false otherwise.
	 */
	public boolean isDebugEnabled(org.slf4j.Marker marker) {
		return logger.isDebugEnabled(marker);
	}

	/**
	 * Log a message with the specific Marker at the DEBUG level.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param msg the message string to be logged
	 */
	public void debug(org.slf4j.Marker marker, String msg) {
		logger.debug(marker, msg);
	}

	/**
	 * This method is similar to {@link #debug(String, Object)} method except that the
	 * marker data is also taken into consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arg the argument
	 */
	public void debug(org.slf4j.Marker marker, String format, Object arg) {
		logger.debug(marker, format, arg);
	}

	/**
	 * This method is similar to {@link #debug(String, Object, Object)}
	 * method except that the marker data is also taken into
	 * consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void debug(org.slf4j.Marker marker, String format, Object arg1, Object arg2) {
		logger.debug(marker, format, arg1, arg2);
	}

	/**
	 * This method is similar to {@link #debug(String, Object...)}
	 * method except that the marker data is also taken into
	 * consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void debug(org.slf4j.Marker marker, String format, Object... arguments) {
		logger.debug(marker, format, arguments);
	}

	/**
	 * This method is similar to {@link #debug(String, Throwable)} method except that the
	 * marker data is also taken into consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void debug(org.slf4j.Marker marker, String msg, Throwable t) {
		logger.debug(marker, msg, t);
	}

	/**
	 * Is the logger instance enabled for the INFO level?
	 *
	 * @return True if this Logger is enabled for the INFO level,
	 *         false otherwise.
	 */
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	/**
	 * Log a message at the INFO level.
	 *
	 * @param msg the message string to be logged
	 */
	public void info(String msg) {
		logger.info(msg);
	}

	/**
	 * Log a message at the INFO level according to the specified format
	 * and argument.
	 * <p/>
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the INFO level.
	 * </p>
	 *
	 * @param format the format string
	 * @param arg the argument
	 */
	public void info(String format, Object arg) {
		logger.info(format, arg);
	}

	/**
	 * Log a message at the INFO level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the INFO level.
	 * </p>
	 *
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void info(String format, Object arg1, Object arg2) {
		logger.info(format, arg1, arg2);
	}

	/**
	 * Log a message at the INFO level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the INFO level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for INFO. The variants taking
	 * {@link #info(String, Object) one} and {@link #info(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.
	 * </p>
	 *
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void info(String format, Object... arguments) {
		logger.info(format, arguments);
	}

	/**
	 * Log an exception (throwable) at the INFO level with an
	 * accompanying message.
	 *
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void info(String msg, Throwable t) {
		logger.info(msg, t);
	}

	/**
	 * Similar to {@link #isInfoEnabled()} method except that the marker
	 * data is also taken into consideration.
	 *
	 * @param marker The marker data to take into consideration
	 * @return true if this logger is warn enabled, false otherwise
	 */
	public boolean isInfoEnabled(org.slf4j.Marker marker) {
		return logger.isInfoEnabled(marker);
	}

	/**
	 * Log a message with the specific Marker at the INFO level.
	 *
	 * @param marker The marker specific to this log statement
	 * @param msg the message string to be logged
	 */
	public void info(org.slf4j.Marker marker, String msg) {
		logger.info(marker, msg);
	}

	/**
	 * This method is similar to {@link #info(String, Object)} method except that the
	 * marker data is also taken into consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arg the argument
	 */
	public void info(org.slf4j.Marker marker, String format, Object arg) {
		logger.info(marker, format, arg);
	}

	/**
	 * This method is similar to {@link #info(String, Object, Object)}
	 * method except that the marker data is also taken into
	 * consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void info(org.slf4j.Marker marker, String format, Object arg1, Object arg2) {
		logger.info(marker, format, arg1, arg2);
	}

	/**
	 * This method is similar to {@link #info(String, Object...)}
	 * method except that the marker data is also taken into
	 * consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void info(org.slf4j.Marker marker, String format, Object... arguments) {
		logger.info(marker, format, arguments);
	}

	/**
	 * This method is similar to {@link #info(String, Throwable)} method
	 * except that the marker data is also taken into consideration.
	 *
	 * @param marker the marker data for this log statement
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void info(org.slf4j.Marker marker, String msg, Throwable t) {
		logger.info(marker, msg, t);
	}

	/**
	 * Is the logger instance enabled for the WARN level?
	 *
	 * @return True if this Logger is enabled for the WARN level,
	 *         false otherwise.
	 */
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	/**
	 * Log a message at the WARN level.
	 *
	 * @param msg the message string to be logged
	 */
	public void warn(String msg) {
		logger.warn(msg);
	}

	/**
	 * Log a message at the WARN level according to the specified format
	 * and argument.
	 * <p/>
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the WARN level.
	 * </p>
	 *
	 * @param format the format string
	 * @param arg the argument
	 */
	public void warn(String format, Object arg) {
		logger.warn(format, arg);
	}

	/**
	 * Log a message at the WARN level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the WARN level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for WARN. The variants taking
	 * {@link #warn(String, Object) one} and {@link #warn(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.
	 * </p>
	 *
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void warn(String format, Object... arguments) {
		logger.warn(format, arguments);
	}

	/**
	 * Log a message at the WARN level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the WARN level.
	 * </p>
	 *
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void warn(String format, Object arg1, Object arg2) {
		logger.warn(format, arg1, arg2);
	}

	/**
	 * Log an exception (throwable) at the WARN level with an
	 * accompanying message.
	 *
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void warn(String msg, Throwable t) {
		logger.warn(msg, t);
	}

	/**
	 * Similar to {@link #isWarnEnabled()} method except that the marker
	 * data is also taken into consideration.
	 *
	 * @param marker The marker data to take into consideration
	 * @return True if this Logger is enabled for the WARN level,
	 *         false otherwise.
	 */
	public boolean isWarnEnabled(org.slf4j.Marker marker) {
		return logger.isWarnEnabled(marker);
	}

	/**
	 * Log a message with the specific Marker at the WARN level.
	 *
	 * @param marker The marker specific to this log statement
	 * @param msg the message string to be logged
	 */
	public void warn(org.slf4j.Marker marker, String msg) {
		logger.warn(marker, msg);
	}

	/**
	 * This method is similar to {@link #warn(String, Object)} method except that the
	 * marker data is also taken into consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arg the argument
	 */
	public void warn(org.slf4j.Marker marker, String format, Object arg) {
		logger.warn(marker, format, arg);
	}

	/**
	 * This method is similar to {@link #warn(String, Object, Object)}
	 * method except that the marker data is also taken into
	 * consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void warn(org.slf4j.Marker marker, String format, Object arg1, Object arg2) {
		logger.warn(marker, format, arg1, arg2);
	}

	/**
	 * This method is similar to {@link #warn(String, Object...)}
	 * method except that the marker data is also taken into
	 * consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void warn(org.slf4j.Marker marker, String format, Object... arguments) {
		logger.warn(marker, format, arguments);
	}

	/**
	 * This method is similar to {@link #warn(String, Throwable)} method
	 * except that the marker data is also taken into consideration.
	 *
	 * @param marker the marker data for this log statement
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void warn(org.slf4j.Marker marker, String msg, Throwable t) {
		logger.warn(marker, msg, t);
	}

	/**
	 * Is the logger instance enabled for the ERROR level?
	 *
	 * @return True if this Logger is enabled for the ERROR level,
	 *         false otherwise.
	 */
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	/**
	 * Log a message at the ERROR level.
	 *
	 * @param msg the message string to be logged
	 */
	public void error(String msg) {
		logger.error(msg);
	}

	/**
	 * Log a message at the ERROR level according to the specified format
	 * and argument.
	 * <p/>
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level.
	 * </p>
	 *
	 * @param format the format string
	 * @param arg the argument
	 */
	public void error(String format, Object arg) {
		logger.error(format, arg);
	}

	/**
	 * Log a message at the ERROR level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level.
	 * </p>
	 *
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void error(String format, Object arg1, Object arg2) {
		logger.error(format, arg1, arg2);
	}

	/**
	 * Log a message at the ERROR level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the ERROR level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for ERROR. The variants taking
	 * {@link #error(String, Object) one} and {@link #error(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.
	 * </p>
	 *
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void error(String format, Object... arguments) {
		logger.error(format, arguments);
	}

	/**
	 * Log an exception (throwable) at the ERROR level with an
	 * accompanying message.
	 *
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void error(String msg, Throwable t) {
		logger.error(msg, t);
	}

	/**
	 * Similar to {@link #isErrorEnabled()} method except that the
	 * marker data is also taken into consideration.
	 *
	 * @param marker The marker data to take into consideration
	 * @return True if this Logger is enabled for the ERROR level,
	 *         false otherwise.
	 */
	public boolean isErrorEnabled(org.slf4j.Marker marker) {
		return logger.isErrorEnabled(marker);
	}

	/**
	 * Log a message with the specific Marker at the ERROR level.
	 *
	 * @param marker The marker specific to this log statement
	 * @param msg the message string to be logged
	 */
	public void error(org.slf4j.Marker marker, String msg) {
		logger.error(marker, msg);
	}

	/**
	 * This method is similar to {@link #error(String, Object)} method except that the
	 * marker data is also taken into consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arg the argument
	 */
	public void error(org.slf4j.Marker marker, String format, Object arg) {
		logger.error(marker, format, arg);
	}

	/**
	 * This method is similar to {@link #error(String, Object, Object)}
	 * method except that the marker data is also taken into
	 * consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void error(org.slf4j.Marker marker, String format, Object arg1, Object arg2) {
		logger.error(marker, format, arg1, arg2);
	}

	/**
	 * This method is similar to {@link #error(String, Object...)}
	 * method except that the marker data is also taken into
	 * consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void error(org.slf4j.Marker marker, String format, Object... arguments) {
		logger.error(marker, format, arguments);
	}

	/**
	 * This method is similar to {@link #error(String, Throwable)}
	 * method except that the marker data is also taken into
	 * consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void error(org.slf4j.Marker marker, String msg, Throwable t) {
		logger.error(marker, msg, t);
	}
}
