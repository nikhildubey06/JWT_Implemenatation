package com.example.demo.securityconfig;

//This is class is required for creating a response containing the JWT to be returned to the user.

import java.io.Serializable;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;

	private final String jwttoken; // This variable is used to store the JWT token generated during the authentication process.

	public JwtResponse(String jwttoken) { // constructor used to set the JWT token variable when creating an instance of the class.

		this.jwttoken = jwttoken;

	}

	public String getToken() {
		
		return this.jwttoken;

	}
}
