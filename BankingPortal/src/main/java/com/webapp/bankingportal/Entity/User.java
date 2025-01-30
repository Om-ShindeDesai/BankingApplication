package com.webapp.bankingportal.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data   /*The @Data annotation is part of Project Lombok, a Java library that helps reduce boilerplate code. 
         It is used to automatically generate commonly used methods like
          getters, setters, toString(), equals(), hashCode(), and constructors. */
public class User {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    @NotEmpty
    private String name;
    
    @NotEmpty
    private String password;
    
   @Email     /* @Email validates if the provided string is a proper email format.*/
    @NotEmpty 
    @Column(unique = true)
    private String email;
   
    @NotEmpty
    @Column(unique = true)
    private String phoneNumber;
    
   @NotEmpty
    private String address;
   
   // Establishing a one-to-one relationship with the account
   @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
   private Account account;
}
