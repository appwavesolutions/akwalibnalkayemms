package com.renault.akwalibnalkayemms.repository;

import com.renault.akwalibnalkayemms.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword% OR b.definition LIKE %:keyword%")
    List<Book> findByKeyword(@Param("keyword") String keyword);
    
    Book findByTitleIgnoreCase(String title);
}

