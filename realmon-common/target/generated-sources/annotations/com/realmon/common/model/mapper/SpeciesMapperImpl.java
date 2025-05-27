package com.realmon.common.model.mapper;

import com.realmon.common.model.dto.SpeciesDTO;
import com.realmon.common.model.entity.Species;
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
public class SpeciesMapperImpl implements SpeciesMapper {

    @Override
    public SpeciesDTO toDTO(Species entity) {
        if ( entity == null ) {
            return null;
        }

        SpeciesDTO.SpeciesDTOBuilder speciesDTO = SpeciesDTO.builder();

        speciesDTO.id( entity.getId() );
        speciesDTO.name( entity.getName() );
        speciesDTO.scientificName( entity.getScientificName() );
        speciesDTO.wikiUrl( entity.getWikiUrl() );
        speciesDTO.category( entity.getCategory() );

        speciesDTO.icon( entity.getIcon() );

        return speciesDTO.build();
    }

    @Override
    public List<SpeciesDTO> toDTOs(List<Species> entities) {
        if ( entities == null ) {
            return null;
        }

        List<SpeciesDTO> list = new ArrayList<SpeciesDTO>( entities.size() );
        for ( Species species : entities ) {
            list.add( toDTO( species ) );
        }

        return list;
    }

    @Override
    public Species toEntity(SpeciesDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Species.SpeciesBuilder species = Species.builder();

        species.id( dto.getId() );
        species.name( dto.getName() );
        species.scientificName( dto.getScientificName() );
        species.wikiUrl( dto.getWikiUrl() );
        species.category( dto.getCategory() );

        return species.build();
    }
}
