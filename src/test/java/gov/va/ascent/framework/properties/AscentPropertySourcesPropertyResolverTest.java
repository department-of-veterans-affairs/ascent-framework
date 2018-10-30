/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ascent.framework.properties;

import gov.va.ascent.framework.config.BasePropertiesConfig.BasePropertiesEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
    
    
    @Before
    public void setUp() {
        instance = new AscentPropertySourcesPropertyResolver();
        ResourcePropertySource propertySource1 = null;
		try {
			propertySource1 = new ResourcePropertySource(
			        "resource", DEFAULT_PROPERTIES);
		} catch (IOException e) {
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
        String key = "wss-partner-person.ws.client.endpoints";
        boolean expResult = false;
        boolean result = instance.containsProperty(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of containsProperty method, of class AscentPropertySourcesPropertyResolver.
     * @throws IOException
     */
    @Test
    public void testContainsPropertyeEsolverNull() throws IOException {
        String key = "wss-partner-person.ws.client.endpoints";
        boolean expResult = false;
        instance.setPropertySourcesPropertyResolver(null);
        boolean result = instance.containsProperty(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of getFromFileProperty method, of class AscentPropertySourcesPropertyResolver.
     * @throws IOException 
     */
    @Test
    public void testGetFromFileProperty() throws IOException {
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
        String key = "wss-partner-person.ws.client.endpoint";
        String expResult = "http://localhost:9736/testEndPoint";
        Object result = instance.getProperty(key);
        assertEquals(expResult, result);    
    }

    /**
     * Test of getProperty method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testGetProperty_String_Class_ResolverNull() {
        String key = "wss-partner-person.ws.client.endpoint";
        String expResult = null;
        instance.setPropertySourcesPropertyResolver(null);
        Object result = instance.getProperty(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of getProperty method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testGetProperty_String_String() {
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
        String key = "wss-partner-person.ws.client.endpointa:test";
        String defaultValue = null;
        String expResult = null;
        String result = instance.getProperty(key, defaultValue);
        assertEquals(expResult, result);
    }

    /**
     * Test of getProperty method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testGetProperty_String_Null_ResolverNull() {
        String key = "wss-partner-person.ws.client.endpointa:test";
        String defaultValue = null;
        String expResult = null;
        instance.setPropertySourcesPropertyResolver(null);
        String result = instance.getProperty(key, defaultValue);
        assertEquals(expResult, result);
    }

    /**
     * Test of getProperty method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testGetProperty_targeValueType() {
        String key = "wss-partner-person.ws.client.endpointa:test";
        String expResult = null;
        String result = instance.getProperty(key, String.class);
        assertEquals(expResult, result);
    }

    /**
     * Test of getProperty method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testGetProperty_targeValueTypeResolverNull() {
        String key = "wss-partner-person.ws.client.endpointa:test";
        String expResult = null;
        instance.setPropertySourcesPropertyResolver(null);
        String result = instance.getProperty(key, String.class);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPropertyFileHolders method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testGetPropertyFileHolders() {
        List<PropertyFileHolder> expResult = new ArrayList<>();
        List<PropertyFileHolder> result = instance.getPropertyFileHolders();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPropertySources method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testSetPropertySources() {
        PropertySources propertySources = null;
        instance.setPropertySources(propertySources);
    }

    /**
     * Test of setPropertySourcesPropertyResolver method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testSetPropertySourcesPropertyResolver() {
        PropertyResolver propertySourcesPropertyResolver = null;
        AscentPropertySourcesPropertyResolver instance = new AscentPropertySourcesPropertyResolver();
        instance.setPropertySourcesPropertyResolver(propertySourcesPropertyResolver);
    }

    /**
     * Test of setValueSeparator method, of class AscentPropertySourcesPropertyResolver.
     */
    @Test
    public void testSetValueSeparator() {
        String valueSeparator = "";
        AscentPropertySourcesPropertyResolver instance = new AscentPropertySourcesPropertyResolver();
        instance.setValueSeparator(valueSeparator);
        
        
    }
    	
	@Configuration
	@PropertySource(DEFAULT_PROPERTIES)
	static class DefaultEnvironment extends BasePropertiesEnvironment {
		
	}
	
}
