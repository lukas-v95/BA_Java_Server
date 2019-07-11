package com.fhv.uebersetzer.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TestRestAPIs {
	
	@GetMapping("/api/test/user")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public String userAccess() {
		return ">>> User Contents!";
	}

	@GetMapping("/api/test/fhemp")
	@PreAuthorize("hasRole('FHEmp') or hasRole('ADMIN')")
	public String projectManagementAccess() {
		return ">>> FH Employee Board";
	}
	
	@GetMapping("/api/test/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return ">>> Admin Contents";
	}

	@GetMapping("/api/test/test")
	@PreAuthorize("hasRole('FHEmp') or hasRole('ADMIN')")
	public String testAccess() {
		System.out.println("Geklappt!!!");
		return "Es hat geklappt!!!!";
	}

	@GetMapping("/foo")
	public void foo(){
			System.out.println("Hello there!");

	}



}
