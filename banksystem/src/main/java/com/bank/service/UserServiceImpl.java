package com.bank.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.cofig.JwtTokenProvider;
import com.bank.dto.AccountInfo;
import com.bank.dto.BankResponse;
import com.bank.dto.CreditDebitRequest;
import com.bank.dto.EmailDetails;
import com.bank.dto.EnquiryRequest;
import com.bank.dto.LoginDto;
import com.bank.dto.ResetPasswordRequest;
import com.bank.dto.TransactionDto;
import com.bank.dto.TransferRequest;
import com.bank.dto.UserRequest;
import com.bank.pojos.Role;
import com.bank.pojos.Transaction;
import com.bank.pojos.User;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import com.bank.utils.AccountUtils;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

@Service
@Setter
@Getter
public class UserServiceImpl implements UserService {
	
	
//	private String currentUserEmail ;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	 EmailService emailService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider; 
	
	@Autowired
	TransactionRepository transactionRepository;
	 
	
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
				.password(passwordEncoder.encode(userRequest.getPassword()))
				.phoneNumber(userRequest.getPhoneNumber())
				.alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
				.status("Active")
				.role(Role.valueOf("ROLE_ADMIN"))
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
	
	
	
	
	public BankResponse Login(LoginDto loginDto) {
		
//		User user = userRepository.findByEmail(loginDto.getEmail());
//        
//        if(user == null ||!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
//            return BankResponse.builder()
//                    .responseCode(AccountUtils.INVALID_CREDENTIALS_CODE)
//                    .responseMessage(AccountUtils.INVALID_CREDENTIALS_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }
//        
//        return BankResponse.builder()
//                .responseCode(AccountUtils.LOGIN_SUCCESS_CODE)
//                .responseMessage(AccountUtils.LOGIN_SUCCESS_MESSAGE) 
		
		System.out.println("Email: " + loginDto.getEmail());
		System.out.println("Password: " + loginDto.getPassword());
		Authentication authentication = null;
		
		
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
		
		System.out.println("Email: " + loginDto.getEmail());
		System.out.println("Password: " + loginDto.getPassword());
		User user = userRepository.findByEmail(loginDto.getEmail());
		
		EmailDetails loginAlert = EmailDetails.builder()
                .subject("Bank LOGIN")
				.recipient(loginDto.getEmail())

                .messageBody("Welcome to the Apna-Bank! You have successfully logged in.")
                .
			build();
		emailService.sendEmailAlert(loginAlert);
		
		String token = jwtTokenProvider.generateToken(authentication);
		
		 
   return BankResponse.builder()
            .responseCode(AccountUtils.LOGIN_SUCCESS_CODE)
             .responseMessage(jwtTokenProvider.generateToken(authentication)).accountInfo(new AccountInfo(user.getAccountNumber(), user.getFirstName()+user.getLastName()+user.getOtherName(),user.getAccountBalance()))
             .build();
		
	}
	

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest request) {
		
		
//		if(!validateUser(request.getAccountNumber())) {
//			throw new RuntimeException("You are not authrized to view this account balance..!!");
//		}
		//check if the provided account number exists
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		
		if(!isAccountExist) {
			
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		User foundUser =userRepository.findByAccountNumber(request.getAccountNumber());
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
				.responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
				.accountInfo(AccountInfo.builder().accountBalance(foundUser.getAccountBalance())
						.accountNumber(request.getAccountNumber())
						.accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName())
						.build())
				.build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest request) {
		
		System.out.println(request.getAccountNumber());
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		System.out.println("isAccountExist: " + isAccountExist);
		
		if(!isAccountExist) {
			
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }
		User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
		String response = foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
		
		System.out.println(response);
		return response;

		
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest request) {
		
		//Checking If the Account Exists
		
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		
if(!isAccountExist) {
			
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
		}

       User userToCredit =userRepository.findByAccountNumber(request.getAccountNumber());
       
       
       
       userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
       
       userRepository.save(userToCredit);
       //Save Transaction
       
       TransactionDto transactionDto = TransactionDto.builder()
    		   .accountNumber(userToCredit.getAccountNumber())
    		   .transactionType("CREDIT")
    		   .transactionAmount(request.getAmount())
    		   .build();
       
       transactionService.saveTransaction(transactionDto);
       

		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREDIT_SUCCESS)
				.responseMessage(AccountUtils.ACCOUNT_CREDIT_MESSAGE)
				.accountInfo(AccountInfo.builder()
						
						.accountName(userToCredit.getFirstName()+" "+ userToCredit.getLastName()+ " "+ userToCredit.getOtherName())
						.accountBalance(userToCredit.getAccountBalance())
						.accountNumber(userToCredit.getAccountNumber())
						.build())
				
				.build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest request) {
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
if(!isAccountExist) {
			
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
		}

 // Check if the amount is available in the account or not more than the available amount in the account

User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());

