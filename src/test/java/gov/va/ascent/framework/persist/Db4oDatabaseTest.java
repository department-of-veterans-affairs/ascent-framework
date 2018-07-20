/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ascent.framework.persist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

	private final Logger db4oLogger = super.getLogger(Db4oDatabase.class);

	Db4oDatabase instance;

	@Before
	public void setUp() {
		instance = new Db4oDatabase();
		instance.postConstruct();
		final Message message = new Message();
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
		final File dir = new File("./");
		for (final File file : dir.listFiles()) {
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
		final boolean expResult = false;
		final boolean result = instance.isDataInitPermittedForThisInstance();
		assertEquals(expResult, result);
	}

	/**
	 * Test of isDataInitPermittedForThisInstance method, of class Db4oDatabase.
	 */
	@Test
	public void testIsDataInitPermittedForThisInstanceTrue() {
		final boolean expResult = true;
		instance.setClientServerMode(false);
		final boolean result = instance.isDataInitPermittedForThisInstance();
		assertEquals(expResult, result);
	}

	/**
	 * Test of save method, of class Db4oDatabase.
	 */
	@Test
	public void testSave() {
		final Message message = new Message();
		instance.save(message);
	}

	/**
	 * Test of delete method, of class Db4oDatabase.
	 */
	@Test
	public void testDelete() {
		final Message message1 = new Message();
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
		final List<Object> result = instance.queryByExample(Message.class);
		final Message mess = (Message) result.get(0);
		assertTrue("key".equals(mess.getKey()));
	}

	/**
	 * Test of queryForUnique method, of class Db4oDatabase.
	 */
	@Test
	public void testQueryForUnique() {
		final Object objectExample = null;
		final Object result = instance.queryForUnique(objectExample);
		final Message mess = (Message) result;
		assertTrue("key".equals(mess.getKey()));
	}

	/**
	 * Test of preDestroy method, of class Db4oDatabase.
	 */
	@Test
	public void testPreDestroy() {
		instance.preDestroy();
		// openServerForClientServerMode
		// startInClientServerMode
//		server = Db4oClientServer.openServer(serverConfig, db4oFile, port);

	}

	/**
	 * Test of preDestroy method, of class Db4oDatabase.
	 */
	@Test
	public void testPreDestroyClient() {
		super.getAppender().clear();
		db4oLogger.setLevel(Level.INFO);

		instance.preDestroy();
		super.getAppender().clear();
		final Db4oDatabase db = new Db4oDatabase();
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
	public void testPreDestroyClientDisabled() {
		super.getAppender().clear();
		db4oLogger.setLevel(Level.INFO);

		instance.preDestroy();
		super.getAppender().clear();
		final Db4oDatabase db = new Db4oDatabase();
		db.setClientServerMode(false);
		db.setDb4oFile("singleClientModeTest");
		db.setEnabled(false);
		db.postConstruct();
		assertEquals("Db4oDatabase not enabled, not going to start the database", super.getAppender().get(0).getMessage());
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
		final Db4oDatabase db = new Db4oDatabase();
		db.setClientServerMode(true);
		db.setDb4oFile("singleClientModeTest");
		db.postConstruct();
		assertTrue(super.getAppender().get(0).getMessage().contains("Db4oDatabase enabled & initializing"));
		assertEquals("database client instantiated.", super.getAppender().get(1).getMessage());
		instance.preDestroy();
		System.out.println("<<<<<<<<<<");
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
		final Db4oDatabase db = new Db4oDatabase();
		db.setClientServerMode(true);
		db.setDb4oFile("singleClientModeTest");
		db.setEnabled(false);
		db.postConstruct();
		assertEquals("Db4oDatabase not enabled, not going to start the database", super.getAppender().get(0).getMessage());
		instance.preDestroy();
	}

	/**
	 * Test of toString method, of class Db4oDatabase.
	 */
	@Test
	public void testToString() {
		final String result = instance.toString();
		assertTrue(result.contains("localhost"));
	}

	/**
	 * Test of setUpdateDepth method, of class Db4oDatabase.
	 */
	@Test
	public void testSetUpdateDepth() {
		final Integer updateDepth = null;
		instance.setUpdateDepth(updateDepth);
	}

	/**
	 * Test of setPw method, of class Db4oDatabase.
	 */
	@Test
	public void testSetPw() {
		final String password = "";
		final Db4oDatabase instance = new Db4oDatabase();
		instance.setPw(password);
	}

	/**
	 * Test of setUser method, of class Db4oDatabase.
	 */
	@Test
	public void testSetUser() {
		final String user = "";
		final Db4oDatabase instance = new Db4oDatabase();
		instance.setUser(user);
	}

	/**
	 * Test of setHost method, of class Db4oDatabase.
	 */
	@Test
	public void testSetHost() {
		final String host = "";
		final Db4oDatabase instance = new Db4oDatabase();
		instance.setHost(host);
	}

	/**
	 * Test of setActivationDepth method, of class Db4oDatabase.
	 */
	@Test
	public void testSetActivationDepth() {
		final Integer activationDepth = null;
		final Db4oDatabase instance = new Db4oDatabase();
		instance.setActivationDepth(activationDepth);
	}

	/**
	 * Test of setDb4oFile method, of class Db4oDatabase.
	 */
	@Test
	public void testSetDb4oFile() {
		final String db4oFile = "";
		final Db4oDatabase instance = new Db4oDatabase();
		instance.setDb4oFile(db4oFile);
	}

	/**
	 * Test of setClientServerMode method, of class Db4oDatabase.
	 */
	@Test
	public void testSetClientServerMode() {
		final boolean clientServerMode = false;
		final Db4oDatabase instance = new Db4oDatabase();
		instance.setClientServerMode(clientServerMode);
	}

	/**
	 * Test of setPort method, of class Db4oDatabase.
	 */
	@Test
	public void testSetPort() {
		final int port = 0;
		final Db4oDatabase instance = new Db4oDatabase();
		instance.setPort(port);
	}

	/**
	 * Test of setEnabled method, of class Db4oDatabase.
	 */
	@Test
	public void testSetEnabled() {
		final boolean enabled = false;
		final Db4oDatabase instance = new Db4oDatabase();
		instance.setEnabled(enabled);
	}

	/**
	 * Test of setCleanData method, of class Db4oDatabase.
	 */
	@Test
	public void testSetCleanData() {
		final boolean cleanData = false;
		final Db4oDatabase instance = new Db4oDatabase();
		instance.setCleanData(cleanData);
	}

	@Test
	public void testGetObjectsOfType() {
		final Object[] messages = instance.getObjectsOfType(Message.class);
		assertNotNull(messages);
		assertTrue(messages.length > 0);
	}

}
