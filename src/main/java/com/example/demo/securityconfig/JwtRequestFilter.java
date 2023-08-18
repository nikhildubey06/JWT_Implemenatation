package com.example.demo.securityconfig;

// The JwtRequestFilter extends the Spring Web Filter OncePerRequestFilter class. 
// For any incoming request this Filter class gets executed. It checks if the request has a valid JWT token. 
// If it has a valid JWT Token then it sets the Authentication in the context, to specify that the current user is authenticated.

import java.io.IOException;

import javax.servlet.FilterChain;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter { // Spring Web Filter that ensures the filter is 
	                                                        //  executed only once per request

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService; // used to load user-details based on the user name extracted
														 // from the JWT token

	@Autowired
	private JwtTokenUtil jwtTokenUtil; // used for token validation and extraction.

	@Override
	// the filter starts by extracting the JWT token from the Authorization header of the incoming HTTP request. 
	// if the token exists and starts with "Bearer ", it is extracted from the header.
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
	throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;

		String jwtToken = null;

		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

			jwtToken = requestTokenHeader.substring(7);

			try {

				username = jwtTokenUtil.getUsernameFromToken(jwtToken);

			} catch (IllegalArgumentException e) {

				System.out.println("Unable to get JWT Token");

			} catch (ExpiredJwtException e) {

				//System.out.println("Sorry ! JWT Token has expired");

			}
		}

		// Once we get the token we validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			//ensures that the user is not already authenticated in the current security context. 

			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			// if token is valid configure Spring Security to manually set authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// After setting the Authentication in the context, we specify that the current user is authenticated.
				// So it passes the Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

			}
		}

		// Finally, the filter allows the request to continue its normal flow through the filter chain by calling this.
		chain.doFilter(request, response);
	}
}
