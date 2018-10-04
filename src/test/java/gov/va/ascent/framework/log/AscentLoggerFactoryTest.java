package gov.va.ascent.framework.log;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.slf4j.ILoggerFactory;

import gov.va.ascent.framework.exception.AscentRuntimeException;

public class AscentLoggerFactoryTest {

	@Test
	public final void testAscentLoggerFactory() throws NoSuchMethodException, SecurityException {
		Constructor<AscentLoggerFactory> constructor = AscentLoggerFactory.class.getDeclaredConstructor(null);
		constructor.setAccessible(true);
		try {
			constructor.newInstance(null);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			assertTrue(InvocationTargetException.class.equals(e.getClass()));
			assertTrue(AscentRuntimeException.class.equals(e.getCause().getClass()));
		}
	}

	@Test
	public final void testGetLoggerClass() {
		AscentLogger logger = AscentLoggerFactory.getLogger(this.getClass());
		assertNotNull(logger);
		assertTrue(logger.getName().equals(this.getClass().getName()));
	}

	@Test
	public final void testGetLoggerString() {
		AscentLogger logger = AscentLoggerFactory.getLogger(this.getClass().getName());
		assertNotNull(logger);
		assertTrue(logger.getName().equals(this.getClass().getName()));
	}

	@Test
	public final void testGetBoundFactory() {
		ILoggerFactory factory = AscentLoggerFactory.getBoundFactory();
		assertNotNull(factory);
	}
}
