package com.ayush.terminology.namaste;

import com.ayush.terminology.namaste.model.AppUser;
import com.ayush.terminology.namaste.model.Role;
import com.ayush.terminology.namaste.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class NamasteApplication {

	public static void main(String[] args) {
		SpringApplication.run(NamasteApplication.class, args);
	}

//    @Bean
//    CommandLineRunner init(UserRepository repo, PasswordEncoder encoder) {
//        return args -> {
//            if (repo.findByUsername("admin").isEmpty()) {
//                repo.save(new AppUser(
//                        null,
//                        "admin",
//                        encoder.encode("admin123"),
//                        Role.ADMIN
//                ));
//            }
//        };
//    }

}
