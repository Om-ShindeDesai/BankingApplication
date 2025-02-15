package com.bank.service;

import java.util.List;

import com.bank.dto.BankResponse;
import com.bank.dto.CreditDebitRequest;
import com.bank.dto.EnquiryRequest;
import com.bank.dto.LoginDto;
import com.bank.dto.ResetPasswordRequest;
import com.bank.dto.TransactionDto;
import com.bank.dto.TransferRequest;
import com.bank.dto.UserRequest;

public interface UserService {
	BankResponse createAccount(UserRequest userRequest);
	
	BankResponse balanceEnquiry(EnquiryRequest request);
	
	String nameEnquiry(EnquiryRequest request);
	
	BankResponse creditAccount(CreditDebitRequest request);
	
	BankResponse debitAccount(CreditDebitRequest request);
	
	BankResponse transfer(TransferRequest request);

	BankResponse Login(LoginDto loginDto);
	
	BankResponse deleteUser(String accountNumber); 
	    
    BankResponse updateUser(String accountNumber, UserRequest userRequest);
    BankResponse logout();
    
    BankResponse resetPassword(ResetPasswordRequest request);
    
    List<TransactionDto> getTransactionHistory(String accountNumber);

}
