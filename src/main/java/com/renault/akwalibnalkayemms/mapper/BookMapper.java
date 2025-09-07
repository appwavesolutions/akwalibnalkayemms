package com.renault.akwalibnalkayemms.mapper;

import com.renault.akwalibnalkayemms.dto.BookDTO;
import com.renault.akwalibnalkayemms.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {
    
    BookDTO toDto(Book entity);
    
    Book toEntity(BookDTO dto);
    
    void updateEntityFromDto(BookDTO dto, @MappingTarget Book entity);
}
