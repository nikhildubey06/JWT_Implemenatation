package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Registration;

public interface RegistrationRepo extends JpaRepository<Registration, Long> {

	Registration findByUserName(String userName);

	Registration findByEmail(String email);

}
