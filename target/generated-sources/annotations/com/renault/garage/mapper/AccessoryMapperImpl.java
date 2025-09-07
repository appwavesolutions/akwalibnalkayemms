package com.renault.garage.mapper;

import com.renault.garage.dto.AccessoryDTO;
import com.renault.garage.model.Accessory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-21T21:04:25+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.4.1 (Eclipse Adoptium)"
)
@Component
public class AccessoryMapperImpl implements AccessoryMapper {

    @Override
    public AccessoryDTO toDto(Accessory accessory) {
        if ( accessory == null ) {
            return null;
        }

        AccessoryDTO.AccessoryDTOBuilder accessoryDTO = AccessoryDTO.builder();

        accessoryDTO.id( accessory.getId() );
        accessoryDTO.name( accessory.getName() );
        accessoryDTO.description( accessory.getDescription() );
        accessoryDTO.price( accessory.getPrice() );
        accessoryDTO.type( accessory.getType() );

        return accessoryDTO.build();
    }

    @Override
    public Accessory toEntity(AccessoryDTO accessoryDTO) {
        if ( accessoryDTO == null ) {
            return null;
        }

        Accessory.AccessoryBuilder accessory = Accessory.builder();

        accessory.id( accessoryDTO.getId() );
        accessory.name( accessoryDTO.getName() );
        accessory.description( accessoryDTO.getDescription() );
        accessory.price( accessoryDTO.getPrice() );
        accessory.type( accessoryDTO.getType() );

        return accessory.build();
    }

    @Override
    public List<AccessoryDTO> toDtoList(List<Accessory> accessories) {
        if ( accessories == null ) {
            return null;
        }

        List<AccessoryDTO> list = new ArrayList<AccessoryDTO>( accessories.size() );
        for ( Accessory accessory : accessories ) {
            list.add( toDto( accessory ) );
        }

        return list;
    }
}
