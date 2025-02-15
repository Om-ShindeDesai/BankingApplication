package com.bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.pojos.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Boolean existsByEmail(String email);
	
     User findByEmail(String email);
     
	
	Boolean existsByAccountNumber(String accountNumber);
	
	User findByAccountNumber(String accountNumber);

	
	@Transactional
	void deleteByAccountNumber(String accountNumber);

}
