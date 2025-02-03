package com.bank.service;

import com.bank.dto.BankResponse;
import com.bank.dto.EnquiryRequest;
import com.bank.dto.UserRequest;

public interface UserService {
	BankResponse createAccount(UserRequest userRequest);
	
	BankResponse enquiryRequest(EnquiryRequest request);
	
	String nameEnquiry(EnquiryRequest request);

}
