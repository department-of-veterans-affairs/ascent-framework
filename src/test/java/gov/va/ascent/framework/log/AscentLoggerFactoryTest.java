package gov.va.ascent.framework.log;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

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
	public final void testGetLogger() {
		AscentLogger logger = AscentLoggerFactory.getLogger(this.getClass());
		assertNotNull(logger);
		System.out.println(logger.getName());
		assert logger.getName().equals(this.getClass().getName());
	}

}
