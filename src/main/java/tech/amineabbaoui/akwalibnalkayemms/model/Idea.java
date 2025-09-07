package tech.amineabbaoui.akwalibnalkayemms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Idea")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"book", "chapter"})
@EqualsAndHashCode(exclude = {"book", "chapter"})
public class Idea {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(length = 200)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
}

