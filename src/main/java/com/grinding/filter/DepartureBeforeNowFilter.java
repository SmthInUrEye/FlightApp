package com.grinding.filter;

import com.grinding.dto.FlightDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartureBeforeNowFilter implements FlightFilter{

@Override
public String getName(){
    return "departure-before-now";
}

@Override
public List<FlightDTO> filter(List<FlightDTO> flights){
    LocalDateTime now=LocalDateTime.now();
    return flights.stream().filter(flight->flight.getSegments().stream().allMatch(segment->segment.getDepartureDate().isBefore(now))).collect(Collectors.toList());
}
}