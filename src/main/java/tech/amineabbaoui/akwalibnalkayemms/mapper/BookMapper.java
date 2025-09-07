package tech.amineabbaoui.akwalibnalkayemms.mapper;

import tech.amineabbaoui.akwalibnalkayemms.dto.BookDTO;
import tech.amineabbaoui.akwalibnalkayemms.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {
    
    BookDTO toDto(Book entity);
    
    Book toEntity(BookDTO dto);
    
    void updateEntityFromDto(BookDTO dto, @MappingTarget Book entity);
}
