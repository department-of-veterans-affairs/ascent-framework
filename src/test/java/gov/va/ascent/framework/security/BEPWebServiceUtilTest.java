package gov.va.ascent.framework.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BEPWebServiceUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetExternalUIDWithoutHashGeneration() {
		final String retVal = BEPWebServiceUtil.getExternalUID("UnitTest_EVSS");
		assertNotNull(retVal);
		assertEquals(true, "UnitTest_EVSS".equals(retVal));

	}

	@Test
	public void testGetExternalUIDWithHashGeneration() {
		final String retVal = BEPWebServiceUtil.getExternalUID("UnitTestEVSSWithStringLengthGreaterThan39");
		assertNotNull(retVal);
		assertFalse("UnitTestEVSSWithStringLengthGreaterThan39".equals(retVal));
	}

	@Test
	public void testGetExternalKey() {
		final String retVal = BEPWebServiceUtil.getExternalKey("UnitTestEVSSKey");
		assertNotNull(retVal);
		assertTrue("UnitTestEVSSKey".equals(retVal));
	}

	@Test
	public void testGetClientMachine() {
		final String retVal = BEPWebServiceUtil.getClientMachine("localhost");
		assertNotNull(retVal);
	}

}
