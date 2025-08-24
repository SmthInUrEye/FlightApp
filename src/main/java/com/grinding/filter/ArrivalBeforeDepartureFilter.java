package com.grinding.filter;

import com.grinding.dto.FlightDTO;
import com.grinding.repository.FlightRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArrivalBeforeDepartureFilter implements FlightFilter{
private final FlightRepository flightRepository;

public ArrivalBeforeDepartureFilter(FlightRepository flightRepository){
    this.flightRepository=flightRepository;
}

@Override
public String getName(){
    return "arrival-before-departure";
}

@Override
public List<FlightDTO> filter(List<FlightDTO> flights){
    return flights.stream().filter(flight->flight.getSegments().stream().allMatch(segment-> !segment.getArrivalDate().isBefore(segment.getDepartureDate()))).collect(Collectors.toList());
}

@Override
public void removeInvalidFlights(List<FlightDTO> flights) {
    List<Long> toDeleteIds = flights.stream()
                              .filter(flight -> flight.getSegments().stream()
                                                 .anyMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate())))
                              .map(FlightDTO::getId)
                              .collect(Collectors.toList());

    flightRepository.deleteAllById(toDeleteIds);
}
}
