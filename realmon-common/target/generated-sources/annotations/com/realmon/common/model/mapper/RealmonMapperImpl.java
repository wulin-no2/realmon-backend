package com.realmon.common.model.mapper;

import com.realmon.common.model.dto.RealmonDTO;
import com.realmon.common.model.entity.Realmon;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-28T00:47:45+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class RealmonMapperImpl implements RealmonMapper {

    @Override
    public RealmonDTO toDTO(Realmon entity) {
        if ( entity == null ) {
            return null;
        }

        RealmonDTO realmonDTO = new RealmonDTO();

        realmonDTO.setId( entity.getId() );
        realmonDTO.setName( entity.getName() );
        realmonDTO.setIcon( entity.getIcon() );
        realmonDTO.setLatitude( entity.getLatitude() );
        realmonDTO.setLongitude( entity.getLongitude() );
        realmonDTO.setImageUrl( entity.getImageUrl() );
        realmonDTO.setWikiUrl( entity.getWikiUrl() );
        realmonDTO.setSpecies( entity.getSpecies() );

        return realmonDTO;
    }

    @Override
    public List<RealmonDTO> toDTOs(List<Realmon> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<RealmonDTO> list = new ArrayList<RealmonDTO>( entityList.size() );
        for ( Realmon realmon : entityList ) {
            list.add( toDTO( realmon ) );
        }

        return list;
    }
}
