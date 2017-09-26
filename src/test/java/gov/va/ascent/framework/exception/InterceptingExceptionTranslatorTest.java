package gov.va.ascent.framework.exception;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InterceptingExceptionTranslatorTest {

    private String targetObject;

    @Rule
    public ExpectedException exceptions = ExpectedException.none();

    @Before
    public void setup() {

        this.targetObject = "Not A Number";


    }

    @Test
    public void testAscentRunTimeExceptionDefault() throws Exception {
        InterceptingExceptionTranslator interceptingExceptionTranslator = new InterceptingExceptionTranslator();
        interceptingExceptionTranslator.setDefaultExceptionType(AscentRuntimeException.class);

        Throwable throwable = new Throwable("Cause Unit Test");

        exceptions.expect(AscentRuntimeException.class);
        exceptions.expectMessage("Unit Test");
        exceptions.expectCause(Matchers.<Throwable>equalTo(throwable));
        interceptingExceptionTranslator.afterThrowing(this.getClass().getMethod("test"),
                null, targetObject,throwable);

    }

    @Test
    public void testExcludeAscentRuntimeException() throws Exception {
        InterceptingExceptionTranslator interceptingExceptionTranslator = new InterceptingExceptionTranslator();
        interceptingExceptionTranslator.setDefaultExceptionType(RuntimeException.class);
        Map<String, Class<? extends RuntimeException>> exceptionMap = new HashMap<>();
        exceptionMap.put("AscentRuntimeException", AscentRuntimeException.class);

        interceptingExceptionTranslator.setExceptionMap(exceptionMap);

        Set<String> exclusion = new HashSet<>();
        exclusion.add("gov.va.ascent.framework.exception.AscentRuntimeException");


        Throwable throwable = new Throwable("Cause Unit Test");

        exceptions.expect(AscentRuntimeException.class);
        exceptions.expectMessage("Unit Test");
        exceptions.expectCause(Matchers.<Throwable>equalTo(throwable));
        interceptingExceptionTranslator.afterThrowing(this.getClass().getMethod("test"),
                null, targetObject,throwable);

    }
}
