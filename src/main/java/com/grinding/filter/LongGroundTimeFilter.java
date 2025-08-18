package com.grinding.filter;

import com.grinding.entity.FlightEntity;
import com.grinding.entity.SegmentEntity;
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
public List<FlightEntity> filter(List<FlightEntity> flights){

    return flights.stream().filter(flight->{
        List<SegmentEntity> segments=flight.getSegments();

        long totalGroundTime=0;

        for(int i=0;i<segments.size()-1;i++){

            SegmentEntity current=segments.get(i);
            SegmentEntity next=segments.get(i+1);

            long groundTime=Duration.between(current.getArrivalDate(),next.getDepartureDate()).toMinutes();

            if (groundTime < 0) {
                return false;
            }

            totalGroundTime+=groundTime;
        }
        return totalGroundTime>=120;
    }).collect(Collectors.toList());
}
}