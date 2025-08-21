package com.grinding;

import com.grinding.dto.FlightDTO;
import com.grinding.filter.ArrivalBeforeDepartureFilter;
import com.grinding.filter.DepartureBeforeNowFilter;
import com.grinding.filter.FlightFilter;
import com.grinding.filter.LongGroundTimeFilter;
import com.grinding.mapper.FlightMapper;
import com.grinding.service.FlightFilterService;
import com.grinding.testing.FlightBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FlightFilterServiceTest{
private FlightFilterService flightFilterService;
private FlightMapper flightMapper;

@BeforeEach
void setUp(){

    List<FlightFilter> filters=Arrays.asList(new ArrivalBeforeDepartureFilter(),new DepartureBeforeNowFilter(),new LongGroundTimeFilter());
    flightFilterService=new FlightFilterService(filters);
    flightMapper=new FlightMapper();
}

private List<FlightDTO> getFlightDTOs(){
    var flights=FlightBuilder.createFlights();
    return flights.stream().map(flightMapper::toDTO).toList();
}

@Test
void testFilterArrivalBeforeDeparture(){
    List<FlightDTO> flights=getFlightDTOs();

    List<FlightDTO> filtered=flightFilterService.filterByName("arrival-before-departure",flights);

    assertTrue(filtered.stream().allMatch(flight->flight.getSegments().stream().allMatch(segment->segment.getArrivalDate().isBefore(segment.getDepartureDate()))));
}

@Test
void testFilterDepartureBeforeNow(){
    List<FlightDTO> flights=getFlightDTOs();

    List<FlightDTO> filtered=flightFilterService.filterByName("departure-before-now",flights);

    assertTrue(filtered.stream().allMatch(flight->flight.getSegments().stream().allMatch(segment->segment.getDepartureDate().isBefore(java.time.LocalDateTime.now()))));
}

@Test
void testFilterLongGroundTime(){
    List<FlightDTO> flights=getFlightDTOs();

    List<FlightDTO> filtered=flightFilterService.filterByName("long-ground-time",flights);

    filtered.forEach(flight->{
        long totalGroundTime=0;
        var segments=flight.getSegments();
        for(int i=0;i<segments.size()-1;i++){
            long groundTime=java.time.Duration.between(segments.get(i).getArrivalDate(),segments.get(i+1).getDepartureDate()).toMinutes();
            totalGroundTime+=groundTime;
        }
        assertTrue(totalGroundTime>=120);
    });
}

@Test
void testFilterByUnknownName_ThrowsException(){
    List<FlightDTO> flights=getFlightDTOs();

    IllegalArgumentException ex=assertThrows(IllegalArgumentException.class,()->flightFilterService.filterByName("unknown-filter",flights));
    assertTrue(ex.getMessage().contains("Фильтр с именем 'unknown-filter' не найден"));
}
}