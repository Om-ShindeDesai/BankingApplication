package com.bank.cofig;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bank.service.TokenBlackListService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

//	@Autowired
//	private UserServiceImpl userService;
	


	
	private JwtTokenProvider jwtTokenProvider;
	private  UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("Authentication Filter");
		
		System.out.println(request.getParameter("email"));	
		request.getParameterMap().forEach((key, value) -> {
			System.out.println("Parameter Name: " + key);
			System.out.println("Parameter Value: " + Arrays.toString(value));
		});
		
		String token = getTokenFromRequest(request);
		
		if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
			
			
			System.out.println("In if");
			String username = jwtTokenProvider.getUsername(token);
			UserDetails userDetails =userDetailsService.loadUserByUsername(username);
  
//			userService.setCurrentUserEmail(username);

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				
				    userDetails, null, userDetails.getAuthorities());
		
		 authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		 SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		
		}
		
		System.out.println(" after If");
		filterChain.doFilter(request, response);
		
	}
 private String getTokenFromRequest(HttpServletRequest request) {
	 
	 String bearerToken = request.getHeader("Authorization");
	 
	 if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
         return bearerToken.substring(7);
     }
	 return null;
 }
}
