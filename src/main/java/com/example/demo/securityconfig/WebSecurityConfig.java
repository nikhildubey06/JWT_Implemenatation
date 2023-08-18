package com.example.demo.securityconfig;

//This class extends the WebSecurityConfigurerAdapter is a convenience class 
//that allows customization to both WebSecurity and HttpSecurity.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//When you apply this annotation to a configuration class, 
//Spring will process the security annotations on individual methods in the application

public class WebSecurityConfig {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) 
//			throws Exception {
//
//		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
//
//	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setUserDetailsService(this.jwtUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	// The passwordEncoder method creates a bean of BCryptPasswordEncoder, 
	// which is used to encode and verify passwords during authentication.
	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();

	}

	// This method creates a bean for the AuthenticationManager 
	// so that it can be autowired and used in other parts of the application.
	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();

	}

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable().authorizeRequests()

				.antMatchers("/Records/users/login", "/Records/users/signup","/swagger-ui.html","/v2/api-docs", "/configuration/ui",
						"/swagger-resources/**", "/configuration/security", "/webjars/**")
				.permitAll()
				.anyRequest().authenticated().and()

				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		httpSecurity.authenticationProvider(daoAuthenticationProvider());
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
		DefaultSecurityFilterChain defaultSecurityFilterChain=httpSecurity.build();
		return defaultSecurityFilterChain;
	}
}