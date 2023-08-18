package com.example.demo.securityconfig;

// JWTUserDetailsService implements the Spring Security UserDetailsService interface.
// It overrides the loadUserByUsername for fetching user details from the database using the user name. 
// The Spring Security Authentication Manager calls this method for getting the user details from
// the database when authenticating the user details provided by the user. 
// Here we are getting the user details from a hard coded User List.

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Registration;
import com.example.demo.repository.RegistrationRepo;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private final RegistrationRepo registerRepo;

	@Autowired
	public JwtUserDetailsService(RegistrationRepo registerRepo) {
		this.registerRepo = registerRepo;
	}

	@Override
	// This method retrieves user information based on the given user name (in this case, the user's email).
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Registration user = registerRepo.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}

		return new User(user.getEmail(), user.getPassword(), new ArrayList<>());

	}

}