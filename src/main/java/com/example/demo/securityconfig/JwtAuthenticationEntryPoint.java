package com.example.demo.securityconfig;

// This class will extend Spring's AuthenticationEntryPoint class and override its method commence. 
// It rejects every unauthenticated request and send error code 401

import java.io.IOException;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.AuthenticationEntryPoint;

import org.springframework.stereotype.Component;

@Component // annotation that allows Spring to detect our custom beans automatically.
          // Spring will: Scan our application for classes annotated with @Component. 
         // Instantiate them and inject any specified dependencies into them.

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	 // It is used for version control during object serialization and de-serialization.
	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	// This method is called whenever an unauthenticated user attempts to access a protected resource.
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) 
			throws IOException {

		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}
}
