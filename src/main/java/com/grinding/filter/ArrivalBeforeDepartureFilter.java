package com.grinding.filter;

import com.grinding.testing.Flight;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArrivalBeforeDepartureFilter implements FlightFilter {

@Override
public List<Flight> filter(List<Flight> flights) {
    return flights.stream()
            .filter(flight -> flight.getSegments()
                               .stream()
                               .allMatch(segment -> !segment
                                                      .getArrivalDate()
                                                      .isBefore(segment
                                                                 .getDepartureDate())))
            .collect(Collectors.toList());
}
}