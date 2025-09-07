package com.renault.akwalibnalkayemms.controller;

import com.renault.akwalibnalkayemms.dto.QuoteDTO;
import com.renault.akwalibnalkayemms.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {
    
    private final QuoteService quoteService;
    
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }
    
    @PostMapping
    public ResponseEntity<QuoteDTO> createQuote(@Valid @RequestBody QuoteDTO dto) {
        return ResponseEntity.ok(quoteService.createQuote(dto));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<QuoteDTO> getQuoteById(@PathVariable Integer id) {
        return ResponseEntity.ok(quoteService.getQuoteById(id));
    }
    
    @GetMapping
    public ResponseEntity<Page<QuoteDTO>> getAllQuotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(quoteService.getAllQuotes(page, size, sortBy, sortDir));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<QuoteDTO> updateQuote(
            @PathVariable Integer id,
            @Valid @RequestBody QuoteDTO dto
    ) {
        return ResponseEntity.ok(quoteService.updateQuote(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuote(@PathVariable Integer id) {
        quoteService.deleteQuote(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<QuoteDTO>> searchQuotesByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(quoteService.searchQuotesByKeyword(keyword));
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<QuoteDTO>> getQuotesByType(@PathVariable Integer type) {
        return ResponseEntity.ok(quoteService.getQuotesByType(type));
    }
    
    @GetMapping("/title")
    public ResponseEntity<List<QuoteDTO>> getQuotesByTitle(@RequestParam String title) {
        return ResponseEntity.ok(quoteService.getQuotesByTitle(title));
    }
}


