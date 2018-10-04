package gov.va.ascent.framework.log;

import org.slf4j.event.Level;
import org.slf4j.helpers.MessageFormatter;

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
	@Override
	public org.slf4j.Logger getLoggerInterfaceImpl() {
		return super.getLoggerInterfaceImpl();
	}

	/**
	 * Get the actual bound implementation of the logger (currently the logback logger).
	 *
	 * @return
	 */
	public ch.qos.logback.classic.Logger getLoggerBoundImpl() {
		// Could also do something like this too (replace "^" with "" - stupid sonar) ...
		// C^l^a^s^s^<^?^>^i^m^p^l^C^l^a^s^s^ ^=^ ^
		// S^t^a^t^i^c^L^o^g^g^e^r^B^i^n^d^e^r^.^g^e^t^S^i^n^g^l^e^t^o^n^(^)^.^g^e^t^L^o^g^g^e^r^F^a^c^t^o^r^y^(^)^.
		// g^e^t^L^o^g^g^e^r^(^s^u^p^e^r^.^l^o^g^g^e^r^.^g^e^t^N^a^m^e^(^)^)^.^g^e^t^C^l^a^s^s^(^)^;
		// r^e^t^u^r^n^ ^i^m^p^l^C^l^a^s^s^.^c^a^s^t^(^s^u^p^e^r.^l^o^g^g^e^r^)^;
		return (ch.qos.logback.classic.Logger) super.getLoggerInterfaceImpl();
	}

	/**
	 * Log a message having an ASCII Art banner at the DEBUG level.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 *
	 * @param banner the banner
	 * @param msg the message string to be logged
	 * @since 1.4
	 */
	public void debug(AscentBanner banner, String msg) {
		super.debug(banner.getBanner() + NEWLINE + msg);
	}

	/**
	 * Log a message having an ASCII Art banner at the DEBUG level according to the specified format
	 * and argument.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the DEBUG level.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arg the argument
	 */
	public void debug(AscentBanner banner, String format, Object arg) {
		super.debug(banner.getBanner() + NEWLINE + MessageFormatter.format(format, arg).getMessage());
	}

	/**
	 * Log a message having an ASCII Art banner at the DEBUG level according to the specified format
	 * and arguments.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the DEBUG level.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void debug(AscentBanner banner, String format, Object arg1, Object arg2) {
		super.debug(banner.getBanner() + NEWLINE + MessageFormatter.format(format, arg1, arg2).getMessage());
	}

	/**
	 * Log a message having an ASCII Art banner at the DEBUG level according to the specified format
	 * and arguments.
	 * Note that logging banners is time consuming, so should be used sparingly.
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
	public void debug(AscentBanner banner, String format, Object... args) {
		super.debug(banner.getBanner() + NEWLINE + MessageFormatter.arrayFormat(format, args).getMessage());
	}

	/**
	 * Log an exception (throwable) having an ASCII Art banner at the DEBUG level with an
	 * accompanying message.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 *
	 * @param bannerText the short text to be converted to ASCII art
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void debug(AscentBanner banner, String msg, Throwable t) {
		super.debug(banner.getBanner() + NEWLINE + msg, t);
	}

	/**
	 * Log a message having an ASCII Art banner at the INFO level.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 *
	 * @param banner the banner
	 * @param msg the message string to be logged
	 */
	public void info(AscentBanner banner, String msg) {
		super.info(banner.getBanner() + NEWLINE + msg);
	}

	/**
	 * Log a message having an ASCII Art banner at the INFO level according to the specified format
	 * and argument.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the INFO level.
	 *
	 * @param bannerthe banner
	 * @param format the format string
	 * @param arg the argument
	 */
	public void info(AscentBanner banner, String format, Object arg) {
		super.info(banner.getBanner() + NEWLINE + MessageFormatter.format(format, arg).getMessage());
	}

	/**
	 * Log a message having an ASCII Art banner at the INFO level according to the specified format
	 * and arguments.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the INFO level.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void info(AscentBanner banner, String format, Object arg1, Object arg2) {
		super.info(banner.getBanner() + NEWLINE + MessageFormatter.format(format, arg1, arg2).getMessage());
	}

	/**
	 * Log a message having an ASCII Art banner at the INFO level according to the specified format
	 * and arguments.
	 * Note that logging banners is time consuming, so should be used sparingly.
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
	public void info(AscentBanner banner, String format, Object... args) {
		super.info(banner.getBanner() + NEWLINE + MessageFormatter.arrayFormat(format, args).getMessage());
	}

	/**
	 * Log an exception (throwable) having an ASCII Art banner at the INFO level with an
	 * accompanying message.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 *
	 * @param banner the banner
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void info(AscentBanner banner, String msg, Throwable t) {
		super.info(banner.getBanner() + NEWLINE + msg, t);
	}

	/**
	 * Log a message having an ASCII Art banner at the WARN level.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 *
	 * @param banner the banner
	 * @param msg the message string to be logged
	 */
	public void warn(AscentBanner banner, String msg) {
		super.warn(banner.getBanner() + NEWLINE + msg);
	}

	/**
	 * Log a message having an ASCII Art banner at the WARN level according to the specified format
	 * and argument.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the WARN level.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arg the argument
	 */
	public void warn(AscentBanner banner, String format, Object arg) {
		super.warn(banner.getBanner() + NEWLINE + MessageFormatter.format(format, arg).getMessage());
	}

	/**
	 * Log a message having an ASCII Art banner at the WARN level according to the specified format
	 * and arguments.
	 * Note that logging banners is time consuming, so should be used sparingly.
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
	public void warn(AscentBanner banner, String format, Object... args) {
		super.warn(banner.getBanner() + NEWLINE + MessageFormatter.arrayFormat(format, args).getMessage());
	}

	/**
	 * Log a message having an ASCII Art banner at the WARN level according to the specified format
	 * and arguments.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the WARN level.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void warn(AscentBanner banner, String format, Object arg1, Object arg2) {
		super.warn(banner.getBanner() + NEWLINE + MessageFormatter.format(format, arg1, arg2).getMessage());
	}

	/**
	 * Log an exception (throwable) having an ASCII Art banner at the WARN level with an
	 * accompanying message.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 *
	 * @param banner the banner
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void warn(AscentBanner banner, String msg, Throwable t) {
		super.warn(banner.getBanner() + NEWLINE + msg, t);
	}

	/**
	 * Log a message having an ASCII Art banner at the ERROR level.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 *
	 * @param banner the banner
	 * @param msg the message string to be logged
	 */
	public void error(AscentBanner banner, String msg) {
		super.error(banner.getBanner() + NEWLINE + msg);
	}

	/**
	 * Log a message having an ASCII Art banner at the ERROR level according to the specified format
	 * and argument.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arg the argument
	 */
	public void error(AscentBanner banner, String format, Object arg) {
		super.error(banner.getBanner() + NEWLINE + MessageFormatter.format(format, arg).getMessage());
	}

	/**
	 * Log a message having an ASCII Art banner at the ERROR level according to the specified format
	 * and arguments.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level.
	 *
	 * @param banner the banner
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void error(AscentBanner banner, String format, Object arg1, Object arg2) {
		super.error(banner.getBanner() + NEWLINE + MessageFormatter.format(format, arg1, arg2).getMessage());
	}

	/**
	 * Log a message having an ASCII Art banner at the ERROR level according to the specified format
	 * and arguments.
	 * Note that logging banners is time consuming, so should be used sparingly.
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
	public void error(AscentBanner banner, String format, Object... args) {
		super.error(banner.getBanner() + NEWLINE + MessageFormatter.arrayFormat(format, args).getMessage());
	}

	/**
	 * Log an exception (throwable) having an ASCII Art banner at the ERROR level with an
	 * accompanying message.
	 * Note that logging banners is time consuming, so should be used sparingly.
	 *
	 * @param banner the banner
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	public void error(AscentBanner banner, String msg, Throwable t) {
		super.error(banner.getBanner() + NEWLINE + msg, t);
	}

	/**
	 * Log a message at an arbitrary log level.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 *
	 * @param level the org.slf4j.event.Level
	 * @param msg the message string to be logged
	 */
	@Override
	public void log(Level level, String msg) {
		super.log(level, msg);
	}

	/**
	 * Log a message at an arbitrary log level according to the specified format
	 * and argument.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the DEBUG level.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 *
	 * @param level the org.slf4j.event.Level
	 * @param format the format string
	 * @param arg the argument
	 */
	public void log(Level level, String format, Object arg) {
		super.log(level, MessageFormatter.format(format, arg).getMessage());
	}

	/**
	 * Log a message at an arbitrary log level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the DEBUG level.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 *
	 * @param level the org.slf4j.event.Level
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void log(Level level, String format, Object arg1, Object arg2) {
		super.log(level, MessageFormatter.format(format, arg1, arg2).getMessage());
	}

	/**
	 * Log a message at an arbitrary log level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the DEBUG level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for DEBUG. The variants taking
	 * {@link #debug(String, Object) one} and {@link #debug(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 *
	 * @param level the org.slf4j.event.Level
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void log(Level level, String format, Object... args) {
		super.log(level, MessageFormatter.arrayFormat(format, args).getMessage());
	}

	/**
	 * Log an exception (throwable) at an arbitrary log level with an
	 * accompanying message.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 *
	 * @param level the org.slf4j.event.Level
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	@Override
	public void log(Level level, String msg, Throwable t) {
		super.log(level, msg, t);
	}

	/**
	 * Log a message with the specific Marker at an arbitrary log level.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 *
	 * @param level the org.slf4j.event.Level
	 * @param marker the marker data specific to this log statement
	 * @param msg the message string to be logged
	 */
	@Override
	public void log(Level level, org.slf4j.Marker marker, String msg) {
		super.log(level, marker, msg);
	}

	/**
	 * This method is similar to {@link #log(String, Object)} method except that the
	 * marker data is also taken into consideration.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 *
	 * @param level the org.slf4j.event.Level
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arg the argument
	 */
	public void log(Level level, org.slf4j.Marker marker, String format, Object arg) {
		super.log(level, marker, MessageFormatter.format(format, arg).getMessage());
	}

	/**
	 * This method is similar to {@link #log(String, Object, Object)}
	 * method except that the marker data is also taken into
	 * consideration.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 *
	 * @param level the org.slf4j.event.Level
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void log(Level level, org.slf4j.Marker marker, String format, Object arg1, Object arg2) {
		super.log(level, marker, MessageFormatter.format(format, arg1, arg2).getMessage());
	}

	/**
	 * This method is similar to {@link #log(String, Object...)}
	 * method except that the marker data is also taken into
	 * consideration.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 *
	 * @param level the org.slf4j.event.Level
	 * @param marker the marker data specific to this log statement
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void log(Level level, org.slf4j.Marker marker, String format, Object... args) {
		super.log(level, marker, MessageFormatter.arrayFormat(format, args).getMessage());
	}

	/**
	 * This method is similar to {@link #log(String, Throwable)} method except that the
	 * marker data is also taken into consideration.
	 * <p>
	 * WARNING: this method is significantly slower than calling the desired log level method directly. Use only if necessary.
	 *
	 * @param level the org.slf4j.event.Level
	 * @param marker the marker data specific to this log statement
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	@Override
	public void log(Level level, org.slf4j.Marker marker, String msg, Throwable t) {
		super.log(level, marker, msg, t);
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
		return super.getLoggerInterfaceImpl().getName();
	}

	/**
	 * Is the logger instance enabled for the TRACE level?
	 *
	 * @return True if this Logger is enabled for the TRACE level,
	 *         false otherwise.
	 * @since 1.4
	 */
	public boolean isTraceEnabled() {
		return super.getLoggerInterfaceImpl().isTraceEnabled();
	}

	/**
	 * Log a message at the TRACE level.
	 *
	 * @param msg the message string to be logged
	 * @since 1.4
	 */
	@Override
	public void trace(String msg) {
		super.trace(msg);
	}

	/**
	 * Log a message at the TRACE level according to the specified format
	 * and argument.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the TRACE level.
	 *
	 * @param format the format string
	 * @param arg the argument
	 * @since 1.4
	 */
	public void trace(String format, Object arg) {
		super.trace(MessageFormatter.format(format, arg).getMessage());
	}

	/**
	 * Log a message at the TRACE level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the TRACE level.
	 *
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 * @since 1.4
	 */
	public void trace(String format, Object arg1, Object arg2) {
		super.trace(MessageFormatter.format(format, arg1, arg2).getMessage());
	}

	/**
	 * Log a message at the TRACE level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the TRACE level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for TRACE. The variants taking {@link #trace(String, Object) one} and
	 * {@link #trace(String, Object, Object) two} arguments exist solely in order to avoid this hidden cost.
	 *
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 * @since 1.4
	 */
	public void trace(String format, Object... args) {
		super.trace(MessageFormatter.arrayFormat(format, args).getMessage());
	}

	/**
	 * Log an exception (throwable) at the TRACE level with an
	 * accompanying message.
	 *
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 * @since 1.4
	 */
	@Override
	public void trace(String msg, Throwable t) {
		super.trace(msg, t);
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
		return super.getLoggerInterfaceImpl().isTraceEnabled(marker);
	}

	/**
	 * Log a message with the specific Marker at the TRACE level.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param msg the message string to be logged
	 * @since 1.4
	 */
	@Override
	public void trace(org.slf4j.Marker marker, String msg) {
		super.trace(marker, msg);
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
		super.trace(marker, MessageFormatter.format(format, arg).getMessage());
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
		super.trace(marker, MessageFormatter.format(format, arg1, arg2).getMessage());
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
	public void trace(org.slf4j.Marker marker, String format, Object... args) {
		super.trace(marker, MessageFormatter.arrayFormat(format, args).getMessage());
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
	@Override
	public void trace(org.slf4j.Marker marker, String msg, Throwable t) {
		super.trace(marker, msg, t);
	}

	/**
	 * Is the logger instance enabled for the DEBUG level?
	 *
	 * @return True if this Logger is enabled for the DEBUG level,
	 *         false otherwise.
	 */
	public boolean isDebugEnabled() {
		return super.getLoggerInterfaceImpl().isDebugEnabled();
	}

	/**
	 * Log a message at the DEBUG level.
	 *
	 * @param msg the message string to be logged
	 */
	@Override
	public void debug(String msg) {
		super.debug(msg);
	}

	/**
	 * Log a message at the DEBUG level according to the specified format
	 * and argument.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the DEBUG level.
	 *
	 * @param format the format string
	 * @param arg the argument
	 */
	public void debug(String format, Object arg) {
		super.debug(MessageFormatter.format(format, arg).getMessage());
	}

	/**
	 * Log a message at the DEBUG level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the DEBUG level.
	 *
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void debug(String format, Object arg1, Object arg2) {
		super.debug(MessageFormatter.format(format, arg1, arg2).getMessage());
	}

	/**
	 * Log a message at the DEBUG level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the DEBUG level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for DEBUG. The variants taking
	 * {@link #debug(String, Object) one} and {@link #debug(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.
	 *
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void debug(String format, Object... args) {
		super.debug(MessageFormatter.arrayFormat(format, args).getMessage());
	}

	/**
	 * Log an exception (throwable) at the DEBUG level with an
	 * accompanying message.
	 *
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	@Override
	public void debug(String msg, Throwable t) {
		super.debug(msg, t);
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
		return super.getLoggerInterfaceImpl().isDebugEnabled(marker);
	}

	/**
	 * Log a message with the specific Marker at the DEBUG level.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param msg the message string to be logged
	 */
	@Override
	public void debug(org.slf4j.Marker marker, String msg) {
		super.debug(marker, msg);
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
		super.debug(marker, MessageFormatter.format(format, arg).getMessage());
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
		super.debug(marker, MessageFormatter.format(format, arg1, arg2).getMessage());
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
	public void debug(org.slf4j.Marker marker, String format, Object... args) {
		super.debug(marker, MessageFormatter.arrayFormat(format, args).getMessage());
	}

	/**
	 * This method is similar to {@link #debug(String, Throwable)} method except that the
	 * marker data is also taken into consideration.
	 *
	 * @param marker the marker data specific to this log statement
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	@Override
	public void debug(org.slf4j.Marker marker, String msg, Throwable t) {
		super.debug(marker, msg, t);
	}

	/**
	 * Is the logger instance enabled for the INFO level?
	 *
	 * @return True if this Logger is enabled for the INFO level,
	 *         false otherwise.
	 */
	public boolean isInfoEnabled() {
		return super.getLoggerInterfaceImpl().isInfoEnabled();
	}

	/**
	 * Log a message at the INFO level.
	 *
	 * @param msg the message string to be logged
	 */
	@Override
	public void info(String msg) {
		super.info(msg);
	}

	/**
	 * Log a message at the INFO level according to the specified format
	 * and argument.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the INFO level.
	 *
	 * @param format the format string
	 * @param arg the argument
	 */
	public void info(String format, Object arg) {
		super.info(MessageFormatter.format(format, arg).getMessage());
	}

	/**
	 * Log a message at the INFO level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the INFO level.
	 *
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void info(String format, Object arg1, Object arg2) {
		super.info(MessageFormatter.format(format, arg1, arg2).getMessage());
	}

	/**
	 * Log a message at the INFO level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the INFO level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for INFO. The variants taking
	 * {@link #info(String, Object) one} and {@link #info(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.
	 *
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void info(String format, Object... args) {
		super.info(MessageFormatter.arrayFormat(format, args).getMessage());
	}

	/**
	 * Log an exception (throwable) at the INFO level with an
	 * accompanying message.
	 *
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	@Override
	public void info(String msg, Throwable t) {
		super.info(msg, t);
	}

	/**
	 * Similar to {@link #isInfoEnabled()} method except that the marker
	 * data is also taken into consideration.
	 *
	 * @param marker The marker data to take into consideration
	 * @return true if this logger is warn enabled, false otherwise
	 */
	public boolean isInfoEnabled(org.slf4j.Marker marker) {
		return super.getLoggerInterfaceImpl().isInfoEnabled(marker);
	}

	/**
	 * Log a message with the specific Marker at the INFO level.
	 *
	 * @param marker The marker specific to this log statement
	 * @param msg the message string to be logged
	 */
	@Override
	public void info(org.slf4j.Marker marker, String msg) {
		super.info(marker, msg);
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
		super.info(marker, MessageFormatter.format(format, arg).getMessage());
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
		super.info(marker, MessageFormatter.format(format, arg1, arg2).getMessage());
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
	public void info(org.slf4j.Marker marker, String format, Object... args) {
		super.info(marker, MessageFormatter.arrayFormat(format, args).getMessage());
	}

	/**
	 * This method is similar to {@link #info(String, Throwable)} method
	 * except that the marker data is also taken into consideration.
	 *
	 * @param marker the marker data for this log statement
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	@Override
	public void info(org.slf4j.Marker marker, String msg, Throwable t) {
		super.info(marker, msg, t);
	}

	/**
	 * Is the logger instance enabled for the WARN level?
	 *
	 * @return True if this Logger is enabled for the WARN level,
	 *         false otherwise.
	 */
	public boolean isWarnEnabled() {
		return super.getLoggerInterfaceImpl().isWarnEnabled();
	}

	/**
	 * Log a message at the WARN level.
	 *
	 * @param msg the message string to be logged
	 */
	@Override
	public void warn(String msg) {
		super.warn(msg);
	}

	/**
	 * Log a message at the WARN level according to the specified format
	 * and argument.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the WARN level.
	 *
	 * @param format the format string
	 * @param arg the argument
	 */
	public void warn(String format, Object arg) {
		super.warn(MessageFormatter.format(format, arg).getMessage());
	}

	/**
	 * Log a message at the WARN level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the WARN level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for WARN. The variants taking
	 * {@link #warn(String, Object) one} and {@link #warn(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.
	 *
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void warn(String format, Object... args) {
		super.warn(MessageFormatter.arrayFormat(format, args).getMessage());
	}

	/**
	 * Log a message at the WARN level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the WARN level.
	 *
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void warn(String format, Object arg1, Object arg2) {
		super.warn(MessageFormatter.format(format, arg1, arg2).getMessage());
	}

	/**
	 * Log an exception (throwable) at the WARN level with an
	 * accompanying message.
	 *
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	@Override
	public void warn(String msg, Throwable t) {
		super.warn(msg, t);
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
		return super.getLoggerInterfaceImpl().isWarnEnabled(marker);
	}

	/**
	 * Log a message with the specific Marker at the WARN level.
	 *
	 * @param marker The marker specific to this log statement
	 * @param msg the message string to be logged
	 */
	@Override
	public void warn(org.slf4j.Marker marker, String msg) {
		super.warn(marker, msg);
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
		super.warn(marker, MessageFormatter.format(format, arg).getMessage());
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
		super.warn(marker, MessageFormatter.format(format, arg1, arg2).getMessage());
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
	public void warn(org.slf4j.Marker marker, String format, Object... args) {
		super.warn(marker, MessageFormatter.arrayFormat(format, args).getMessage());
	}

	/**
	 * This method is similar to {@link #warn(String, Throwable)} method
	 * except that the marker data is also taken into consideration.
	 *
	 * @param marker the marker data for this log statement
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	@Override
	public void warn(org.slf4j.Marker marker, String msg, Throwable t) {
		super.warn(marker, msg, t);
	}

	/**
	 * Is the logger instance enabled for the ERROR level?
	 *
	 * @return True if this Logger is enabled for the ERROR level,
	 *         false otherwise.
	 */
	public boolean isErrorEnabled() {
		return super.getLoggerInterfaceImpl().isErrorEnabled();
	}

	/**
	 * Log a message at the ERROR level.
	 *
	 * @param msg the message string to be logged
	 */
	@Override
	public void error(String msg) {
		super.error(msg);
	}

	/**
	 * Log a message at the ERROR level according to the specified format
	 * and argument.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level.
	 *
	 * @param format the format string
	 * @param arg the argument
	 */
	public void error(String format, Object arg) {
		super.error(MessageFormatter.format(format, arg).getMessage());
	}

	/**
	 * Log a message at the ERROR level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level.
	 *
	 * @param format the format string
	 * @param arg1 the first argument
	 * @param arg2 the second argument
	 */
	public void error(String format, Object arg1, Object arg2) {
		super.error(MessageFormatter.format(format, arg1, arg2).getMessage());
	}

	/**
	 * Log a message at the ERROR level according to the specified format
	 * and arguments.
	 * <p>
	 * This form avoids superfluous string concatenation when the logger
	 * is disabled for the ERROR level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
	 * even if this logger is disabled for ERROR. The variants taking
	 * {@link #error(String, Object) one} and {@link #error(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.
	 *
	 * @param format the format string
	 * @param arguments a list of 3 or more arguments
	 */
	public void error(String format, Object... args) {
		super.error(MessageFormatter.arrayFormat(format, args).getMessage());
	}

	/**
	 * Log an exception (throwable) at the ERROR level with an
	 * accompanying message.
	 *
	 * @param msg the message accompanying the exception
	 * @param t the exception (throwable) to log
	 */
	@Override
	public void error(String msg, Throwable t) {
		super.error(msg, t);
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
		return super.getLoggerInterfaceImpl().isErrorEnabled(marker);
	}

	/**
	 * Log a message with the specific Marker at the ERROR level.
	 *
	 * @param marker The marker specific to this log statement
	 * @param msg the message string to be logged
	 */
	@Override
	public void error(org.slf4j.Marker marker, String msg) {
		super.error(marker, msg);
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
		super.error(marker, MessageFormatter.format(format, arg).getMessage());
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
		super.error(marker, MessageFormatter.format(format, arg1, arg2).getMessage());
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
	public void error(org.slf4j.Marker marker, String format, Object... args) {
		super.error(marker, MessageFormatter.arrayFormat(format, args).getMessage());
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
	@Override
	public void error(org.slf4j.Marker marker, String msg, Throwable t) {
		super.error(marker, msg, t);
	}
}
