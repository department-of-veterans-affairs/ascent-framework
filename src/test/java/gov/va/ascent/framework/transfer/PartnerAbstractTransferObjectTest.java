package gov.va.ascent.framework.transfer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PartnerAbstractTransferObjectTest {

	public class TestPartnerTransferObject extends AbstractPartnerTransferObject {
		private static final long serialVersionUID = 1L;

		public boolean test() {
			return true;
		}
	}

	private TestPartnerTransferObject testObj;

	@Before
	public void setUp() throws Exception {
		testObj = new TestPartnerTransferObject();
		assertNotNull(testObj);
	}

	@Test
	public final void testTest() {
		assertTrue(testObj.test());
	}

}
