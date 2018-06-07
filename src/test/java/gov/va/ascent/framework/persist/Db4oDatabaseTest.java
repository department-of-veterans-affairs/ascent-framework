/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ascent.framework.persist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import gov.va.ascent.framework.AbstractBaseLogTester;
import gov.va.ascent.framework.messages.Message;

/**
 *
 * @author rthota
 */
@RunWith(SpringRunner.class)
public class Db4oDatabaseTest extends AbstractBaseLogTester {

	private Logger db4oLogger = super.getLogger(Db4oDatabase.class);

	Db4oDatabase instance;

	@Before
	public void setUp() {
		instance = new Db4oDatabase();
		instance.postConstruct();
		Message message = new Message();
		message.setKey("key");
		message.setText("sample text");
		instance.save(message);
	}

	@Override
	@After
	public void tearDown() {

	}

	@AfterClass
	public static void clean() throws Exception {
		File dir = new File("./");
		for (File file : dir.listFiles()) {
			if (file.getName().equals("datafile.db4o") || file.getName().equals("singleClientModeTest")) {
				file.delete();
			}
		}

	}

	/**
	 * Test of isDataInitPermittedForThisInstance method, of class Db4oDatabase.
	 */
	@Test
	public void testIsDataInitPermittedForThisInstance() {
		boolean expResult = false;
		boolean result = instance.isDataInitPermittedForThisInstance();
		assertEquals(expResult, result);
	}

	/**
	 * Test of isDataInitPermittedForThisInstance method, of class Db4oDatabase.
	 */
	@Test
	public void testIsDataInitPermittedForThisInstanceTrue() {
		boolean expResult = true;
		instance.setClientServerMode(false);
		boolean result = instance.isDataInitPermittedForThisInstance();
		assertEquals(expResult, result);
	}

	/**
	 * Test of save method, of class Db4oDatabase.
	 */
	@Test
	public void testSave() {
		Message message = new Message();
		instance.save(message);
	}

	/**
	 * Test of delete method, of class Db4oDatabase.
	 */
	@Test
	public void testDelete() {
		Message message1 = new Message();
		instance.save(message1);
		instance.delete(message1);
	}

	/**
	 * Test of deleteAll method, of class Db4oDatabase.
	 */
	@Test
	public void testDeleteAll() {
		instance.deleteAll();
	}

	/**
	 * Test of queryByExample method, of class Db4oDatabase.
	 */
	@Test
	public void testQueryByExample() {
		List<Object> result = instance.queryByExample(Message.class);
		Message mess = (Message) result.get(0);
		assertTrue("key".equals(mess.getKey()));
	}

	/**
	 * Test of queryForUnique method, of class Db4oDatabase.
	 */
	@Test
	public void testQueryForUnique() {
		Object objectExample = null;
		Object result = instance.queryForUnique(objectExample);
		Message mess = (Message) result;
		assertTrue("key".equals(mess.getKey()));
	}

	/**
	 * Test of preDestroy method, of class Db4oDatabase.
	 */
	@Test
	public void testPreDestroy() {
		instance.preDestroy();
	}

	/**
	 * Test of preDestroy method, of class Db4oDatabase.
	 */
	@Test
	public void testPreDestroyServer() {
		super.getAppender().clear();
		db4oLogger.setLevel(Level.INFO);

		instance.preDestroy();
		super.getAppender().clear();
		Db4oDatabase db = new Db4oDatabase();
		db.setClientServerMode(false);
		db.setDb4oFile("singleClientModeTest");
		db.postConstruct();
		assertEquals("cleanData==true and db isn't started, deleting old database prior to starting.",
				super.getAppender().get(1).getMessage());
		assertEquals("database client instantiated.", super.getAppender().get(3).getMessage());
	}

	/**
	 * Test of preDestroy method, of class Db4oDatabase.
	 */
	@Test
	public void testPreDestroyServerDisabled() {
		super.getAppender().clear();
		db4oLogger.setLevel(Level.INFO);


		instance.preDestroy();
		super.getAppender().clear();
		Db4oDatabase db = new Db4oDatabase();
		db.setClientServerMode(false);
		db.setDb4oFile("singleClientModeTest");
		db.setEnabled(false);
		db.postConstruct();
		assertEquals("Db4oDatabase not enabled, not going to start the database", super.getAppender().get(0).getMessage());
	}

	/**
	 * Test of toString method, of class Db4oDatabase.
	 */
	@Test
	public void testToString() {
		String result = instance.toString();
		assertTrue(result.contains("localhost"));
	}

	/**
	 * Test of setUpdateDepth method, of class Db4oDatabase.
	 */
	@Test
	public void testSetUpdateDepth() {
		Integer updateDepth = null;
		instance.setUpdateDepth(updateDepth);
	}

	/**
	 * Test of setPw method, of class Db4oDatabase.
	 */
	@Test
	public void testSetPw() {
		String password = "";
		Db4oDatabase instance = new Db4oDatabase();
		instance.setPw(password);
	}

	/**
	 * Test of setUser method, of class Db4oDatabase.
	 */
	@Test
	public void testSetUser() {
		String user = "";
		Db4oDatabase instance = new Db4oDatabase();
		instance.setUser(user);
	}

	/**
	 * Test of setHost method, of class Db4oDatabase.
	 */
	@Test
	public void testSetHost() {
		String host = "";
		Db4oDatabase instance = new Db4oDatabase();
		instance.setHost(host);
	}

	/**
	 * Test of setActivationDepth method, of class Db4oDatabase.
	 */
	@Test
	public void testSetActivationDepth() {
		Integer activationDepth = null;
		Db4oDatabase instance = new Db4oDatabase();
		instance.setActivationDepth(activationDepth);
	}

	/**
	 * Test of setDb4oFile method, of class Db4oDatabase.
	 */
	@Test
	public void testSetDb4oFile() {
		String db4oFile = "";
		Db4oDatabase instance = new Db4oDatabase();
		instance.setDb4oFile(db4oFile);
	}

	/**
	 * Test of setClientServerMode method, of class Db4oDatabase.
	 */
	@Test
	public void testSetClientServerMode() {
		boolean clientServerMode = false;
		Db4oDatabase instance = new Db4oDatabase();
		instance.setClientServerMode(clientServerMode);
	}

	/**
	 * Test of setPort method, of class Db4oDatabase.
	 */
	@Test
	public void testSetPort() {
		int port = 0;
		Db4oDatabase instance = new Db4oDatabase();
		instance.setPort(port);
	}

	/**
	 * Test of setEnabled method, of class Db4oDatabase.
	 */
	@Test
	public void testSetEnabled() {
		boolean enabled = false;
		Db4oDatabase instance = new Db4oDatabase();
		instance.setEnabled(enabled);
	}

	/**
	 * Test of setCleanData method, of class Db4oDatabase.
	 */
	@Test
	public void testSetCleanData() {
		boolean cleanData = false;
		Db4oDatabase instance = new Db4oDatabase();
		instance.setCleanData(cleanData);
	}

}
