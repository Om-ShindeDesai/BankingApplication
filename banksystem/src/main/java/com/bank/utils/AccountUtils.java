package com.bank.utils;

import java.time.Year;

public class AccountUtils {
	
	public static final String ACCOUNT_EXISTS_CODE = "001";
	public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account created!"; 
	public static final String ACCOUNT_CREATION_SUCCESS =  "002";
	public static final String ACCOUNT_CREATION_MESSAGE =  "Account has been successfully created!";
	public static final String ACCOUNT_NOT_EXISTS_CODE = "003";
	public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "Account does not exist!";
	public static final String ACCOUNT_FOUND_CODE = "004";
	public static final String ACCOUNT_FOUND_MESSAGE = " User Account found!";
	public static final String ACCOUNT_CREDIT_SUCCESS = "005";
	public static final String ACCOUNT_CREDIT_MESSAGE = "Account credited successfully!";
	public static final String INSUFFICIENT_BALANCE_CODE = "006";
	public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance in the account!";
	public static final String ACCOUNT_DEBIT_SUCCESS = "007";
	public static final String ACCOUNT_DEBIT_MESSAGE = "Account debited successfully!";
	public static final String TRANSFER_SUCCESSFUL_CODE = "008";
	public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer successful!";
	public static final String LOGIN_SUCCESS_CODE = "009";
	public static final String LOGIN_SUCCESS_MESSAGE = "Login successful!";
	public static final String ACCOUNT_DELETED_SUCCESS_CODE = "010";
	public static final String ACCOUNT_DELETED_SUCCESS_MESSAGE = "Account deleted successfully!";
	public static final String ACCOUNT_UPDATE_SUCCESS_CODE = "011";
	public static final String ACCOUNT_UPDATE_SUCCESS_MESSAGE = "Account updated successfully!";
	public static final String LOGOUT_SUCCESS_CODE = "012";
	public static final String LOGOUT_SUCCESS_MESSAGE = "Logged out successfully!";
	
	public static String generateAccountNumber()
	{
		/*
		 * current year 2025 + random six digits
		 */
		Year currentYear = Year.now();
		int min = 100000;
		int max = 999999;
		
		int randNumber = (int)Math.floor(Math.random() * (max - min + 1) + min);
		
		//convert the current year and random number to string then concatenate
		String year = String.valueOf(currentYear);
		String randomNumber = String.valueOf(randNumber);
		StringBuilder accountNumber = new StringBuilder();
		
		return accountNumber.append(year).append(randNumber).toString();
		
		
		
	}
	
}
