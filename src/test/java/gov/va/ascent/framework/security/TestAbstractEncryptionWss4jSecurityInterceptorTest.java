package gov.va.ascent.framework.security;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.StringUtils;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.MerlinDevice;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractEncryptionWss4jSecurityInterceptorTest {

	class Tester extends AbstractEncryptionWss4jSecurityInterceptor {
		public Tester() {
			super();
		}
	}

	private Tester tester;

	@Before
	public void setUp() throws Exception {
		tester = new Tester();
	}

	@Test
	public final void testAfterPropertiesSet() {
		try {
			tester.afterPropertiesSet();
			assertTrue(true);
		} catch (final Exception e) {
			e.printStackTrace();
			fail("Should not have thrown exception");
		}
	}

	@Test
	public final void testSettersAndGetters() {
		final Crypto crypto = new MerlinDevice();

		tester.setCrypto(crypto);
		tester.setKeyAlias("test.alias");
		tester.setKeyPassword("test.password");
		tester.setCryptoFile("test.file");

		assertNotNull(tester.getCrypto());
		assertTrue(StringUtils.isNotBlank(tester.getKeyAlias()));
		assertTrue(StringUtils.isNotBlank(tester.getKeyPassword()));
		assertTrue(StringUtils.isNotBlank(tester.getCryptoFile()));

	}

}
