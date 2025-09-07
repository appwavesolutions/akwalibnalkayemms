package com.renault.akwalibnalkayemms.repository;

import com.renault.akwalibnalkayemms.model.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Integer> {
    
    List<Idea> findByBookId(Integer bookId);
    
    List<Idea> findByChapterId(Integer chapterId);
    
    List<Idea> findByBookIdAndChapterId(Integer bookId, Integer chapterId);
    
    List<Idea> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT i FROM Idea i WHERE i.book.id = :bookId AND (i.title LIKE %:keyword% OR i.description LIKE %:keyword%)")
    List<Idea> findByBookIdAndKeyword(@Param("bookId") Integer bookId, @Param("keyword") String keyword);
    
    @Query("SELECT i FROM Idea i WHERE i.chapter.id = :chapterId AND (i.title LIKE %:keyword% OR i.description LIKE %:keyword%)")
    List<Idea> findByChapterIdAndKeyword(@Param("chapterId") Integer chapterId, @Param("keyword") String keyword);
}

