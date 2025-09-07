package com.renault.garage.mapper;

import com.renault.garage.dto.GarageDTO;
import com.renault.garage.dto.GarageVehicleDTO;
import com.renault.garage.dto.OpeningHoursDTO;
import com.renault.garage.model.Garage;
import com.renault.garage.model.GarageOpeningHours;
import com.renault.garage.model.GarageVehicle;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-21T21:04:25+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.4.1 (Eclipse Adoptium)"
)
@Component
public class GarageMapperImpl implements GarageMapper {

    @Autowired
    private GarageVehicleMapper garageVehicleMapper;
    @Autowired
    private OpeningHoursMapper openingHoursMapper;

    @Override
    public GarageDTO toDTO(Garage garage) {
        if ( garage == null ) {
            return null;
        }

        GarageDTO.GarageDTOBuilder garageDTO = GarageDTO.builder();

        garageDTO.id( garage.getId() );
        garageDTO.name( garage.getName() );
        garageDTO.address( garage.getAddress() );
        garageDTO.phone( garage.getPhone() );
        garageDTO.email( garage.getEmail() );
        garageDTO.openingHours( garageOpeningHoursListToOpeningHoursDTOList( garage.getOpeningHours() ) );
        garageDTO.garageVehicles( garageVehicleListToGarageVehicleDTOList( garage.getGarageVehicles() ) );

        return garageDTO.build();
    }

    protected List<OpeningHoursDTO> garageOpeningHoursListToOpeningHoursDTOList(List<GarageOpeningHours> list) {
        if ( list == null ) {
            return null;
        }

        List<OpeningHoursDTO> list1 = new ArrayList<OpeningHoursDTO>( list.size() );
        for ( GarageOpeningHours garageOpeningHours : list ) {
            list1.add( openingHoursMapper.toDTO( garageOpeningHours ) );
        }

        return list1;
    }

    protected List<GarageVehicleDTO> garageVehicleListToGarageVehicleDTOList(List<GarageVehicle> list) {
        if ( list == null ) {
            return null;
        }

        List<GarageVehicleDTO> list1 = new ArrayList<GarageVehicleDTO>( list.size() );
        for ( GarageVehicle garageVehicle : list ) {
            list1.add( garageVehicleMapper.toDTO( garageVehicle ) );
        }

        return list1;
    }
}
