package com.main.service;

import com.main.dto.RegisterUserDto;
import com.main.entity.Role;
import com.main.entity.User;
import com.main.repository.RoleRepository;
import com.main.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserService {
  private final UserRepository userRepo;
  private final RoleRepository roleRepo;
  private final PasswordEncoder encoder;

  public UserService(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder) {
    this.userRepo = userRepo; this.roleRepo = roleRepo; this.encoder = encoder;
  }
 
  @Transactional
  public User register(RegisterUserDto dto) {
    if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
      throw new IllegalArgumentException("Username already exists");
    }
    if (userRepo.existsByEmail(dto.getEmail())) {
      throw new IllegalArgumentException("Email already exists");
    }
    Role userRole = roleRepo.findByName("ROLE_USER").orElseThrow(() -> new IllegalStateException("ROLE_USER missing"));

    User user = new User();
    user.setUsername(dto.getUsername());
    user.setPassword(encoder.encode(dto.getPassword()));
    user.setFullName(dto.getFullName());
    user.setEmail(dto.getEmail());
    user.setEnabled(true);
    user.setRoles(Set.of(userRole));
    return userRepo.save(user);
  }
}
