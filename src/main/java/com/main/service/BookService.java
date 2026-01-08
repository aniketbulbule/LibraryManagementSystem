package com.main.service;

import com.main.entity.Book;
import com.main.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {
  private final BookRepository repo;

  @Value("${app.uploads.dir}")
  private String uploadsDir;

  public BookService(BookRepository repo) { this.repo = repo; }

  public List<Book> listAll(String q) {
    return (q == null || q.isBlank()) ? repo.findAll() : repo.findByTitleContainingIgnoreCase(q);
  }

  @Transactional
  public Book add(Book b, MultipartFile imageFile) throws IOException {
    b.setAvailableCopies(b.getTotalCopies());
    if (imageFile != null && !imageFile.isEmpty()) {
      b.setImagePath(storeImage(imageFile));
    }
    return repo.save(b);
  }
 
  @Transactional
  public Book update(Long id, Book changes, MultipartFile imageFile) throws IOException {
    Book b = repo.findById(id).orElseThrow();
    b.setTitle(changes.getTitle());
    b.setAuthor(changes.getAuthor());
    b.setIsbn(changes.getIsbn());
    b.setTotalCopies(changes.getTotalCopies());
    if (b.getAvailableCopies() > b.getTotalCopies()) {
      b.setAvailableCopies(b.getTotalCopies());
    }
    if (imageFile != null && !imageFile.isEmpty()) {
      b.setImagePath(storeImage(imageFile));
    }
    return repo.save(b);
  }

  @Transactional
  public void delete(Long id) {
    repo.deleteById(id);
  }

  public Book get(Long id) { return repo.findById(id).orElseThrow(); }

  private String storeImage(MultipartFile file) throws IOException {
    String ext = Path.of(file.getOriginalFilename()).getFileName().toString();
    String name = UUID.randomUUID().toString() + "-" + ext.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    Path uploadPath = Paths.get(uploadsDir).toAbsolutePath().normalize();
    Files.createDirectories(uploadPath);
    Path target = uploadPath.resolve(name);
    Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
    return "/uploads/" + name;
  }
}
