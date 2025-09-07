package com.renault.akwalibnalkayemms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class QuoteDTO {
    
    private Integer id;
    private String content;
    private String description;
    private String example;
    private String text;
    private String title;
    private Integer type;
}

