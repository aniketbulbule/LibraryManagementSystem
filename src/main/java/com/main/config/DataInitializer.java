package com.main.config;

import com.main.entity.Book;
import com.main.entity.Role;
import com.main.entity.User;
import com.main.repository.BookRepository;
import com.main.repository.RoleRepository;
import com.main.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(RoleRepository roleRepo,
                           UserRepository userRepo,
                           BookRepository bookRepo,
                           PasswordEncoder encoder) {
        return args -> {
            // Ensure roles exist first
            Role adminRole = roleRepo.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepo.save(new Role("ROLE_ADMIN")));
            Role userRole = roleRepo.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepo.save(new Role("ROLE_USER")));

            // Create admin user only if not present
            if (userRepo.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setEmail("admin@example.com");
                admin.setFullName("System Admin");
                admin.setEnabled(true);
                admin.setRoles(Set.of(adminRole, userRole));
                userRepo.save(admin);
            }

            // Seed sample books
            if (bookRepo.count() == 0) {
                bookRepo.save(new Book("Clean Code", "Robert C. Martin",
                        "9780132350884", 5, 5, null));
                bookRepo.save(new Book("Effective Java", "Joshua Bloch",
                        "9780134685991", 3, 3, null));
            }
        };
    }
}
