package com.example.demo.securityconfig;

//This class is required for storing the user name and password we receive from the client.

import java.io.Serializable;

public class JwtRequest implements Serializable {
	// The class implements the Serializable interface, which is a marker interface
	// used to indicate that the objects of this class can be converted into a byte stream. 
	// This is often used in such as when objects need to be passed between different parts of an application or across a network.

	private static final long serialVersionUID = 5926468583005150707L;

	// These variables are used to store the user's credentials received from the client.
	private String username;

	private String password;

	public JwtRequest() {

	}

	public JwtRequest(String username, String password) {

		this.setUsername(username);

		this.setPassword(password);

	}

	public String getUsername() {

		return this.username;

	}

	public void setUsername(String username) {

		this.username = username;

	}

	public String getPassword() {

		return this.password;

	}

	public void setPassword(String password) {

		this.password = password;

	}
}