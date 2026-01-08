package com.main.controller;

import com.main.service.BookService;
import com.main.service.CurrentUserHelper;
import com.main.service.LoanService;
import com.main.service.ReservationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
  private final BookService bookService;
  private final LoanService loanService;
  private final ReservationService reservationService;
  private final CurrentUserHelper currentUser;

  public UserController(BookService bookService, LoanService loanService,
                        ReservationService reservationService, CurrentUserHelper currentUser) {
    this.bookService = bookService; this.loanService = loanService;
    this.reservationService = reservationService; this.currentUser = currentUser;
  }

  @GetMapping("/")
  public String home() { return "home"; }

  @GetMapping("/books")
  public String books(@RequestParam(required = false) String q, Model model) {
    model.addAttribute("books", bookService.listAll(q));
    return "books";
  }

  @GetMapping("/user/loans")
  public String myLoans(Authentication auth, Model model) {
    Long userId = currentUser.userId(auth.getName());
    model.addAttribute("loans", loanService.activeLoansForUser(userId));
    return "user/loans";
  }

  @PostMapping("/user/borrow/{bookId}")
  public String borrow(@PathVariable Long bookId, Authentication auth, Model model) {
    try {
      Long userId = currentUser.userId(auth.getName());
      loanService.borrowBook(userId, bookId);
      return "redirect:/user/loans";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("books", bookService.listAll(null));
      return "books";
    }
  }

  @PostMapping("/user/return/{loanId}")
  public String returnBook(@PathVariable Long loanId) {
    loanService.returnBook(loanId);
    return "redirect:/user/loans";
  }

  @PostMapping("/user/reserve/{bookId}")
  public String reserve(@PathVariable Long bookId, Authentication auth, Model model) {
    try {
      Long userId = currentUser.userId(auth.getName());
      reservationService.reserve(userId, bookId);
      return "redirect:/books";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("books", bookService.listAll(null));
      return "books";
    }
  }
}
