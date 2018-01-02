package gov.va.ascent.framework.security;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.authority.AuthorityUtils;

import gov.va.ascent.framework.security.PersonTraits.PATTERN_FORMAT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PersonTraitsTest {

	PersonTraits personTraitsDefault = new PersonTraits("NA","NA", AuthorityUtils.NO_AUTHORITIES);
	SimpleDateFormat format1 = new SimpleDateFormat(PATTERN_FORMAT.BIRTHDATE_YYYYMMDD.getPattern());
	
	@Before
	public void setUp() throws Exception {
		personTraitsDefault.setDodedipnid("TestDodedipnid");
		personTraitsDefault.setFirstName("FN");
		personTraitsDefault.setLastName("LN");
		personTraitsDefault.setMiddleName("MN");
		personTraitsDefault.setPrefix("Dr.");
		personTraitsDefault.setSuffix("Jr");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -10);
		 		
		personTraitsDefault.setBirthDate(format1.format(cal.getTime()));
		personTraitsDefault.setAssuranceLevel(2);
		personTraitsDefault.setEmail("validemail@testdomain.com");
		personTraitsDefault.setFileNumber("testValidFN");
		personTraitsDefault.setGender("M");
		personTraitsDefault.setPnid("testValidPnid");
		personTraitsDefault.setPnidType("testValidPnidType");
		personTraitsDefault.setPid("796079018");
		personTraitsDefault.setIcn("testValidIcn");
		List<String> correlationIds = new ArrayList<>();
		correlationIds.add("id1");
		correlationIds.add("id2");
		
		personTraitsDefault.setCorrelationIds(correlationIds);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		assertNotNull(personTraitsDefault.hashCode());
		personTraitsDefault.setFirstName(null);
		assertNotNull(personTraitsDefault.hashCode());
		personTraitsDefault.setLastName(null);
		assertNotNull(personTraitsDefault.hashCode());
		personTraitsDefault.setMiddleName(null);
		assertNotNull(personTraitsDefault.hashCode());
		personTraitsDefault.setPrefix(null);
		assertNotNull(personTraitsDefault.hashCode());
		personTraitsDefault.setSuffix(null);
		assertNotNull(personTraitsDefault.hashCode());
		personTraitsDefault.setBirthDate(null);
		assertNotNull(personTraitsDefault.hashCode());
		personTraitsDefault.setGender(null);
		assertNotNull(personTraitsDefault.hashCode());
		personTraitsDefault.setAssuranceLevel(0);
		assertNotNull(personTraitsDefault.hashCode());
		personTraitsDefault.setPid(null);
		assertNotNull(personTraitsDefault.hashCode());
		personTraitsDefault.setPnid(null);
		assertNotNull(personTraitsDefault.hashCode());
		personTraitsDefault.setPnidType(null);
		assertNotNull(personTraitsDefault.hashCode());
		personTraitsDefault.setCorrelationIds(null);
		assertNotNull(personTraitsDefault.hashCode());
		personTraitsDefault.setIcn(null);
		assertNotNull(personTraitsDefault.hashCode());
		personTraitsDefault.setFileNumber(null);
		assertNotNull(personTraitsDefault.hashCode());		
	}

	@Test
	public void testIsEnabled() {
		assertTrue(personTraitsDefault.isEnabled());
	}

	@Test
	public void testIsAccountNonExpired() {
		assertTrue(personTraitsDefault.isAccountNonExpired());
	}

	@Test
	public void testIsAccountNonLocked() {
		assertTrue(personTraitsDefault.isAccountNonLocked());
	}

	@Test
	public void testIsCredentialsNonExpired() {
		assertTrue(personTraitsDefault.isCredentialsNonExpired());
	}


	@Test
	public void testGetAuthorities() {
		assertNotNull(personTraitsDefault.getAuthorities());
		assertEquals(AuthorityUtils.NO_AUTHORITIES,personTraitsDefault.getAuthorities());
	}

	@Test
	public void testGetPassword() {
		assertNotNull(personTraitsDefault.getPassword());
		assertEquals("NA",personTraitsDefault.getPassword());
	}

	@Test
	public void testGetUsername() {
		assertNotNull(personTraitsDefault.getUsername());
		assertEquals("NA",personTraitsDefault.getUsername());
	}

	@Test
	public void testGetDodedipnid() {
		personTraitsDefault.setDodedipnid("TestDodedipnid");
		assertNotNull(personTraitsDefault.getDodedipnid());
		assertTrue("TestDodedipnid".equals(personTraitsDefault.getDodedipnid()));
	}

	@Test
	public void testSetDodedipnid() {
		personTraitsDefault.setDodedipnid("TestDodedipnid");
		assertNotNull(personTraitsDefault.getDodedipnid());
	}

	@Test
	public void testGetFirstName() {
		personTraitsDefault.setFirstName("TestFN");
		assertNotNull(personTraitsDefault.getFirstName());
		assertTrue("TestFN".equals(personTraitsDefault.getFirstName()));		
	}

	@Test
	public void testSetFirstName() {
		personTraitsDefault.setFirstName("TestFN");
		assertNotNull(personTraitsDefault.getFirstName());
	}

	@Test
	public void testGetLastName() {
		personTraitsDefault.setLastName("TestLN");
		assertNotNull(personTraitsDefault.getLastName());
		assertTrue("TestLN".equals(personTraitsDefault.getLastName()));
	}

	@Test
	public void testSetLastName() {
		personTraitsDefault.setLastName("TestLN");
		assertNotNull(personTraitsDefault.getLastName());

	}

	@Test
	public void testGetMiddleName() {
		personTraitsDefault.setMiddleName("TestMN");
		assertNotNull(personTraitsDefault.getMiddleName());
		assertTrue("TestMN".equals(personTraitsDefault.getMiddleName()));
	}

	@Test
	public void testSetMiddleName() {
		personTraitsDefault.setMiddleName("TestMN");
		assertNotNull(personTraitsDefault.getMiddleName());
	}

	@Test
	public void testGetPrefix() {
		personTraitsDefault.setPrefix("TestPrefix");
		assertNotNull(personTraitsDefault.getPrefix());
		assertTrue("TestPrefix".equals(personTraitsDefault.getPrefix()));
	}

	@Test
	public void testSetPrefix() {
		personTraitsDefault.setPrefix("TestPrefix");
		assertNotNull(personTraitsDefault.getPrefix());
	}

	@Test
	public void testGetSuffix() {
		personTraitsDefault.setSuffix("TestSuffix");
		assertNotNull(personTraitsDefault.getSuffix());
		assertTrue("TestSuffix".equals(personTraitsDefault.getSuffix()));
	}

	@Test
	public void testSetSuffix() {
		personTraitsDefault.setSuffix("TestSuffix");
		assertNotNull(personTraitsDefault.getSuffix());
	}

	@Test
	public void testGetBirthDate() {
		personTraitsDefault.setBirthDate(format1.format(Calendar.getInstance().getTime()));
		assertNotNull(personTraitsDefault.getBirthDate());
	}

	@Test
	public void testSetBirthDate() {
		personTraitsDefault.setBirthDate(format1.format(Calendar.getInstance().getTime()));
		assertNotNull(personTraitsDefault.getBirthDate());
	}

	@Test
	public void testGetGender() {
		personTraitsDefault.setGender("M");
		assertNotNull(personTraitsDefault.getGender());
		assertTrue("M".equals(personTraitsDefault.getGender()));
	}

	@Test
	public void testSetGender() {
		personTraitsDefault.setGender("M");
		assertNotNull(personTraitsDefault.getGender());
	}

	@Test
	public void testGetAssuranceLevel() {
		personTraitsDefault.setAssuranceLevel(3);
		assertNotNull(personTraitsDefault.getAssuranceLevel());
		assertTrue(03==(personTraitsDefault.getAssuranceLevel()));
	}

	@Test
	public void testSetAssuranceLevel() {
		personTraitsDefault.setAssuranceLevel(2);
		assertNotNull(personTraitsDefault.getAssuranceLevel());
	}

	@Test
	public void testGetEmail() {
		personTraitsDefault.setEmail("test123@maildomain.com");
		assertNotNull(personTraitsDefault.getEmail());
		assertTrue("test123@maildomain.com".equals(personTraitsDefault.getEmail()));
	}

	@Test
	public void testSetEmail() {
		personTraitsDefault.setEmail("test123@maildomain.com");
		assertNotNull(personTraitsDefault.getEmail());
	}

	@Test
	public void testGetPnidType() {
		personTraitsDefault.setPnidType("testPnidType");
		assertNotNull(personTraitsDefault.getPnidType());
		assertTrue("testPnidType".equals(personTraitsDefault.getPnidType()));
	}

	@Test
	public void testSetPnidType() {
		personTraitsDefault.setPnidType("testPnidType");
		assertNotNull(personTraitsDefault.getPnidType());
	}

	@Test
	public void testGetPnid() {
		personTraitsDefault.setPnid("testPnid");
		assertNotNull(personTraitsDefault.getPnid());
		assertTrue("testPnid".equals(personTraitsDefault.getPnid()));
	}

	@Test
	public void testSetPnid() {
		personTraitsDefault.setPnidType("testPnidType");
		assertNotNull(personTraitsDefault.getPnidType());
	}

	@Test
	public void testGetPid() {
		personTraitsDefault.setPid("testPid");
		assertNotNull(personTraitsDefault.getPid());
		assertTrue("testPid".equals(personTraitsDefault.getPid()));
	}

	@Test
	public void testSetPid() {
		personTraitsDefault.setPid("testPid");
		assertNotNull(personTraitsDefault.getPid());
	}

	@Test
	public void testGetIcn() {
		personTraitsDefault.setIcn("testIcn");
		assertNotNull(personTraitsDefault.getIcn());
		assertTrue("testIcn".equals(personTraitsDefault.getIcn()));
	}

	@Test
	public void testSetIcn() {
		personTraitsDefault.setIcn("testIcn");
		assertNotNull(personTraitsDefault.getIcn());
	}

	@Test
	public void testGetFileNumber() {
		personTraitsDefault.setFileNumber("796079018");
		assertNotNull(personTraitsDefault.getFileNumber());
		assertTrue("796079018".equals(personTraitsDefault.getFileNumber()));
	}

	@Test
	public void testSetFileNumber() {
		personTraitsDefault.setFileNumber("796079018");
		assertNotNull(personTraitsDefault.getFileNumber());
	}

	@Test
	public void testGetTokenId() {
		personTraitsDefault.setTokenId("796079018");
		assertNotNull(personTraitsDefault.getTokenId());
		assertTrue("796079018".equals(personTraitsDefault.getTokenId()));
	}

	@Test
	public void testSetTokenId() {
		personTraitsDefault.setTokenId("796079018");
		assertNotNull(personTraitsDefault.getTokenId());
	}

	@Test
	public void testGetUser() {
		assertTrue("FN LN".equals(personTraitsDefault.getUser()));
	}
	
	@Test
	public void testGetCorrelationId() {
		assertNotNull(personTraitsDefault.getCorrelationIds());
		assertTrue(personTraitsDefault.getCorrelationIds().size()>0);
	}
	
	@Test
	public void testSetCorrelationId() {
		List<String> correlationIds = new ArrayList<>();
		correlationIds.add("corr_id1");
		correlationIds.add("corr_id2");
		personTraitsDefault.setCorrelationIds(correlationIds);
		assertNotNull(personTraitsDefault.getCorrelationIds());
	}

	@Test
	public void testEqualsNullObject() {
		assertFalse(personTraitsDefault.equals(null));
	}
	@Test
	public void testEqualsNotNullObject() {
		PersonTraits o = new PersonTraits();
		o.setFirstName("FN");
		o.setLastName("ln");
		o.setMiddleName("mn");
		o.setPrefix("Mr.");
		o.setSuffix("Sr");
		o.setBirthDate(format1.format(Calendar.getInstance().getTime()));
		o.setDodedipnid("dummydodedipnid");
		o.setAssuranceLevel(3);
		o.setEmail("testemail@testdomain.com");
		o.setFileNumber("testFN");
		o.setGender("m");
		o.setPnid("testpnid");
		o.setPnidType("testPnidType");
		o.setPid("testPid");
		o.setIcn("testIcn");
		assertFalse(personTraitsDefault.equals(o));
		
		o.setDodedipnid("TestDodedipnid");
		assertFalse(personTraitsDefault.equals(o));
		o.setFirstName("FN");
		assertFalse(personTraitsDefault.equals(o));
		o.setLastName("LN");
		assertFalse(personTraitsDefault.equals(o));
		o.setMiddleName("MN");
		assertFalse(personTraitsDefault.equals(o));
		o.setPrefix("Dr.");
		assertFalse(personTraitsDefault.equals(o));
		o.setSuffix("Jr");
		assertFalse(personTraitsDefault.equals(o));
		o.setGender("M");
		assertFalse(personTraitsDefault.equals(o));
		o.setAssuranceLevel(3);
		assertFalse(personTraitsDefault.equals(o));
		o.setEmail("validemail@testdomain.com");
		assertFalse(personTraitsDefault.equals(o));
		o.setPnidType("testValidPnidType");
		assertFalse(personTraitsDefault.equals(o));
		o.setPnid("testValidPnid");
		assertFalse(personTraitsDefault.equals(o));
		o.setPid("796079018");
		assertFalse(personTraitsDefault.equals(o));
		List<String> correlationIds = new ArrayList<>();
		correlationIds.add("id1");
		correlationIds.add("id2");
		o.setCorrelationIds(correlationIds);
		assertFalse(personTraitsDefault.equals(o));	
		o.setIcn("testValidIcn");
		assertFalse(personTraitsDefault.equals(o));		  		
		o.setBirthDate(format1.format(Calendar.getInstance().getTime()));
		assertFalse(personTraitsDefault.equals(o));
	}	
	
	@Test
	public void testToString() {
		assertNotNull(personTraitsDefault.toString());
	}

}
