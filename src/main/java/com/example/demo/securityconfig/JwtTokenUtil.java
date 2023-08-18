package com.example.demo.securityconfig;

// The JwtTokenUtil is responsible for performing JWT operations like creation and validation.
// It makes use of the io.jsonwebtoken.Jwts for achieving this

import java.io.Serializable;

import java.util.Date;

import java.util.HashMap;

import java.util.Map;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable { // Serialization is what Spring boot does when
													// it converts the Java object to JSON object

	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY = 60*60;

	@Value("${jwt.secret}")

	private String secret; // This secret key is used for signing and verifying the JWT tokens.

	// retrieve user name from jwt token
	public String getUsernameFromToken(String token) {

		return getClaimFromToken(token, Claims::getSubject);

	}

	// retrieve issued date from jwt token
	public Date getIssuedAtDateFromToken(String token) {

		return getClaimFromToken(token, Claims::getIssuedAt);

	}

	// retrieve expiry date from jwt token
	public Date getExpirationDateFromToken(String token) {

		return getClaimFromToken(token, Claims::getExpiration);

	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {

		final Claims claims = getAllClaimsFromToken(token);

		return claimsResolver.apply(claims);

	}

	// This private method parses the JWT token, verifies its signature using the secret key
	// and returns all the claims present in the token.
	private Claims getAllClaimsFromToken(String token) {

		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

	}

	// check if token is expired
	private Boolean isTokenExpired(String token) {

		final Date expiration = getExpirationDateFromToken(token);

		return expiration.before(new Date());

	}

	// This private method can be extended to customize whether to ignore the token expiration or not.
	// In this implementation, it always returns false, meaning the token expiration is not ignored.
	private Boolean ignoreTokenExpiration(String token) {

		return false;

	}

	// generate token for user
	public String generateToken(UserDetails userDetails) {

		Map<String, Object> claims = new HashMap<>();

		return doGenerateToken(claims, userDetails.getUsername());

	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();

	}

	// This method checks whether a token can be refreshed, meaning it hasn't expired, or the expiration can be ignored.
	public Boolean canTokenBeRefreshed(String token) {

		return (!isTokenExpired(token) || ignoreTokenExpiration(token));

	}

	// This method validates whether the token is valid for the given UserDetails.
	// It checks if the user name in the token matches the user name in the provided UserDetails and if the token has not expired.
	public Boolean validateToken(String token, UserDetails userDetails) {

		final String username = getUsernameFromToken(token);

		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

	}
}