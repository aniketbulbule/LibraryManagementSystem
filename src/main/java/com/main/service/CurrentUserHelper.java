package com.main.service;

import com.main.entity.User;
import com.main.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserHelper {
  private final UserRepository userRepo;
  public CurrentUserHelper(UserRepository userRepo) { this.userRepo = userRepo; }
  public Long userId(String username) {
    User u = userRepo.findByUsername(username).orElseThrow();
    return u.getId();
  }
}
