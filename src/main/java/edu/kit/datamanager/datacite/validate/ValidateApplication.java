package edu.kit.datamanager.datacite.validate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class ValidateApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidateApplication.class, args);
	}
}
