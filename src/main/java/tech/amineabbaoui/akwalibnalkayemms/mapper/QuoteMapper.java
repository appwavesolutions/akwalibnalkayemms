package tech.amineabbaoui.akwalibnalkayemms.mapper;

import tech.amineabbaoui.akwalibnalkayemms.dto.QuoteDTO;
import tech.amineabbaoui.akwalibnalkayemms.model.Quote;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface QuoteMapper {
    
    QuoteDTO toDto(Quote entity);
    
    Quote toEntity(QuoteDTO dto);
    
    void updateEntityFromDto(QuoteDTO dto, @MappingTarget Quote entity);
}

