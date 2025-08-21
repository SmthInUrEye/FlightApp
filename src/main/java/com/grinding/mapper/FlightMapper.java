package com.grinding.mapper;

import com.grinding.dto.FlightDTO;
import com.grinding.dto.SegmentDTO;
import com.grinding.entity.FlightEntity;
import com.grinding.entity.SegmentEntity;
import com.grinding.testing.Flight;
import com.grinding.testing.Segment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FlightMapper{

// Entity -> Model
public Flight toModel(FlightEntity entity){
    List<Segment> segments=entity.getSegments().stream().map(this::toSegmentModel).collect(Collectors.toList());
    return new Flight(segments);
}

// Model -> Entity
public FlightEntity toEntity(Flight flight){
    FlightEntity entity=new FlightEntity();
    List<SegmentEntity> segmentEntities=flight.getSegments().stream().map(this::toSegmentEntity).collect(Collectors.toList());
    entity.setSegments(segmentEntities);
    return entity;
}

// Model -> DTO
public FlightDTO toDTO(Flight flight){
    List<SegmentDTO> segmentDTOs=flight.getSegments().stream().map(this::toSegmentDTO).collect(Collectors.toList());
    return new FlightDTO(segmentDTOs);
}

// DTO -> Model
public Flight toModel(FlightDTO dto){
    List<Segment> segments=dto.getSegments().stream().map(this::toSegmentModel).collect(Collectors.toList());
    return new Flight(segments);
}

// Entity -> DTO
public FlightDTO toDTO(FlightEntity entity){
    List<SegmentDTO> segmentDTOs=entity.getSegments().stream().map(this::toSegmentDTO).collect(Collectors.toList());
    return new FlightDTO(entity.getId(),segmentDTOs);
}

// DTO -> Entity
public FlightEntity toEntity(FlightDTO dto){
    FlightEntity entity=new FlightEntity();
    entity.setId(dto.getId());

    List<SegmentEntity> segmentEntities=dto.getSegments().stream().map(segmentDto->{
        SegmentEntity segmentEntity=toSegmentEntity(segmentDto);
        segmentEntity.setFlight(entity);
        return segmentEntity;
    }).collect(Collectors.toList());

    entity.setSegments(segmentEntities);

    return entity;
}

private Segment toSegmentModel(SegmentEntity entity){
    return new Segment(entity.getDepartureDate(),entity.getArrivalDate());
}

private SegmentEntity toSegmentEntity(Segment segment){
    SegmentEntity entity=new SegmentEntity();
    entity.setDepartureDate(segment.getDepartureDate());
    entity.setArrivalDate(segment.getArrivalDate());
    return entity;
}

private SegmentDTO toSegmentDTO(Segment segment){
    SegmentDTO dto=new SegmentDTO();
    dto.setDepartureDate(segment.getDepartureDate());
    dto.setArrivalDate(segment.getArrivalDate());
    return dto;
}

private Segment toSegmentModel(SegmentDTO dto){
    return new Segment(dto.getDepartureDate(),dto.getArrivalDate());
}

private SegmentEntity toSegmentEntity(SegmentDTO dto){
    SegmentEntity entity=new SegmentEntity();
    entity.setDepartureDate(dto.getDepartureDate());
    entity.setArrivalDate(dto.getArrivalDate());
    return entity;
}

private SegmentDTO toSegmentDTO(SegmentEntity entity){
    SegmentDTO dto=new SegmentDTO();
    dto.setId(entity.getId());
    dto.setDepartureDate(entity.getDepartureDate());
    dto.setArrivalDate(entity.getArrivalDate());
    return dto;
}
}
