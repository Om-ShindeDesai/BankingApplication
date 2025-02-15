package com.bank.cofig;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.bank.service.TokenBlackListService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;


@Component
public class JwtTokenProvider {
	
	@Autowired
	private TokenBlackListService tokenBlacklistService;

	
	@Value("${app.jwt-secret}")
	private String jwtSecret;
	
	@Value("${app.jwt-expirationTime}")
	private long jwtExpirationDate;
	
	public String generateToken(Authentication authentication) {
		
		String username = authentication.getName();
		Date currentdate =  new Date();
		Date expireDate = new Date(currentdate.getTime() + jwtExpirationDate);
		
        return Jwts.builder()
        		.setSubject(username)
                .setIssuedAt(currentdate)
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
		
	}
	private Key key() {
		byte[] bytes = Decoders.BASE64.decode(jwtSecret);
		
		return Keys.hmacShaKeyFor(bytes);
	}
	
	public String getUsername(String token) {
		
		Claims claims = Jwts.parser()
				.setSigningKey(key())
				.build()
                .parseClaimsJws(token)
                .getBody();
		
		return claims.getSubject();
		
	}
	
	public boolean validateToken(String token) {
		
		if(tokenBlacklistService.isTokenBlacklisted(token)) {
			throw new RuntimeException("Token blacklisted");
		}
		try {
            Jwts.parser().setSigningKey(key())
//            .setAllowedClockSkewSeconds(60000) //
            .build()
            .parse(token);
            return true;
        } catch (ExpiredJwtException | IllegalArgumentException | SignatureException| MalformedJwtException e) {
            throw new RuntimeException(e);
        }
	}
	
	 public String getUsernameFromToken(String token)  {
	        return null;
	    }

}
