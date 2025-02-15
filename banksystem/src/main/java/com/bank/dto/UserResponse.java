package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

import com.bank.pojos.User;

@Data
@AllArgsConstructor
public class UserResponse {
    private String responseCode;
    private String responseMessage;
    private List<User> users;
    private String accountNumber;
}
