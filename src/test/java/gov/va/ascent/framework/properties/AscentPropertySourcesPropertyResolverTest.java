/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ascent.framework.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gov.va.ascent.framework.config.BasePropertiesConfig.BasePropertiesEnvironment;

/**
 *
 * @author rthota
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class AscentPropertySourcesPropertyResolverTest {
	public static final String APP_NAME = "framework-test";

	private static final String DEFAULT_PROPERTIES = "classpath:/config/" + APP_NAME + ".properties";
	AscentPropertySourcesPropertyResolver instance;
    public AscentPropertySourcesPropertyResolverTest() {
    }
    
/*	@Autowired
	private PropertySources propertySources;*/
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new AscentPropertySourcesPropertyResolver();
        ResourcePropertySource propertySource1 = null;
		try {
			propertySource1 = new ResourcePropertySource(
			        "resource", DEFAULT_PROPERTIES);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addFirst(propertySource1);
        instance.setPropertySources(propertySources);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of containsProperty method, of class AscentPropertySourcesPropertyResolver.
     * @throws IOException 
     */
    @Test
    public void testContainsProperty() throws IOException {
        System.out.println("containsProperty");
        String key = "wss-partner-person.ws.client.endpoint";
        boolean expResult = true;
        boolean result = instance.containsProperty(key);
        assertEquals(expResult, result);   
    }

    /**
     * Test of containsProperty method, of class AscentPropertySourcesPropertyResolver.
     * @throws IOException 
     */
    @Test
    public void testContainsProperty_null() throws IOException {
        System.out.println("containsProperty");
        String key = "wss-partner-person.ws.client.endpoints";
        boolean expResult = false;
        boolean result = instance.containsProperty(key);
        assertEquals(expResult, result);   
    }
    /**
     * Test of getFromFileProperty method, of class AscentPropertySourcesPropertyResolver.
     * @throws IOException 
     */
    @Test
    public void testGetFromFileProperty() throws IOException {
        System.out.println("getFromFileProperty");
        String key = "wss-partner-person.ws.client.endpoint";
        String expResult = "http://localhost:9736/testEndPoint";
        String result = instance.getFromFileProperty(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of getProperty method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testGetProperty_String() {
        System.out.println("getProperty");
        String key = "wss-partner-person.ws.client.endpoint";
        String expResult = "http://localhost:9736/testEndPoint";
        String result = instance.getProperty(key);
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of getProperty method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testGetProperty_String_Class() {
        System.out.println("getProperty");
        String key = "wss-partner-person.ws.client.endpoint";
        String expResult = "http://localhost:9736/testEndPoint";
        Object result = instance.getProperty(key);
        assertEquals(expResult, result);    
    }

    /**
     * Test of getProperty method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testGetProperty_String_String() {
        System.out.println("getProperty");
        String key = "wss-partner-person.ws.client.endpoint";
        String defaultValue = "test";
        String expResult = "http://localhost:9736/testEndPoint";
        String result = instance.getProperty(key, defaultValue);
        assertEquals(expResult, result);
        result = instance.getProperty(key, defaultValue);
        assertEquals(expResult, result);
    }

    /**
     * Test of getProperty method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testGetProperty_String_Null() {
        System.out.println("getProperty");
        String key = "wss-partner-person.ws.client.endpointa:test";
        String defaultValue = null;
        String expResult = null;
        String result = instance.getProperty(key, defaultValue);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPropertyFileHolders method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testGetPropertyFileHolders() {
        System.out.println("getPropertyFileHolders");
        List<PropertyFileHolder> expResult = new ArrayList<>();
        List<PropertyFileHolder> result = instance.getPropertyFileHolders();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPropertySources method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testSetPropertySources() {
        System.out.println("setPropertySources");
        PropertySources propertySources = null;
        instance.setPropertySources(propertySources);
    }

    /**
     * Test of setPropertySourcesPropertyResolver method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testSetPropertySourcesPropertyResolver() {
        System.out.println("setPropertySourcesPropertyResolver");
        PropertyResolver propertySourcesPropertyResolver = null;
        AscentPropertySourcesPropertyResolver instance = new AscentPropertySourcesPropertyResolver();
        instance.setPropertySourcesPropertyResolver(propertySourcesPropertyResolver);
    }

    /**
     * Test of setValueSeparator method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testSetValueSeparator() {
        System.out.println("setValueSeparator");
        String valueSeparator = "";
        AscentPropertySourcesPropertyResolver instance = new AscentPropertySourcesPropertyResolver();
        instance.setValueSeparator(valueSeparator);
        
        
    }
    	
	@Configuration
	@PropertySource(DEFAULT_PROPERTIES)
	static class DefaultEnvironment extends BasePropertiesEnvironment {
		
	}
	
}
