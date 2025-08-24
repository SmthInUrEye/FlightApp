package com.grinding.filter;

import com.grinding.dto.FlightDTO;
import com.grinding.dto.SegmentDTO;
import com.grinding.repository.FlightRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LongGroundTimeFilter implements FlightFilter{
private final FlightRepository flightRepository;

public LongGroundTimeFilter(FlightRepository flightRepository){
    this.flightRepository=flightRepository;
}

@Override
public String getName(){
    return "long-ground-time";
}

@Override
public List<FlightDTO> filter(List<FlightDTO> flights){

    return flights.stream().filter(flight->{
        List<SegmentDTO> segments=flight.getSegments();

        long totalGroundTime=0;

        for(int i=0;i<segments.size()-1;i++){

            SegmentDTO current=segments.get(i);
            SegmentDTO next=segments.get(i+1);

            long groundTime=Duration.between(current.getArrivalDate(),next.getDepartureDate()).toMinutes();

            if(groundTime<0){
                return false;
            }

            totalGroundTime+=groundTime;
        }
        return totalGroundTime<=120;
    }).collect(Collectors.toList());
}

@Override
public void removeInvalidFlights(List<FlightDTO> flights){
    List<Long> toDeleteIds=flights.stream().filter(flight->{
        List<SegmentDTO> segments=flight.getSegments();
        long totalGroundTime=0;

        for(int i=0;i<segments.size()-1;i++){
            SegmentDTO current=segments.get(i);
            SegmentDTO next=segments.get(i+1);

            long groundTime=Duration.between(current.getArrivalDate(),next.getDepartureDate()).toMinutes();

            if(groundTime<0){
                return true;
            }

            totalGroundTime+=groundTime;
        }

        return totalGroundTime>120;
    }).map(FlightDTO::getId).collect(Collectors.toList());

    flightRepository.deleteAllById(toDeleteIds);
}
}