package tech.amineabbaoui.akwalibnalkayemms.repository;

import tech.amineabbaoui.akwalibnalkayemms.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {
    
    List<Chapter> findByBookId(Integer bookId);
    
    List<Chapter> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT c FROM Chapter c WHERE c.book.id = :bookId AND (c.title LIKE %:keyword%)")
    List<Chapter> findByBookIdAndKeyword(@Param("bookId") Integer bookId, @Param("keyword") String keyword);
    
    Chapter findByBookIdAndTitleIgnoreCase(Integer bookId, String title);
}

