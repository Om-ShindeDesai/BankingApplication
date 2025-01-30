package com.webapp.bankingportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapp.bankingportal.Entity.User;

public interface UserRepository extends JpaRepository<User ,Long> {

}
