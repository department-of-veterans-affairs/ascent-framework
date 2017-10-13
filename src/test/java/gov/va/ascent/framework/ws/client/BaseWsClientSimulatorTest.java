package gov.va.ascent.framework.ws.client;

import static org.junit.Assert.*;

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
