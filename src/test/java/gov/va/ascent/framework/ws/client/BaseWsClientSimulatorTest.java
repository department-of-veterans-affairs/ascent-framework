package gov.va.ascent.framework.ws.client;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BaseWsClientSimulatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBaseWsClientSimulator() {
		BaseWsClientSimulator test = new BaseWsClientSimulator();
		assertNotNull(test);
	}

	@Test
	public void testGetSimulatorResponseByFileNameString() {
		try {
			String retVal = BaseWsClientSimulator.getSimulatorResponseByFileName("testWsClientSimulator1.txt");
			assertNotNull(retVal);
			assertTrue("UnitTestData1".equals(retVal));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetResponseStreamByFileName() {
		try {
			InputStream inputStream = BaseWsClientSimulator.getSimulatorResponseStreamByFileName("testWsClientSimulator1.txt");
			assertNotNull(inputStream);
			assertTrue("UnitTestData1".equals(IOUtils.toString(inputStream, StandardCharsets.UTF_8.name())));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetSimulatorResponseByFileNameStringString() {
		try {
			String retVal = BaseWsClientSimulator.getSimulatorResponseByFileName("testWsClientSimulator.txt","testWsClientSimulator2.txt");
			assertNotNull(retVal);
			assertTrue("UnitTestData2".equals(retVal));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
