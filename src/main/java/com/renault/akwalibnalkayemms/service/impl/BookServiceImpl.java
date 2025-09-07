package com.renault.akwalibnalkayemms.service.impl;

import com.renault.akwalibnalkayemms.dto.BookDTO;
import com.renault.akwalibnalkayemms.exception.BookNotFoundException;
import com.renault.akwalibnalkayemms.mapper.BookMapper;
import com.renault.akwalibnalkayemms.model.Book;
import com.renault.akwalibnalkayemms.repository.BookRepository;
import com.renault.akwalibnalkayemms.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private BookMapper bookMapper;
    
    @Override
    public BookDTO createBook(BookDTO dto) {
        Book book = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }
    
    @Override
    public BookDTO getBookById(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return bookMapper.toDto(book);
    }
    
    @Override
    public Page<BookDTO> getAllBooks(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Book> bookPage = bookRepository.findAll(pageable);
        return bookPage.map(bookMapper::toDto);
    }
    
    @Override
    public BookDTO updateBook(Integer id, BookDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookMapper.updateEntityFromDto(dto, book);
        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }
    
    @Override
    public void deleteBook(Integer id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }
    
    @Override
    public List<BookDTO> searchBooksByKeyword(String keyword) {
        return bookRepository.findByKeyword(keyword).stream()
                .map(bookMapper::toDto)
                .toList();
    }
    
    @Override
    public BookDTO getBookByTitle(String title) {
        Book book = bookRepository.findByTitleIgnoreCase(title);
        if (book == null) {
            throw new BookNotFoundException("Book with title: " + title + " not found");
        }
        return bookMapper.toDto(book);
    }
}


