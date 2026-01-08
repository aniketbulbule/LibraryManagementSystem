package com.main.controller;

import com.main.entity.Book;
import com.main.repository.UserRepository;
import com.main.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {
  private final BookService bookService;
  private final UserRepository userRepo;

  public AdminController(BookService bookService, UserRepository userRepo) {
    this.bookService = bookService; this.userRepo = userRepo;
  }

  @GetMapping("/books")
  public String books(@RequestParam(required = false) String q, Model model) {
    model.addAttribute("books", bookService.listAll(q));
    model.addAttribute("book", new Book());
    return "admin/books";
  }

  @PostMapping("/books")
  public String addBook(@ModelAttribute Book book, @RequestParam("imageFile") MultipartFile imageFile) {
    try {
      bookService.add(book, imageFile);
      return "redirect:/admin/books";
    } catch (Exception e) {
      return "redirect:/admin/books?error=" + e.getMessage();
    }
  }

  @PostMapping("/books/{id}")
  public String updateBook(@PathVariable Long id, @ModelAttribute Book changes,
                           @RequestParam(value="imageFile", required=false) MultipartFile imageFile) {
    try {
      bookService.update(id, changes, imageFile);
      return "redirect:/admin/books";
    } catch (Exception e) {
      return "redirect:/admin/books?error=" + e.getMessage();
    }
  }

  @PostMapping("/books/{id}/delete")
  public String deleteBook(@PathVariable Long id) {
    bookService.delete(id);
    return "redirect:/admin/books";
  }

  @GetMapping("/users")
  public String users(Model model) {
    model.addAttribute("users", userRepo.findAll());
    return "admin/users";
  }
}
