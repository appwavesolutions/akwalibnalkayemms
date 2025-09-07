package com.renault.akwalibnalkayemms.service;

import com.renault.akwalibnalkayemms.dto.BookDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {
    
    BookDTO createBook(BookDTO dto);
    
    BookDTO getBookById(Integer id);
    
    Page<BookDTO> getAllBooks(int page, int size, String sortBy, String sortDir);
    
    BookDTO updateBook(Integer id, BookDTO dto);
    
    void deleteBook(Integer id);
    
    List<BookDTO> searchBooksByKeyword(String keyword);
    
    BookDTO getBookByTitle(String title);
}


