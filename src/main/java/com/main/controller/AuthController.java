package com.main.controller;

import com.main.dto.RegisterUserDto;
import com.main.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) { this.userService = userService; }

  @GetMapping("/login")
  public String login() { return "login"; }

  @GetMapping("/register")
  public String registerForm(Model model) {
    model.addAttribute("user", new RegisterUserDto());
    return "register";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute("user") @Valid RegisterUserDto dto, Model model) {
    try {
      userService.register(dto);
      return "redirect:/login?registered";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      return "register";
    }
  }
}
