package com.renault.akwalibnalkayemms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Quote")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Quote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(length = 1000)
    private String content;
    
    @Column(length = 500)
    private String description;
    
    @Column(length = 1000)
    private String example;
    
    @Column(length = 1000)
    private String text;
    
    @Column(length = 200)
    private String title;
    
    @Column
    private Integer type;
}