BigInteger availableAmount =userToDebit.getAccountBalance().toBigInteger();

BigInteger debitAmount = request.getAmount().toBigInteger();

if(availableAmount.intValue() < debitAmount.intValue()) {
	
	return BankResponse.builder()
			.responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
			.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
			.accountInfo(null)
			.build();
			
}



else {
	userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
    userRepository.save(userToDebit);
    
    TransactionDto transactionDto = TransactionDto.builder()
    		.accountNumber(userToDebit.getAccountNumber())
    		.transactionType("DEBIT")
    		.transactionAmount(request.getAmount())
    		.build();

    		transactionService.saveTransaction(transactionDto);
    
    return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS)
            .responseMessage(AccountUtils.ACCOUNT_DEBIT_MESSAGE)
            .accountInfo(AccountInfo.builder()
                        .accountName(userToDebit.getFirstName()+" "+ userToDebit.getLastName()+ " "+ userToDebit.getOtherName())
                        .accountBalance(userToDebit.getAccountBalance())
                        .accountNumber(userToDebit.getAccountNumber())
                        .build())
            .build();
}
		
		
	}

	@Override
	public BankResponse transfer(TransferRequest request) {
		
		//Get the Account to Debit
		//Check if the account i am Debiting  is not more than Current Balance
		//debit the account 
		//get the account to credit
		//credit the Account
		
		boolean isDestinationAccountExists = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
		
		if(!isDestinationAccountExists) {
			
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
		
		if(request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0){
			
			return BankResponse.builder()
					.responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
		String sourceUserName = sourceAccountUser.getFirstName()+" "+sourceAccountUser.getLastName()+" "+sourceAccountUser.getOtherName();
		
		userRepository.save(sourceAccountUser);
		
		EmailDetails debitAlert = EmailDetails.builder()
				.subject("DEBIT ALERT")
				.recipient(sourceAccountUser.getEmail())
				.messageBody("The Sum of "+ request.getAmount()+ " has been deducted from your Account! Your Current Balance is" +sourceAccountUser.getAccountBalance())
				
				.build();
		
		
		
		emailService.sendEmailAlert(debitAlert);
		
		User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
		destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
		
//		String recipientUserName = destinationAccountUser.getFirstName() + " "+destinationAccountUser.getLastName()+" "+ destinationAccountUser.getOtherName(); 
		 userRepository.save(destinationAccountUser);
		EmailDetails creditAlert = EmailDetails.builder()
				.subject("CREDIT ALERT")
				.recipient(destinationAccountUser.getEmail())
				.messageBody("The Sum of "+ request.getAmount()+ " has been credit to your Account From "+sourceUserName+" Your Current Balance is" +destinationAccountUser.getAccountBalance())
				
				.build();
		emailService.sendEmailAlert(creditAlert);
		
		TransactionDto transactionDto = TransactionDto.builder()
				.accountNumber(destinationAccountUser.getAccountNumber())
				.transactionType("CREDIT")
				.transactionAmount(request.getAmount())
				.transactionDate(request.getTransferDate())
				.build();

				transactionService.saveTransaction(transactionDto);
		
		
		return  BankResponse.builder()
				.responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
				.responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
				.accountInfo(null)
				
				.build();
	}
	
	@Override
	@Transactional
	public BankResponse deleteUser(String accountNumber) {
	    boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);

	    if (!isAccountExist) {
	        return BankResponse.builder()
	                .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
	                .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
	                .accountInfo(null)
	                .build();
	    }

	    userRepository.deleteByAccountNumber(accountNumber);

	    return BankResponse.builder()
	            .responseCode(AccountUtils.ACCOUNT_DELETED_SUCCESS_CODE)
	            .responseMessage(AccountUtils.ACCOUNT_DELETED_SUCCESS_MESSAGE)
	            .accountInfo(null)
	            .build();
	}
	
	@Override
	public BankResponse updateUser(String accountNumber, UserRequest userRequest) {
	    boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);

	    if (!isAccountExist) {
	        return BankResponse.builder()
	                .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
	                .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
	                .accountInfo(null)
	                .build();
	    }

	    User existingUser = userRepository.findByAccountNumber(accountNumber);
	    
	    // Update user details
	    existingUser.setFirstName(userRequest.getFirstName());
	    existingUser.setLastName(userRequest.getLastName());
	    existingUser.setOtherName(userRequest.getOtherName());
	    existingUser.setGender(userRequest.getGender());
	    existingUser.setAddress(userRequest.getAddress());
	    existingUser.setStateOfOrigin(userRequest.getStateOfOrigin());
	    existingUser.setPhoneNumber(userRequest.getPhoneNumber());
	    existingUser.setAlternativePhoneNumber(userRequest.getAlternativePhoneNumber());

	    // If password is being updated, encode it
	    if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
	        existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
	    }

	    userRepository.save(existingUser);

	    return BankResponse.builder()
	            .responseCode(AccountUtils.ACCOUNT_UPDATE_SUCCESS_CODE)
	            .responseMessage(AccountUtils.ACCOUNT_UPDATE_SUCCESS_MESSAGE)
	            .accountInfo(AccountInfo.builder()
	                    .accountBalance(existingUser.getAccountBalance())
	                    .accountNumber(existingUser.getAccountNumber())
	                    .accountName(existingUser.getFirstName() + " " + existingUser.getLastName() + " " + existingUser.getOtherName())
	                    .build())
	            .build();
	}
	
