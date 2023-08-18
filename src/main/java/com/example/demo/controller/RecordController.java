package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.RecordEntity;
import com.example.demo.entity.Registration;
import com.example.demo.repository.RecordRepo;
import com.example.demo.repository.RegistrationRepo;
import com.example.demo.securityconfig.JwtTokenUtil;

@RestController
@RequestMapping("/Records")
public class RecordController {

	@Autowired
	private RecordRepo repo;

	@Autowired
	private RegistrationRepo registerRepo;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@PostMapping("/addRecord")
	public RecordEntity addRecord(@RequestBody RecordEntity recordEntity) {

		return repo.save(recordEntity);
	}

	@PostMapping("/addRecords")
	public List<RecordEntity> addRecords(@RequestBody List<RecordEntity> recordEntity) {

		return repo.saveAll(recordEntity);
	}

	@GetMapping("/getRecords")
	public List<RecordEntity> findAllRecords() {

		return repo.findAll();
	}

	@GetMapping("/getRecord/{id}")
	public Optional<RecordEntity> findRecordById(@PathVariable int id) {

		return repo.findById(id);
	}

	@PutMapping("/updateRecord/{id}")
	public RecordEntity updateRecord(@PathVariable("id") int id, @RequestBody RecordEntity recordEntity) {
		Optional<RecordEntity> a = repo.findById(id);
		if (a != null) {
			recordEntity.setId(id);

			return repo.save(recordEntity);
		} else {
			return null;
		}
	}

	@DeleteMapping("deleteRecord/{id}")
	public void deleteRecord(@PathVariable int id) {

		repo.deleteById(id);
		System.out.println("Delete succesfully");
	}

	// Registration
	@PostMapping("/users/signup")
	public Registration signUp(@RequestBody Registration user) {

		Registration existingUser = registerRepo.findByUserName(user.getUserName());
		Registration existingEmail = registerRepo.findByEmail(user.getEmail());
		if (existingUser != null) {
			throw new RuntimeException("Username already exists");
		} else if (existingEmail != null) {
			throw new RuntimeException("Email already exists");
		} else {
			return registerRepo.save(user);
		}
	}

	// Login
	@PostMapping("/users/login")
	public Map<String, String> login(@RequestBody Map<String, String> credentials) {
		String email = credentials.get("email");
		String password = credentials.get("password");
		Registration user = registerRepo.findByEmail(email);
		if (user == null || !password.equals(user.getPassword())) {
			throw new RuntimeException("Invalid login credentials");
		}
		String token = jwtTokenUtil.generateToken(user);
		Map<String, String> response = new HashMap<>();
		response.put("token", token);
		return response;
	}
}
