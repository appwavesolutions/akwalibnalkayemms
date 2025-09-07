package com.renault.akwalibnalkayemms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class IdeaDTO {
    
    private Integer id;
    private String title;
    private String description;
    private Integer bookId;
    private String bookTitle;
    private Integer chapterId;
    private String chapterTitle;
}

