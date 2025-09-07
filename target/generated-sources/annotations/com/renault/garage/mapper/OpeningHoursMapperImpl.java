package com.renault.garage.mapper;

import com.renault.garage.dto.OpeningHoursDTO;
import com.renault.garage.model.GarageOpeningHours;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-21T21:04:25+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.4.1 (Eclipse Adoptium)"
)
@Component
public class OpeningHoursMapperImpl implements OpeningHoursMapper {

    @Override
    public OpeningHoursDTO toDTO(GarageOpeningHours openingHours) {
        if ( openingHours == null ) {
            return null;
        }

        OpeningHoursDTO.OpeningHoursDTOBuilder openingHoursDTO = OpeningHoursDTO.builder();

        openingHoursDTO.dayOfWeek( openingHours.getDayOfWeek() );

        openingHoursDTO.openingTime( getOpeningTime(openingHours) );
        openingHoursDTO.closingTime( getClosingTime(openingHours) );

        return openingHoursDTO.build();
    }
}
