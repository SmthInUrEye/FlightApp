package com.grinding.mapper;

import com.grinding.entity.FlightEntity;
import com.grinding.entity.SegmentEntity;
import com.grinding.testing.Flight;
import com.grinding.testing.Segment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FlightMapper {

public Flight toModel(FlightEntity entity) {
    List<Segment> segments = entity.getSegments().stream()
                              .map(this::toModel)
                              .collect(Collectors.toList());
    return new Flight(segments);
}

public FlightEntity toEntity(Flight model) {
    FlightEntity entity = new FlightEntity();
    List<SegmentEntity> segments = model.getSegments().stream()
                                    .map(this::toEntity)
                                    .collect(Collectors.toList());
    segments.forEach(segment -> segment.setFlight(entity));
    entity.setSegments(segments);

    return entity;
}

private Segment toModel(SegmentEntity entity) {
    return new Segment(entity.getDepartureDate(), entity.getArrivalDate());
}

private SegmentEntity toEntity(Segment model) {
    SegmentEntity entity = new SegmentEntity();
    entity.setDepartureDate(model.getDepartureDate());
    entity.setArrivalDate(model.getArrivalDate());
    return entity;
}

}
