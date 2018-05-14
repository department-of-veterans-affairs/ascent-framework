package gov.va.ascent.framework.security;

import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gov.va.ascent.framework.security.BEPWebServiceUtil;

public class BEPWebServiceUtilTest {
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetExternalUIDWithoutHashGeneration() {
		String retVal = BEPWebServiceUtil.getExternalUID("UnitTest_EVSS");
		assertNotNull(retVal);
		assertEquals(true,"UnitTest_EVSS".equals(retVal));
		
		
	}

	@Test
	public void testGetExternalUIDWithHashGeneration() {
		String retVal = BEPWebServiceUtil.getExternalUID("UnitTestEVSSWithStringLengthGreaterThan39");
		assertNotNull(retVal);
		assertFalse("UnitTestEVSSWithStringLengthGreaterThan39".equals(retVal));
	}	
	
	@Test
	public void testGetExternalKey() {
		String retVal = BEPWebServiceUtil.getExternalKey("UnitTestEVSSKey");
		assertNotNull(retVal);
		assertTrue("UnitTestEVSSKey".equals(retVal));
	}

	@Test
	public void testGetClientMachine() {
		String retVal = BEPWebServiceUtil.
		getClientMachine("localhost");
		assertNotNull(retVal);
		assertTrue(StringUtils.contains(retVal, ":"));
	}

}
