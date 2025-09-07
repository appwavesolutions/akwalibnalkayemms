package com.renault.akwalibnalkayemms.exception;

public class QuoteNotFoundException extends RuntimeException {
    
    public QuoteNotFoundException(Integer id) {
        super("Quote not found with id: " + id);
    }
    
    public QuoteNotFoundException(String message) {
        super(message);
    }
}

