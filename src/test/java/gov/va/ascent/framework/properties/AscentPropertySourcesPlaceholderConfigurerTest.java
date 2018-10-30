/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ascent.framework.properties;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gov.va.ascent.framework.config.BasePropertiesConfig.BasePropertiesEnvironment;

;

/**
 *
 * @author rthota
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class AscentPropertySourcesPlaceholderConfigurerTest {
	public static final String APP_NAME = "framework-test";
	private static final String DEFAULT_PROPERTIES = "classpath:/config/" + APP_NAME + ".properties";
	AscentPropertySourcesPropertyResolver ascentPropertySourcesPropertyResolver;
	AscentPropertySourcesPlaceholderConfigurer instance;

	public AscentPropertySourcesPlaceholderConfigurerTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		ascentPropertySourcesPropertyResolver = new AscentPropertySourcesPropertyResolver();
		ResourcePropertySource propertySource = null;
		try {
			propertySource = new ResourcePropertySource(
					"resource", DEFAULT_PROPERTIES);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MutablePropertySources propertySources = new MutablePropertySources();
		propertySources.addFirst(propertySource);
		ascentPropertySourcesPropertyResolver.setPropertySources(propertySources);
		instance = new AscentPropertySourcesPlaceholderConfigurer();
		MockEnvironment env = new MockEnvironment();
		env.setProperty("key", "value");
		env.setActiveProfiles("local-int");
		instance.setEnvironment(env);
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of getActiveProfiles method, of class AscentPropertySourcesPlaceholderConfigurer.
	 */
	@Test
	public void testGetActiveProfiles() {
		String[] expResult = { "local-int" };
		String[] result = instance.getActiveProfiles();
		assertArrayEquals(expResult, result);
	}

	/**
	 * Test of getDefaultProfiles method, of class AscentPropertySourcesPlaceholderConfigurer.
	 */
	@Test
	public void testGetDefaultProfiles() {
		String[] expResult = { "default" };
		;
		String[] result = instance.getDefaultProfiles();
		assertArrayEquals(expResult, result);
	}

	/**
	 * Test of getPropertyInfo method, of class AscentPropertySourcesPlaceholderConfigurer.
	 */
	@Test
	public void testGetPropertyInfo() {
		AscentPropertySourcesPlaceholderConfigurer instance = new AscentPropertySourcesPlaceholderConfigurer();
		List<PropertyFileHolder> result = instance.getPropertyInfo();
		assertTrue(result.size() == 0);
	}

	/**
	 * Test of getPropertySourceResolverWss method, of class AscentPropertySourcesPlaceholderConfigurer.
	 */
	@Test
	public void testGetPropertySourceResolverWss() {
		AscentPropertySourcesPropertyResolver result = instance.getPropertySourceResolverWss();
		assertNotNull(result);
	}

	/**
	 * Test of getSystemProperties method, of class AscentPropertySourcesPlaceholderConfigurer.
	 */
	@Test
	public void testGetSystemProperties() {
		Map<String, Object> result = instance.getSystemProperties();
		assertNotNull(result);
	}

	@Test
	public void testProcessProperties() {
		try {
			DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
			AscentPropertySourcesPropertyResolver propertyResolver = instance.getPropertySourceResolverWss();
			instance.processProperties(beanFactory, propertyResolver);
		} catch (Exception e) {
			fail("Should not have thrown exception.");
		}
	}

	/**
	 * Test of setEnvironment method, of class AscentPropertySourcesPlaceholderConfigurer.
	 */
	@Test
	public void testSetEnvironment() {
		Environment environment = null;
		instance.setEnvironment(environment);
	}

	@Configuration
	@PropertySource(DEFAULT_PROPERTIES)
	static class DefaultEnvironment extends BasePropertiesEnvironment {

	}
}
