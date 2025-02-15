package com.bank.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.pojos.Transaction;
import com.bank.service.BankStatement;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/bank")
@AllArgsConstructor
public class TransactionController {
	
	private final  BankStatement bankStatement;
	
	 @GetMapping("/statement")
	    public ResponseEntity<List<Transaction>> generateStatement(
	            @RequestParam String accountNumber,
	            @RequestParam String startDate,
	            @RequestParam String endDate) {
	        
	        List<Transaction> transactions = bankStatement.generateStatement(accountNumber, startDate, endDate);
	        return ResponseEntity.ok(transactions);
	    }

}
