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
public class ChapterDTO {
    
    private Integer id;
    private String title;
    private Integer bookId;
    private String bookTitle;
    private List<IdeaDTO> ideas;
}

