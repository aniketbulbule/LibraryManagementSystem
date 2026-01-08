package com.main.service;

import com.main.entity.Book;
import com.main.entity.Reservation;
import com.main.entity.User;
import com.main.repository.BookRepository;
import com.main.repository.ReservationRepository;
import com.main.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
  private final ReservationRepository repo;
  private final UserRepository userRepo;
  private final BookRepository bookRepo;

  public ReservationService(ReservationRepository repo, UserRepository userRepo, BookRepository bookRepo) {
    this.repo = repo; this.userRepo = userRepo; this.bookRepo = bookRepo;
  }

  @Transactional
  public Reservation reserve(Long userId, Long bookId) {
    Book book = bookRepo.findById(bookId).orElseThrow();
    if (book.getAvailableCopies() > 0) throw new IllegalStateException("Copies available, no need to reserve");
    User user = userRepo.findById(userId).orElseThrow();

    Reservation r = new Reservation();
    r.setUser(user);
    r.setBook(book);
    r.setReservedAt(LocalDateTime.now());
    r.setFulfilled(false);
    return repo.save(r);
  }

  public List<Reservation> myReservations(Long userId) {
    return repo.findByUserIdAndFulfilledFalse(userId);
  }
}
