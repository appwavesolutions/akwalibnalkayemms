package com.renault.akwalibnalkayemms.service.impl;

import com.renault.akwalibnalkayemms.dto.QuoteDTO;
import com.renault.akwalibnalkayemms.exception.QuoteNotFoundException;
import com.renault.akwalibnalkayemms.mapper.QuoteMapper;
import com.renault.akwalibnalkayemms.model.Quote;
import com.renault.akwalibnalkayemms.repository.QuoteRepository;
import com.renault.akwalibnalkayemms.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuoteServiceImpl implements QuoteService {
    
    @Autowired
    private QuoteRepository quoteRepository;
    
    @Autowired
    private QuoteMapper quoteMapper;
    
    @Override
    public QuoteDTO createQuote(QuoteDTO dto) {
        Quote quote = quoteMapper.toEntity(dto);
        Quote savedQuote = quoteRepository.save(quote);
        return quoteMapper.toDto(savedQuote);
    }
    
    @Override
    public QuoteDTO getQuoteById(Integer id) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new QuoteNotFoundException(id));
        return quoteMapper.toDto(quote);
    }
    
    @Override
    public Page<QuoteDTO> getAllQuotes(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Quote> quotePage = quoteRepository.findAll(pageable);
        return quotePage.map(quoteMapper::toDto);
    }
    
    @Override
    public QuoteDTO updateQuote(Integer id, QuoteDTO dto) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new QuoteNotFoundException(id));
        quoteMapper.updateEntityFromDto(dto, quote);
        Quote updatedQuote = quoteRepository.save(quote);
        return quoteMapper.toDto(updatedQuote);
    }
    
    @Override
    public void deleteQuote(Integer id) {
        if (!quoteRepository.existsById(id)) {
            throw new QuoteNotFoundException(id);
        }
        quoteRepository.deleteById(id);
    }
    
    @Override
    public List<QuoteDTO> searchQuotesByKeyword(String keyword) {
        return quoteRepository.findByKeyword(keyword).stream()
                .map(quoteMapper::toDto)
                .toList();
    }
    
    @Override
    public List<QuoteDTO> getQuotesByType(Integer type) {
        return quoteRepository.findByType(type).stream()
                .map(quoteMapper::toDto)
                .toList();
    }
    
    @Override
    public List<QuoteDTO> getQuotesByTitle(String title) {
        return quoteRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(quoteMapper::toDto)
                .toList();
    }
}



