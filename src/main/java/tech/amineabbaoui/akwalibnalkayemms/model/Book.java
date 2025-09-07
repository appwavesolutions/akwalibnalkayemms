package tech.amineabbaoui.akwalibnalkayemms.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"chapters", "ideas"})
@EqualsAndHashCode(exclude = {"chapters", "ideas"})
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(length = 200)
    private String title;
    
    @Column(length = 1000)
    private String definition;
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Chapter> chapters = new ArrayList<>();
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Idea> ideas = new ArrayList<>();
}

