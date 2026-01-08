package com.main.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
public class Loan {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional=false)
  private User user;

  @ManyToOne(optional=false)
  private Book book;

  @Column(nullable=false)
  private LocalDate loanDate;

  @Column(nullable=false)
  private LocalDate dueDate;

  private LocalDate returnDate;

  @Column(nullable=false)
  private double fine;

  @Enumerated(EnumType.STRING)
  @Column(nullable=false)
  private Status status = Status.ACTIVE;

  public enum Status { ACTIVE, RETURNED, OVERDUE }

  // getters/setters
}
