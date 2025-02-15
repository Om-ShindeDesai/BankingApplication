package com.bank.controller;

import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.customexception.ApiException;
import com.bank.dto.BankResponse;
import com.bank.dto.CreditDebitRequest;

import com.bank.dto.EnquiryRequest;
import com.bank.dto.LoginDto;
import com.bank.dto.ResetPasswordRequest;
import com.bank.dto.TransactionDto;
import com.bank.dto.TransferRequest;
import com.bank.dto.UserRequest;
import com.bank.dto.UserResponse;
import com.bank.pojos.User;
import com.bank.repository.UserRepository;
import com.bank.service.TokenBlackListService;
import com.bank.service.UserService;
import com.bank.utils.AccountUtils;

@RestController
@RequestMapping("/api/user")
@Component

public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TokenBlackListService tokenBlackListService;

	@PostMapping("/register")
	public BankResponse createAccount(@RequestBody UserRequest userRequest) {
		return userService.createAccount(userRequest);
	}	

	@PostMapping("/login")
	public BankResponse login(@RequestBody LoginDto loginDto) {
		System.out.println("In Controller");

		return userService.Login(loginDto);
	}

	@GetMapping("balanceEnquiry/{acno}")
//	@PreAuthorize("@userController.authenticateUser(principal)")	
	public BankResponse balanceEnquiry(@PathVariable String acno) {
		return userService.balanceEnquiry(new EnquiryRequest(acno));
	}

	@PostMapping("nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request) {
		System.out.println(request.getAccountNumber());
		return userService.nameEnquiry(request);
	}

	@PostMapping("creditAccount")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
		return userService.creditAccount(request);
	}

	@PostMapping("debitAccount")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
		return userService.debitAccount(request);
	}

	@PostMapping("transfer")
	public BankResponse transfer(@RequestBody TransferRequest request) {
		return userService.transfer(request);
	}

	@PutMapping("update/{accountNumber}")
	public BankResponse updateUser(@PathVariable String accountNumber, @RequestBody UserRequest userRequest) {
		return userService.updateUser(accountNumber, userRequest);
	}



	@DeleteMapping("delete")
	public BankResponse deleteUser(@RequestBody Map<String, String> request) {
	    String accountNumber = request.get("accountNumber");
	    return userService.deleteUser(accountNumber);
	}

//	  @PostMapping("/logout")
//	    public BankResponse logout() {
//		  System.out.println("In Logout Controller");
//		  
//	        return userService.logout();
//	    }
	
	@PostMapping("logout")
    public ResponseEntity<BankResponse> logout(@RequestHeader("Authorization") String authHeader) {
        // Extract the token (assuming the header is in the form "Bearer <token>")
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        
        // Add the token to the blacklist
        tokenBlackListService.addTokenToBlacklist(token);
        
        // Optionally clear the security context (if needed)
        // SecurityContextHolder.clearContext();
        
        BankResponse response = BankResponse.builder()
                .responseCode(AccountUtils.LOGOUT_SUCCESS_CODE)
                .responseMessage(AccountUtils.LOGOUT_SUCCESS_MESSAGE)
                .accountInfo(null)
                .build();
        
        return ResponseEntity.ok(response);
    }	
	@PostMapping("resetPassword")
    public ResponseEntity<BankResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        BankResponse response = userService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
	@GetMapping("/transactionHistory/{accountNumber}")
    public ResponseEntity<List<TransactionDto>> getTransactionHistory(@PathVariable String accountNumber) {
        List<TransactionDto> transactions = userService.getTransactionHistory(accountNumber);
        return ResponseEntity.ok(transactions);
    }

}
