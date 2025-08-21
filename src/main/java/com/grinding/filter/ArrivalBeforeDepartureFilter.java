package com.grinding.filter;

import com.grinding.dto.FlightDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArrivalBeforeDepartureFilter implements FlightFilter{

@Override
public String getName(){
    return "arrival-before-departure";
}

@Override
public List<FlightDTO> filter(List<FlightDTO> flights){
    return flights.stream().filter(flight->flight.getSegments().stream().allMatch(segment->segment.getArrivalDate().isBefore(segment.getDepartureDate()))).collect(Collectors.toList());
}
}