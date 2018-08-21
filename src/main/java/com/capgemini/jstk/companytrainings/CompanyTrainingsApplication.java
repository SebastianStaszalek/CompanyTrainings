package com.capgemini.jstk.companytrainings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CompanyTrainingsApplication {

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "mysql");
		SpringApplication.run(CompanyTrainingsApplication.class, args);
	}
}
