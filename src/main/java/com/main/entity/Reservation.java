package com.main.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
@RequiredArgsConstructor
@Data
@Entity
public class Reservation {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional=false)
  private User user;

  @ManyToOne(optional=false)
  private Book book;

  @Column(nullable=false)
  private LocalDateTime reservedAt;

  private boolean fulfilled;

  // getters/setters
}
