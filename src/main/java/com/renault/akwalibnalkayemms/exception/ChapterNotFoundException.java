package com.renault.akwalibnalkayemms.exception;

public class ChapterNotFoundException extends RuntimeException {
    
    public ChapterNotFoundException(Integer id) {
        super("Chapter not found with id: " + id);
    }
    
    public ChapterNotFoundException(String message) {
        super(message);
    }
}

