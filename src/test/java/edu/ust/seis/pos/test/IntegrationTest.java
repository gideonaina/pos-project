package edu.ust.seis.pos.test;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static edu.ust.seis.pos.Commons.*;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.ust.seis.pos.*;
import edu.ust.seis.pos.SecuritySubsystem.UserRole;

public class IntegrationTest {

	@BeforeClass
	public void setup() throws IOException, URISyntaxException {

		DataStore ds = new DataStore();
		ds.createSheet(Commons.bookSheet);

	}

	@Test
	public void testDataStoreWrite() throws URISyntaxException, IOException,
			RecordNotFoundException {
		Map<String, String> mp = Commons
				.createPlaceHolderMapFromHeader(Commons.bookSheet);
		mp.put(Commons.BookHead.copyCount.toString(), "500");

		String bookName = "Test Book" + Commons.randomInt();
		mp.put(Commons.BookHead.bookName.toString(), bookName);

		String primaryKey = "TRL" + Commons.randomInt();
		mp.put(Commons.BookHead.copyID.toString(), primaryKey);
		mp.put(Commons.BookHead.isbn.toString(),
				"978995562" + Commons.randomInt());

		DataStore ds = new DataStore();
		ds.dataStoreWrite(mp, Commons.bookSheet);

		String[] cy = { Commons.BookHead.bookName.toString() };

		ArrayList<String> fr = ds.dataStoreRead(Commons.bookSheet,
				Commons.BookHead.copyID.toString(), cy, primaryKey);

		for (String dd : fr) {
			assertEquals(bookName, dd);
		}
	}

	@Test
	public void testDataStoreUpdate() throws Exception {

		DataStore ds = new DataStore();

		String newBookName = "Update Test" + Commons.randomInt();
		String primaryKey = "123";
		ds.dataStoreUpdate(Commons.BookHead.bookName.toString(), newBookName,
				Commons.bookSheet, Commons.BookHead.copyID.toString(),
				primaryKey);

		String[] cy = { Commons.BookHead.bookName.toString() };
		ArrayList<String> fr = ds.dataStoreRead(Commons.bookSheet,
				Commons.BookHead.copyID.toString(), cy, primaryKey);

		for (String dd : fr) {
			assertEquals(newBookName, dd);
		}

	}

	@Test(expectedExceptions = RecordNotFoundException.class)
	public void testNegativeDataStoreUpdate() throws Exception {

		DataStore ds = new DataStore();

		String newBookName = "Update Test" + Commons.randomInt();
		String primaryKey = "notFoundKey";
		ds.dataStoreUpdate(Commons.BookHead.bookName.toString(), newBookName,
				Commons.bookSheet, Commons.BookHead.copyID.toString(),
				primaryKey);

		String[] cy = { Commons.BookHead.bookName.toString() };
		ArrayList<String> fr = ds.dataStoreRead(Commons.bookSheet,
				Commons.BookHead.copyID.toString(), cy, primaryKey);

		for (String dd : fr) {
			assertEquals(newBookName, dd);
		}

	}

	@Test
	public void testConfirmAdminUserExist() throws IOException,
			RecordNotFoundException, URISyntaxException,
			DuplicatePrimaryKeyException {

		SecuritySubsystem ssb = new SecuritySubsystem();
		DataStore ds = new DataStore();
		String[] colRead = { AUTH_ID_NUMBER, AUTH_PASSWORD, AUTH_ROLE };
		ArrayList<String> readVal = new ArrayList<String>();
		readVal = ds.dataStoreRead(AUTH_SHEETNAME, AUTH_USERNAME, colRead,
				ADMIN_USERNAME_VALUE);

		assertEquals(readVal.get(0), ADMIN_USER_ID_VALUE,
				"Wrong UserID assigned to admin");
		assertEquals(readVal.get(1), ADMIN_PASSWORD_VALUE,
				"Wrong password assigned to admin");
		assertEquals(readVal.get(2),
				SecuritySubsystem.UserRole.SUPERADMIN.toString(),
				"The role of Admin should be superAdmin");

	}

	@Test
	public void testRegisterCashierUser() throws IOException,
			RecordNotFoundException, URISyntaxException, DuplicatePrimaryKeyException {

		SecuritySubsystem sbs = new SecuritySubsystem();

		String userName = "TestName" + randomInt();
		String password = "TestPassword" + randomInt();
		UserRole role = SecuritySubsystem.UserRole.CASHIER;
		String userID = sbs.registerUser(userName, password,
				SecuritySubsystem.UserRole.CASHIER);

		String[] colRead = { AUTH_ID_NUMBER, AUTH_PASSWORD, AUTH_ROLE };
		ArrayList<String> readVal = new ArrayList<String>();
		readVal = new DataStore().dataStoreRead(AUTH_SHEETNAME, AUTH_USERNAME,
				colRead, userName);

		assertEquals(readVal.get(0), userID, "Wrong UserID parsed");
		assertEquals(readVal.get(1), password,
				"Wrong password assigned to admin");
		assertEquals(readVal.get(2), role.toString(),
				"The role of Admin should be superAdmin");

	}
	
	@Test
	public void testLogin(){
		
		
	}

}