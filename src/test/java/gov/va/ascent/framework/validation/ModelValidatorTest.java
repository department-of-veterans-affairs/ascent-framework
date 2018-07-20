package gov.va.ascent.framework.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;

import org.junit.Test;
import org.mockito.Mockito;

import gov.va.ascent.framework.service.ServiceRequest;
import gov.va.ascent.framework.validation.ModelValidator.Modes;

public class ModelValidatorTest {

	@Test
	public void testreadResolve() {
		final ModelValidator modelValidator = new ModelValidator();
		assertNotNull(modelValidator.readResolve());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testValidateModelPropertiesWithMock() {

		final gov.va.ascent.framework.validation.ModelValidator modelValidator =
				mock(gov.va.ascent.framework.validation.ModelValidator.class);
		final TestRequest tr = new TestRequest();
		final Class<?>[] classes = new Class[1];
		classes[0] = Default.class;
		final Object o = Mockito.mock(ConstraintViolation.class);
		final Set<ConstraintViolation<TestRequest>> hashSet = new HashSet<ConstraintViolation<TestRequest>>();
		hashSet.add((ConstraintViolation<TestRequest>) o);
		when(modelValidator.doValidateProperty(tr, "TestKey", classes)).thenReturn(hashSet);

		final Map<String, List<ViolationMessageParts>> messages = new LinkedHashMap<>();

		final ViolationMessageParts violationMessageParts = new ViolationMessageParts();
		violationMessageParts.setOriginalKey("UnitTestKey");
		violationMessageParts.setNewKey("UpdatedUnitTestKey");
		final List<ViolationMessageParts> msgList = new ArrayList<>();
		msgList.add(violationMessageParts);
		messages.put("TestKey", msgList);
		when(modelValidator.validateModelProperties(tr, messages, classes)).thenCallRealMethod();

		assertFalse(modelValidator.validateModelProperties(tr, messages, classes));
	}

	@Test
	public void testValidateModelForEmptyMessages() {
		final ModelValidator modelTest = new ModelValidator();
		final TestRequest tr = new TestRequest();
		final Map<String, List<ViolationMessageParts>> messages = new LinkedHashMap<>();
		assertTrue(modelTest.validateModel(tr, messages, (Class[]) null));
	}

	@Test
	public void testValidateModelForNonEmptyMessages() {
		final ModelValidator modelTest = new ModelValidator();
		final TestRequest tr = new TestRequest();
		final Class<?>[] classes = new Class[1];
		classes[0] = Default.class;

		final ViolationMessageParts violationMessageParts = new ViolationMessageParts();
		violationMessageParts.setOriginalKey("UnitTestKey");
		violationMessageParts.setNewKey("UpdatedUnitTestKey");

		final List<ViolationMessageParts> msgList = new ArrayList<>();
		msgList.add(violationMessageParts);

		final Map<String, List<ViolationMessageParts>> messages = new LinkedHashMap<>();
		messages.put("TestKey", msgList);
		assertTrue(modelTest.validateModel(tr, messages, classes));
	}

	@Test
	public void testValidateModelPropertiesForEmptyMessages() {
		final ModelValidator modelTest = new ModelValidator();
		final TestRequest tr = new TestRequest();
		final Map<String, List<ViolationMessageParts>> messages = new LinkedHashMap<>();
		assertTrue(modelTest.validateModelProperties(tr, messages, (Class[]) null));
	}

	@Test
	public void testValidateModelPropertiesForNonEmptyMessages() {
		final ModelValidator modelTest = new ModelValidator();
		final TestRequest tr = new TestRequest();
		final Map<String, List<ViolationMessageParts>> messages = new LinkedHashMap<>();

		final ViolationMessageParts violationMessageParts = new ViolationMessageParts();
		violationMessageParts.setOriginalKey("UnitTestKey");
		violationMessageParts.setNewKey("UpdatedUnitTestKey");
		final List<ViolationMessageParts> msgList = new ArrayList<>();
		msgList.add(violationMessageParts);
		messages.put("TestKey", msgList);
		final Class<?>[] classes = new Class[1];
		classes[0] = Default.class;

		assertTrue(modelTest.validateModelProperties(tr, messages, classes));
	}

	@Test
	public void testConvertKeyToNodepathStyle() {
		String retVal = ModelValidator.convertKeyToNodepathStyle("TestKey", "message value1.message value2");
		assertTrue(retVal.contains("."));
		assertTrue("TestKey.message value1".equals(retVal));

		retVal = ModelValidator.convertKeyToNodepathStyle("TestKey", "value1");
		assertTrue("value1".equals(retVal));

		retVal = ModelValidator.convertKeyToNodepathStyle("TestKey", null);
		assertNull(retVal);

		retVal = ModelValidator.convertKeyToNodepathStyle(null, "message value1.message value2");
		assertNotNull(retVal);
		assertTrue("message value1".equals(retVal));
	}

	@Test
	public void testDoValidate() {
		final ModelValidator modelTest = new ModelValidator();
		final TestRequest tr = new TestRequest();
		assertNotNull(modelTest.doValidate(tr, (Class[]) null));
	}

	@Test
	public void testDoValidateProperty() {
		final ModelValidator modelTest = new ModelValidator();
		final TestRequest tr = new TestRequest();
		final Class<?>[] classes = new Class[1];
		classes[0] = Default.class;
		assertNotNull(modelTest.doValidateProperty(tr, "TestKey", classes));
	}

	@Test
	public void testModes() {
		assertNotNull(Modes.MODEL);
		assertNotNull(Modes.PROPERTIES);
	}

}

class TestRequest extends ServiceRequest {

	/**
	 *
	 */
	private static final long serialVersionUID = 469530062192402379L;

}