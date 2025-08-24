package com.grinding.filter;

import com.grinding.dto.FlightDTO;
import com.grinding.repository.FlightRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartureBeforeNowFilter implements FlightFilter{
private final FlightRepository flightRepository;

public DepartureBeforeNowFilter(FlightRepository flightRepository){
    this.flightRepository=flightRepository;
}

@Override
public String getName(){
    return "departure-before-now";
}

@Override
public List<FlightDTO> filter(List<FlightDTO> flights){
    LocalDateTime now=LocalDateTime.now();
    return flights.stream().filter(flight->flight.getSegments().stream().allMatch(segment-> !segment.getDepartureDate().isBefore(now))).collect(Collectors.toList());
}
@Override
public void removeInvalidFlights(List<FlightDTO> flights) {
    LocalDateTime now = LocalDateTime.now();

    List<Long> toDeleteIds = flights.stream()
                              .filter(flight -> flight.getSegments().stream()
                                                 .anyMatch(segment -> segment.getDepartureDate().isBefore(now)))
                              .map(FlightDTO::getId)
                              .collect(Collectors.toList());

    flightRepository.deleteAllById(toDeleteIds);
}
}
