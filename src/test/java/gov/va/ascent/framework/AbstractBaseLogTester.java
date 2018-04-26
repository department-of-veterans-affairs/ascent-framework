package gov.va.ascent.framework;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public abstract class AbstractBaseLogTester {

	private TestAppender testAppender = new TestAppender();

	private List<Logger> loggers = new ArrayList<Logger>();

	/**
	 * <p>
	 * Get the appender for the current JUnit class.
	 * </p>
	 * <p>
	 * NOTE: you must perform appender.clear() before using the appender.
	 * Old events from previous tests in the class will carry forward to the next test.
	 * This is because TestAppender.events must be static, or it will not work.
	 * </p>
	 *
	 * @return TestAppender
	 */
	protected TestAppender getAppender() {
		return testAppender;
	}

	/**
	 * <p>
	 * Get a logger that was created using the {@link #getLoggers(Class...)} method.
	 * </p>
	 * <p>
	 * If {@code getLoggers(Class[, Class...])} was not previously called, IllegalStateException is thrown.<br/>
	 * If the specified class does not have a logger then {@code null} is returned.
	 * Loggers must be included as a parameter in the call to {@code getLoggers(Class...)}).
	 * </p>
	 *
	 * @param clazz the Class for which the logger was created
	 * @return the associated logger, or {@code null}
	 */
	protected Logger getLogger(Class<?> clazz) {
		if (loggers == null) {
			loggers = new ArrayList<Logger>();
		}

		Logger logger = null;
		for (Logger l : loggers) {
			if (clazz.getName().equals(l.getName())) {
				logger = l;
				logger.setLevel(Level.DEBUG);
				break;
			}
		}
		if (logger == null) {
			logger = (Logger) LoggerFactory.getLogger(clazz);
			logger.setLevel(Level.DEBUG);
			loggers.add(logger);
		}
		return logger;
	}

	@Before
	public void setup() throws Throwable {
		// no-op
	}

	@After
	public void tearDown() {
		// clean up appender
		testAppender.clear();
		// clean up loggers
		if ((loggers != null) && (loggers.size() > 0)) {
			for (Logger logger : loggers) {
				logger.setLevel(Level.ERROR);
				logger.detachAndStopAllAppenders();
			}
		}
		loggers.clear();
	}
}
