package com.webapp.bankingportal.Entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;		
	
	@NotEmpty
	@Column(unique = true)
	private String token;
	
	@NotNull
	private Date createdAt = new Date();
	
	@NotNull
    private Date expiresAt;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;
	
	public Token(String token, Date expiresAt, Account account) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.account = account;
    }
}
