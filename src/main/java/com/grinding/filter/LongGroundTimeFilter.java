package com.grinding.filter;

import com.grinding.testing.Flight;
import com.grinding.testing.Segment;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LongGroundTimeFilter implements FlightFilter{

@Override
public List<Flight> filter(List<Flight> flights){

    return flights.stream()
            .filter(flight->{
        List<Segment> segments=flight
                                .getSegments();

        long totalGroundTime=0;

        for(int i=0;i<segments.size()-1;i++){
            Segment current=segments.get(i);
            Segment next=segments.get(i+1);
            long groundTime=Duration.between(current.getArrivalDate(),next.getDepartureDate()).toMinutes();
            totalGroundTime+=groundTime;
        }
        return totalGroundTime<=120;
    })
            .collect(Collectors.toList());
}
}