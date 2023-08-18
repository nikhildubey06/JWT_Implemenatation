package com.example.demo.securityconfig;

// Expose a POST API /authenticate using the JwtAuthenticationController. 
// The POST API gets user name and password in the body using Spring Authentication Manager we authenticate user name and password.
// If the credentials are valid, a JWT token is created using the JWTTokenUtil and provided to the client.

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.authentication.DisabledException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	// An interface provided by Spring Security to load user-specific data.
	private UserDetailsService jwtInMemoryUserDetailsService;

	// Specifies the URL path ("/authenticate") to which the method should respond when it receives a POST request.
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)

	public ResponseEntity<?> generateAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		//converted the response into json
		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));

	}
	
	//validate user name and password using spring authenticate()
	private void authenticate(String username, String password) throws Exception {

		Objects.requireNonNull(username);

		Objects.requireNonNull(password);

		try {

			// If the credentials are valid, a JWT token is generated using the JwtTokenUtil and returned to the client.
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		} catch (DisabledException e) {

			throw new Exception("USER_DISABLED", e);

		} catch (BadCredentialsException e) {

			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
