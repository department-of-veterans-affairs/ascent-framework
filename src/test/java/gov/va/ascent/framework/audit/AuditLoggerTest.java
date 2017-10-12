/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ascent.framework.audit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;

/**
 *
 * @author rthota
 */
public class AuditLoggerTest {
    
    public AuditLoggerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
/*        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);*/
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of debug method, of class AuditLogger.
     */
    @Test
    public void testDebug() {
        System.out.println("debug");
        Auditable auditable = new testAuditable();
        String activityDetail = "test";
        AuditLogger.debug(auditable, activityDetail);
    }

    /**
     * Test of info method, of class AuditLogger.
     */
    @Test
    public void testInfo() {
        System.out.println("info");
        Auditable auditable = new testAuditable();
        String activityDetail = "test";
        AuditLogger.info(auditable, activityDetail);
    }

    /**
     * Test of warn method, of class AuditLogger.
     */
    @Test
    public void testWarn() {
        System.out.println("warn");
        Auditable auditable = new testAuditable();
        String activityDetail = "test";
        AuditLogger.warn(auditable, activityDetail);
    }

    /**
     * Test of error method, of class AuditLogger.
     */
    @Test
    public void testError() {
        System.out.println("error");
        Auditable auditable = new testAuditable();
        String activityDetail = "test";
        AuditLogger.error(auditable, activityDetail);
    }
    
    
    class testAuditable implements Auditable {

        @Override
        public Class<? extends Annotation> annotationType() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public AuditEvents event() {
            // TODO Auto-generated method stub
            return AuditEvents.REQUEST_RESPONSE;
        }

        @Override
        public String activity() {
            // TODO Auto-generated method stub
            return "test";
        }

		@Override
		public String auditClass() {
			// TODO Auto-generated method stub
			return null;
		}

    }
}
