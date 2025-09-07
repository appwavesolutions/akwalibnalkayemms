package com.renault.garage.mapper;

import com.renault.garage.dto.GarageVehicleDTO;
import com.renault.garage.model.Garage;
import com.renault.garage.model.GarageVehicle;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-21T21:04:25+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.4.1 (Eclipse Adoptium)"
)
@Component
public class GarageVehicleMapperImpl implements GarageVehicleMapper {

    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    public GarageVehicleDTO toDTO(GarageVehicle garageVehicle) {
        if ( garageVehicle == null ) {
            return null;
        }

        GarageVehicleDTO.GarageVehicleDTOBuilder garageVehicleDTO = GarageVehicleDTO.builder();

        garageVehicleDTO.vehicle( vehicleMapper.toDto( garageVehicle.getVehicle() ) );
        garageVehicleDTO.quantity( garageVehicle.getQuantity() );

        return garageVehicleDTO.build();
    }

    @Override
    public GarageVehicle toEntity(GarageVehicleDTO dto, Garage garage) {
        if ( dto == null && garage == null ) {
            return null;
        }

        GarageVehicle.GarageVehicleBuilder garageVehicle = GarageVehicle.builder();

        garageVehicle.garage( garage );

        return garageVehicle.build();
    }
}
