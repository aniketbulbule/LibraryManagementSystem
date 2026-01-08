package com.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByTitleContainingIgnoreCase(String title);

	Optional<Book> findByIsbn(String isbn);
} 