//	private boolean validateUser(String accountNumber) {
//		// finding user from db based on email received in token
//		User user = userRepository.findByEmail(currentUserEmail);
//
//		// check if account number in token matches the account number in the request
//		// if match is not found, throw an exception
//		return user.getAccountNumber().equals(accountNumber);
//	}

	
	 @Override
	    public BankResponse logout() {
	        // Clear the security context (this essentially logs out the user)
	        SecurityContextHolder.clearContext();
	        System.out.println("User logged out successfully");
	        return BankResponse.builder()
	                .responseCode(AccountUtils.LOGOUT_SUCCESS_CODE)
	                .responseMessage(AccountUtils.LOGOUT_SUCCESS_MESSAGE)
	                .accountInfo(null)
	                .build();
	    }
	 
	 @Override
	    public BankResponse resetPassword(ResetPasswordRequest request) {
	        User user = userRepository.findByAccountNumber(request.getAccountNumber());
	        
	        if (user == null) {
	            throw new RuntimeException("User not found");
	        }
	        
	        // Verify old password
	        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
	            throw new RuntimeException("Old Password doesnot match");
	        }
	        
	        // Update password
	        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
	        userRepository.save(user);
	        
	        return BankResponse.builder()
	        		.responseCode("012")
	        		.responseMessage("Password Changed Successfully")
	        		.build() ;
	    }
	 @Override
		public List<TransactionDto> getTransactionHistory(String accountNumber) {
			// TODO Auto-generated method stub
			
	        List<Transaction> transactions = transactionRepository.findByAccountNumber(accountNumber);

			return transactions.stream()
					.map(tx ->  TransactionDto.builder()
							
							.transactionType(tx.getTransactionType())
							.transactionAmount(tx.getTransactionAmount())
							.accountNumber(tx.getAccountNumber())
							.status(tx.getStatus())
							.transactionDate(tx.getCreatedAt())
							.build())
					       .collect(Collectors.toList());
		}
	    

}