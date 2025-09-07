package com.renault.akwalibnalkayemms.service;

import com.renault.akwalibnalkayemms.dto.QuoteDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuoteService {
    
    QuoteDTO createQuote(QuoteDTO dto);
    
    QuoteDTO getQuoteById(Integer id);
    
    Page<QuoteDTO> getAllQuotes(int page, int size, String sortBy, String sortDir);
    
    QuoteDTO updateQuote(Integer id, QuoteDTO dto);
    
    void deleteQuote(Integer id);
    
    List<QuoteDTO> searchQuotesByKeyword(String keyword);
    
    List<QuoteDTO> getQuotesByType(Integer type);
    
    List<QuoteDTO> getQuotesByTitle(String title);
}



