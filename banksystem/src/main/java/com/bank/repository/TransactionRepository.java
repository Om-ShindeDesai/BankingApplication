package com.bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.dto.TransactionDto;
import com.bank.pojos.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
	List<Transaction> findByAccountNumber(String accountNumber);
}
