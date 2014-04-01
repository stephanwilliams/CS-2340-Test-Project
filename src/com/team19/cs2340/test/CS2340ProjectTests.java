package com.team19.cs2340.test;

import java.math.BigDecimal;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.team19.cs2340.DatabaseHelper;
import com.team19.cs2340.finance.FinanceDataException;
import com.team19.cs2340.finance.FinanceDataServiceFactory;
import com.team19.cs2340.finance.IAccount;
import com.team19.cs2340.finance.IFinanceDataService;
import com.team19.cs2340.finance.ITransaction.TransactionType;
import com.team19.cs2340.user.IUser;
import com.team19.cs2340.user.IUserAccountService;
import com.team19.cs2340.user.UserAccountServiceFactory;
import com.team19.cs2340.user.UserAccountException;


public class CS2340ProjectTests extends AndroidTestCase {

	private static final String TEST_USERNAME = "TEST_USER";
	private static final String TEST_USERNAME2 = "TEST_USER2";
	private static final String TEST_USERNAME3 = "TEST_USER3";
	private static final String TEST_PASSWORD = "TEST_PASSWORD";
	
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase database;
	private IUserAccountService userAccountService;
	private IFinanceDataService financeDataService;
	private IUser user;
	private IUser user2;
	private IUser user3;
	private IAccount account;
	private IAccount account2;
	private IAccount account3;

	protected void setUp() throws Exception {
		super.setUp();
		databaseHelper = new DatabaseHelper(getContext());
		database = databaseHelper.getWritableDatabase();
		
		userAccountService = UserAccountServiceFactory.createUserAccountService(getContext());
		financeDataService = FinanceDataServiceFactory.createFinanceDataService(getContext());
		
		user = userAccountService.createUser(TEST_USERNAME, TEST_PASSWORD);
		account = financeDataService.createAccount(user, "test name", "test name", BigDecimal.ZERO, BigDecimal.ZERO);
		
		user2 = userAccountService.createUser(TEST_USERNAME2, TEST_PASSWORD);
		account2 = financeDataService.createAccount(user2, "test name", "test name", BigDecimal.ZERO, BigDecimal.ZERO);
		
		user3 = userAccountService.createUser(TEST_USERNAME3, TEST_PASSWORD);
		account3 = financeDataService.createAccount(user3, "test name", "test name", BigDecimal.ZERO, BigDecimal.ZERO);
		financeDataService.createTransaction(account3, 2, TransactionType.DEPOSIT, "test_category", BigDecimal.ZERO, "test_reason");
		financeDataService.createTransaction(account3, 3, TransactionType.DEPOSIT, "test_category", BigDecimal.ZERO, "test_reason");
	}
	
	public void testAuthenticateUser() throws UserAccountException{
		userAccountService.authenticateUser(TEST_USERNAME, TEST_PASSWORD);
	}
	
	//Random Password
	public void testAuthenticateUserInvalidPassword(){
		try{
			userAccountService.authenticateUser(TEST_USERNAME, TEST_PASSWORD + "a");
		} catch (UserAccountException uae){
			assertEquals("Invalid password", uae.getMessage());
			return;
		}
		fail("Should throw exception on invalid password");
	}
	
	//Null Password
	public void testAuthenticateUserNullPassword(){
		try{
			userAccountService.authenticateUser(TEST_USERNAME, "");
		} catch (UserAccountException uae){
			assertEquals("No Password", uae.getMessage());
			return;
		}
		fail("Should throw exception on null password");
	}
	
	//Null User
	public void testAuthenticateUserNullUser(){
		try{
			userAccountService.authenticateUser("", TEST_PASSWORD);
		} catch (UserAccountException uae){
			assertEquals("No User", uae.getMessage());
			return;
		}
		fail("Should throw exception on null user");
	}
	
	
	public void testGetAccount() throws FinanceDataException {
		financeDataService.getAccount(user, account.getAccountId());
	}
	public void testGetAccountNullUser() {
		try {
			financeDataService.getAccount(null, 0);
		} catch (FinanceDataException fde) {
			assertEquals("User must not be null", fde.getMessage());
			return;
		}
		fail("Should throw exception on null user");
	}
	
	public void testGetAccountNonexistentAccount() {
		try {
			financeDataService.getAccount(user, account.getAccountId() + 1000);
		} catch (FinanceDataException fde) {
			assertEquals("Account does not exist", fde.getMessage());
			return;
		}
		fail("Should throw exception on nonexistent account");
	}
	
	public void testGetAccountWrongOwner() {
		try {
			financeDataService.getAccount(user, account2.getAccountId());
		} catch (FinanceDataException fde) {
			assertEquals("Account belongs to different user", fde.getMessage());
			return;
		}
		fail("Should throw exception on attempted non-owned account access");
	}
	
	public void testGetTransactionsLength() {
		try {
			assertEquals(2, financeDataService.getTransactions(account3).size());
		} catch (FinanceDataException fde) {
			//assertEquals("Account is null", fde.getMessage());
			return;
		}
		return;
	}
	
	public void testGetTransactionsInvalidAccount() {
		try {
			financeDataService.getTransactions(null); 
		} catch (FinanceDataException fde) {
			assertEquals("Account is null", fde.getMessage());
			return;
		}
		fail("Should throw exception that account ");
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		databaseHelper.onUpgrade(database, 0, 0);
	}

}
