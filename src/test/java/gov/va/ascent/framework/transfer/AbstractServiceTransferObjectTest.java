package gov.va.ascent.framework.transfer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AbstractServiceTransferObjectTest {

	public class TestServiceTransferObject extends AbstractServiceTransferObject {
		private static final long serialVersionUID = 1L;

		public boolean test() {
			return true;
		}
	}

	private TestServiceTransferObject testObj;

	@Before
	public void setUp() throws Exception {
		testObj = new TestServiceTransferObject();
		assertNotNull(testObj);
	}

	@Test
	public final void testTest() {
		assertTrue(testObj.test());
	}

}
