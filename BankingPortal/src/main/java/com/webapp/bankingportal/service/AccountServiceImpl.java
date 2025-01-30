package com.webapp.bankingportal.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.webapp.bankingportal.Entity.Account;
import com.webapp.bankingportal.Entity.User;
import com.webapp.bankingportal.repository.AccountRepository;

public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public Account createAccount(User user) {
		// TODO Auto-generated method stub
		Account account = new Account();
		
		
//		return accountRepository.save(user);
		return null;
		
		
		
	}

}
