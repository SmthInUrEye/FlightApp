package com.grinding.filter;

import com.grinding.testing.Flight;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartureBeforeNowFilter implements FlightFilter{

@Override
public List<Flight> filter(List<Flight> flights){
    LocalDateTime now=LocalDateTime.now();
    return flights.stream()
            .filter(flight->flight
                             .getSegments()
                             .stream()
                             .allMatch(segment->!segment
                                                  .getDepartureDate()
                                                  .isBefore(now)))
            .collect(Collectors.toList());
}
}