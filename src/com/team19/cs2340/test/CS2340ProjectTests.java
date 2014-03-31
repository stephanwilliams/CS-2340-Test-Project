package com.team19.cs2340.test;

import java.math.BigDecimal;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.team19.cs2340.DatabaseHelper;
import com.team19.cs2340.finance.FinanceDataServiceFactory;
import com.team19.cs2340.finance.IAccount;
import com.team19.cs2340.finance.IFinanceDataService;
import com.team19.cs2340.user.IUser;
import com.team19.cs2340.user.IUserAccountService;
import com.team19.cs2340.user.UserAccountServiceFactory;

public class CS2340ProjectTests extends AndroidTestCase {

	private static final String TEST_USERNAME = "TEST_USER";
	private static final String TEST_PASSWORD = "TEST_PASSWORD";
	
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase database;
	private IUserAccountService userAccountService;
	private IFinanceDataService financeDataService;
	private IUser user;
	private IAccount account;

	protected void setUp() throws Exception {
		super.setUp();
		
		databaseHelper = new DatabaseHelper(getContext());
		database = databaseHelper.getWritableDatabase();
		
		userAccountService = UserAccountServiceFactory.createUserAccountService(getContext());
		financeDataService = FinanceDataServiceFactory.createFinanceDataService(getContext());
		
		user = userAccountService.createUser(TEST_USERNAME, TEST_PASSWORD);
		account = financeDataService.createAccount(user, "test name", "test name", BigDecimal.ZERO, BigDecimal.ZERO);
		
	}
	
	// createUser, createAccount, createTransaction, 

	protected void tearDown() throws Exception {
		super.tearDown();
		databaseHelper.onUpgrade(database, 0, 0);
		
	}

}
