package com.renault.akwalibnalkayemms.repository;

import com.renault.akwalibnalkayemms.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Integer> {
    
    List<Quote> findByTitleContainingIgnoreCase(String title);
    
    List<Quote> findByType(Integer type);
    
    @Query("SELECT q FROM Quote q WHERE q.content LIKE %:keyword% OR q.text LIKE %:keyword% OR q.title LIKE %:keyword%")
    List<Quote> findByKeyword(@Param("keyword") String keyword);
    
    List<Quote> findByTitleIgnoreCase(String title);
}

