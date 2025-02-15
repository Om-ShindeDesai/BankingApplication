package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ResetPasswordRequest {

	   private String accountNumber;
	    private String oldPassword;
	    private String newPassword;
	    
}
