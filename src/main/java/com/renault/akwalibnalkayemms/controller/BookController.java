package com.renault.akwalibnalkayemms.controller;

import com.renault.akwalibnalkayemms.dto.BookDTO;
import com.renault.akwalibnalkayemms.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    
    private final BookService bookService;
    
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO dto) {
        return ResponseEntity.ok(bookService.createBook(dto));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }
    
    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(bookService.getAllBooks(page, size, sortBy, sortDir));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Integer id,
            @Valid @RequestBody BookDTO dto
    ) {
        return ResponseEntity.ok(bookService.updateBook(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchBooksByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchBooksByKeyword(keyword));
    }
    
    @GetMapping("/title")
    public ResponseEntity<BookDTO> getBookByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.getBookByTitle(title));
    }
}


