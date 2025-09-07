package com.renault.garage.mapper;

import com.renault.garage.dto.VehicleDTO;
import com.renault.garage.model.Vehicle;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-21T21:04:25+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.4.1 (Eclipse Adoptium)"
)
@Component
public class VehicleMapperImpl implements VehicleMapper {

    @Override
    public VehicleDTO toDto(Vehicle vehicle) {
        if ( vehicle == null ) {
            return null;
        }

        VehicleDTO.VehicleDTOBuilder vehicleDTO = VehicleDTO.builder();

        vehicleDTO.id( vehicle.getId() );
        vehicleDTO.brand( vehicle.getBrand() );
        vehicleDTO.fuelType( vehicle.getFuelType() );
        vehicleDTO.manufactureYear( vehicle.getManufactureYear() );

        return vehicleDTO.build();
    }

    @Override
    public Vehicle toEntity(VehicleDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Vehicle.VehicleBuilder vehicle = Vehicle.builder();

        vehicle.brand( dto.getBrand() );
        vehicle.fuelType( dto.getFuelType() );
        vehicle.manufactureYear( dto.getManufactureYear() );

        return vehicle.build();
    }

    @Override
    public void updateEntityFromDto(Vehicle vehicle, VehicleDTO dto) {
        if ( dto == null ) {
            return;
        }

        vehicle.setBrand( dto.getBrand() );
        vehicle.setFuelType( dto.getFuelType() );
        vehicle.setManufactureYear( dto.getManufactureYear() );
    }
}
