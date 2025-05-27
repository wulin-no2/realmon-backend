package com.realmon.common.model.mapper;

import com.realmon.common.model.dto.ObservationDTO;
import com.realmon.common.model.entity.Observation;
import com.realmon.common.model.entity.Species;
import com.realmon.common.model.entity.User;
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
public class ObservationMapperImpl implements ObservationMapper {

    @Override
    public ObservationDTO toDTO(Observation entity) {
        if ( entity == null ) {
            return null;
        }

        ObservationDTO.ObservationDTOBuilder observationDTO = ObservationDTO.builder();

        observationDTO.speciesId( entitySpeciesId( entity ) );
        observationDTO.speciesName( entitySpeciesName( entity ) );
        observationDTO.speciesIcon( entitySpeciesIcon( entity ) );
        observationDTO.userId( entityUserId( entity ) );
        observationDTO.username( entityUserUsername( entity ) );
        observationDTO.id( entity.getId() );
        observationDTO.latitude( entity.getLatitude() );
        observationDTO.longitude( entity.getLongitude() );
        observationDTO.observedAt( entity.getObservedAt() );
        observationDTO.imageUrl( entity.getImageUrl() );
        observationDTO.source( entity.getSource() );

        return observationDTO.build();
    }

    @Override
    public List<ObservationDTO> toDTOs(List<Observation> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ObservationDTO> list = new ArrayList<ObservationDTO>( entities.size() );
        for ( Observation observation : entities ) {
            list.add( toDTO( observation ) );
        }

        return list;
    }

    private String entitySpeciesId(Observation observation) {
        Species species = observation.getSpecies();
        if ( species == null ) {
            return null;
        }
        return species.getId();
    }

    private String entitySpeciesName(Observation observation) {
        Species species = observation.getSpecies();
        if ( species == null ) {
            return null;
        }
        return species.getName();
    }

    private String entitySpeciesIcon(Observation observation) {
        Species species = observation.getSpecies();
        if ( species == null ) {
            return null;
        }
        return species.getIcon();
    }

    private Long entityUserId(Observation observation) {
        User user = observation.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private String entityUserUsername(Observation observation) {
        User user = observation.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getUsername();
    }
}
