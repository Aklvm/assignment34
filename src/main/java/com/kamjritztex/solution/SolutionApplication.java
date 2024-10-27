package com.kamjritztex.solution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan("com.kamjritztex.solution.entity")
public class SolutionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolutionApplication.class, args);
	}

}
