package com.azeezat.auth.service.config;

import com.azeezat.auth.service.entity.Role;
import com.azeezat.auth.service.entity.User;
import com.azeezat.auth.service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminDataInitializer {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository,
                                        PasswordEncoder passwordEncoder){

        return args -> {

            if(!userRepository.existsByEmail("admin@banking.com")){
                User admin = new User();
                admin.setFirstName("System");
                admin.setLastName("Administrator");
                admin.setEmail("admin@banking.com");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
                System.out.println("Admin data initialized");
            }

        };
    }
}
