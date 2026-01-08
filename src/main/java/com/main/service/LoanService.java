package com.main.service;

import com.main.entity.Book;
import com.main.entity.Loan;
import com.main.entity.Reservation;
import com.main.entity.User;
import com.main.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanService {
  private final LoanRepository loanRepo;
  private final BookRepository bookRepo;
  private final UserRepository userRepo;
  private final ReservationRepository reservationRepo;

  public LoanService(LoanRepository loanRepo, BookRepository bookRepo, UserRepository userRepo, ReservationRepository reservationRepo) {
    this.loanRepo = loanRepo; this.bookRepo = bookRepo; this.userRepo = userRepo; this.reservationRepo = reservationRepo;
  }

  @Transactional
  public Loan borrowBook(Long userId, Long bookId) {
    Book book = bookRepo.findById(bookId).orElseThrow();
    if (book.getAvailableCopies() <= 0) throw new IllegalStateException("No available copies");

    List<Reservation> queue = reservationRepo.findByBookIdAndFulfilledFalse(bookId);
    if (!queue.isEmpty()) {
      Reservation next = queue.get(0);
      if (!next.getUser().getId().equals(userId)) {
        throw new IllegalStateException("Book reserved by another user, please wait");
      }
      next.setFulfilled(true);
      reservationRepo.save(next);
    }

    User user = userRepo.findById(userId).orElseThrow();
    Loan loan = new Loan();
    loan.setUser(user);
    loan.setBook(book);
    loan.setLoanDate(LocalDate.now());
    loan.setDueDate(LocalDate.now().plusDays(14));
    loan.setFine(0.0);
    loan.setStatus(Loan.Status.ACTIVE);

    book.setAvailableCopies(book.getAvailableCopies() - 1);
    bookRepo.save(book);

    return loanRepo.save(loan);
  }

  @Transactional
  public Loan returnBook(Long loanId) {
    Loan loan = loanRepo.findById(loanId).orElseThrow();
    if (loan.getStatus() != Loan.Status.ACTIVE) throw new IllegalStateException("Loan already closed");

    loan.setReturnDate(LocalDate.now());
    long overdueDays = Math.max(0, ChronoUnit.DAYS.between(loan.getDueDate(), loan.getReturnDate()));
    double finePerDay = 5.0; // INR/day
    loan.setFine(overdueDays * finePerDay);
    loan.setStatus(overdueDays > 0 ? Loan.Status.OVERDUE : Loan.Status.RETURNED);

    Book book = loan.getBook();
    book.setAvailableCopies(book.getAvailableCopies() + 1);
    bookRepo.save(book);

    return loanRepo.save(loan);
  }

  public List<Loan> activeLoansForUser(Long userId) {
    return loanRepo.findByUserIdAndStatus(userId, Loan.Status.ACTIVE);
  }

  public List<Loan> overdueLoans() {
    return loanRepo.findByStatus(Loan.Status.OVERDUE);
  }
}
