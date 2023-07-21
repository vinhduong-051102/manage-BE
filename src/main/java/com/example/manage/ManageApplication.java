package com.example.manage;

import com.example.manage.request.RegisterRequest;
import com.example.manage.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.example.manage.model.ERole.ADMIN;
import static com.example.manage.model.ERole.TEACHER;

@SpringBootApplication
public class ManageApplication {
	public static void main(String[] args) {
		SpringApplication.run(ManageApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.email("admin@gmail.com")
					.password("Vinh-051102")
					.role(ADMIN)
					.isActive(true)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());
		};
	}
}
