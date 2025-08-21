package com.grinding.filter;

import com.grinding.dto.FlightDTO;
import com.grinding.dto.SegmentDTO;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LongGroundTimeFilter implements FlightFilter{

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
        return totalGroundTime>=120;
    }).collect(Collectors.toList());
}
}