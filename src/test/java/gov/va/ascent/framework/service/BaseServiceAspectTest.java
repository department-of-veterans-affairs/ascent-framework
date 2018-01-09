package gov.va.ascent.framework.service;

import org.junit.Test;

public class BaseServiceAspectTest {

    @Test
    public void testStandardServiceMethod(){
        BaseServiceAspect.publicStandardServiceMethod();
        //does nothing
    }
    
    @Test
    public void testRestControllereMethod(){
        BaseServiceAspect.restController();;
        //does nothing
    }

}
