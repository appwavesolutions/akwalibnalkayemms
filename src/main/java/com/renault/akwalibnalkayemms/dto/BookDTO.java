package com.renault.akwalibnalkayemms.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class BookDTO {
    
    private Integer id;
    private String title;
    private String definition;
    private List<ChapterDTO> chapters;
    private List<IdeaDTO> ideas;
}

