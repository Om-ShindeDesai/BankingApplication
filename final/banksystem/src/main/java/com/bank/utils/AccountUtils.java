package com.bank.utils;

import java.time.Year;

public class AccountUtils {
	
	public static final String ACCOUNT_EXISTS_CODE = "001";
	public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account created!"; 
	public static final String ACCOUNT_CREATION_SUCCESS =  "002";
	public static final String ACCOUNT_CREATION_MESSAGE =  "Account has been successfully created!";
	
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
