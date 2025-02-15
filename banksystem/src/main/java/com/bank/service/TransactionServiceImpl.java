package com.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bank.dto.TransactionDto;
import com.bank.pojos.Transaction;
import com.bank.repository.TransactionRepository;


@Component
public class TransactionServiceImpl implements TransactionService {

	
	@Autowired
    TransactionRepository transactionRepository;
	
	
	@Override
	public void saveTransaction(TransactionDto transactionDto) {

		Transaction transaction = Transaction.builder()
				.transactionType(transactionDto.getTransactionType())
				.accountNumber(transactionDto.getAccountNumber())
				.transactionAmount(transactionDto.getTransactionAmount())
				.createdAt(transactionDto.getTransactionDate())
				.status("SUCCESS")
				
				.build();
		
		transactionRepository.save(transaction);
		System.out.println("Transaction saved Successfully");
	}

}
