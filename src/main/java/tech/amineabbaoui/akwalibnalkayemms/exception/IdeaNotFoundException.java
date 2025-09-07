package tech.amineabbaoui.akwalibnalkayemms.exception;

public class IdeaNotFoundException extends RuntimeException {
    
    public IdeaNotFoundException(Integer id) {
        super("Idea not found with id: " + id);
    }
    
    public IdeaNotFoundException(String message) {
        super(message);
    }
}

