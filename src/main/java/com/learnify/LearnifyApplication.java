package com.learnify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.learnify.repository")
@EntityScan("com.learnify.model")
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class LearnifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnifyApplication.class, args);
	}

}
