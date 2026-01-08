package com.main.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Book {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false)
  private String title;

  private String author;
  private String isbn;

  @Column(nullable=false)
  private int totalCopies;

  @Column(nullable=false)
  private int availableCopies;

  // store image filename/path served via /uploads/**
  private String imagePath;

  public Book() {}
  public Book(String title, String author, String isbn, int totalCopies, int availableCopies, String imagePath) {
    this.title = title; this.author = author; this.isbn = isbn;
    this.totalCopies = totalCopies; this.availableCopies = availableCopies; this.imagePath = imagePath;
  }

  // getters/setters
}
