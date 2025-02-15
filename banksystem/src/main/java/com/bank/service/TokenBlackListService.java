package com.bank.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenBlackListService {

	// Implement token blacklist functionality here
	
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    
    public void addTokenToBlacklist(String token) {
        blacklistedTokens.add(token);
    }
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

}
