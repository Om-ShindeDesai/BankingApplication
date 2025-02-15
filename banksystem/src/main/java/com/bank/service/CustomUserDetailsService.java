package com.bank.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bank.pojos.User;
import com.bank.repository.UserRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
//		return userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username + " not found"));
		User user = userRepository.findByEmail(username);
		if(user == null) {
            throw new UsernameNotFoundException(username + " not found");
        }		
//		 return new org.springframework.security.core.userdetails.User(
//				 user.getId(),
//			        user.getEmail(), 
//			        user.getPassword(), 
//			        user.getAuthorities()
//			    );	

		return userRepository.findByEmail(username);
//		return new UserPrincipal(user);
	}

}
