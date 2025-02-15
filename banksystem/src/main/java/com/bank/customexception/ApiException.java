package com.bank.customexception;

public class ApiException  extends RuntimeException {
	
	public ApiException(String message) {
        super(message);
    }

}
