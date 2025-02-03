package com.bank.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.dto.AccountInfo;
import com.bank.dto.BankResponse;
import com.bank.dto.EmailDetails;
import com.bank.dto.EnquiryRequest;
import com.bank.dto.UserRequest;
import com.bank.pojos.User;
import com.bank.repository.UserRepository;
import com.bank.utils.AccountUtils;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	 EmailService emailService;

	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		
		if(userRepository.existsByEmail(userRequest.getEmail())) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		User newUser = User.builder()
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.otherName(userRequest.getOtherName())
				.gender(userRequest.getGender())
				.address(userRequest.getAddress())
				.stateOfOrigin(userRequest.getStateOfOrigin())
				.accountNumber(AccountUtils.generateAccountNumber())
				.accountBalance(BigDecimal.ZERO)
				.email(userRequest.getEmail())
				.phoneNumber(userRequest.getPhoneNumber())
				.alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
				.status("Active")
				.build();
		
		User savedUser = userRepository.save(newUser);
		
		//Send Email
		EmailDetails emailDetails = EmailDetails.builder()
				.recipient(savedUser.getEmail())
				.subject("BANK ACCOUNT CREATION")
				.messageBody("Congratulations!!! Your Account has been successfully created.\nYour Account Details:\n"
						+ "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + "\nAccount Number: " + savedUser.getAccountNumber())
				.build();
		emailService.sendEmailAlert(emailDetails);
		
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				.responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountBalance(savedUser.getAccountBalance())
						.accountNumber(savedUser.getAccountNumber())
						.accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
						
						.build())
				.build();
	}

	@Override
	public BankResponse enquiryRequest(EnquiryRequest request) {
		//check if the provided account number exists
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		return null;
	}

	@Override
	public String nameEnquiry(EnquiryRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
}