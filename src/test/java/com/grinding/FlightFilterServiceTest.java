package com.grinding;

import com.grinding.dto.FlightDTO;
import com.grinding.filter.ArrivalBeforeDepartureFilter;
import com.grinding.filter.DepartureBeforeNowFilter;
import com.grinding.filter.FlightFilter;
import com.grinding.filter.LongGroundTimeFilter;
import com.grinding.mapper.FlightMapper;
import com.grinding.repository.FlightRepository;
import com.grinding.service.FlightFilterService;
import com.grinding.testing.FlightBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

public class FlightFilterServiceTest{
private FlightFilterService flightFilterService;
private FlightMapper flightMapper;
private FlightRepository flightRepository;

@BeforeEach
void setUp(){
    flightMapper=new FlightMapper();
    flightRepository=Mockito.mock(FlightRepository.class);

    List<FlightFilter> filters=Arrays.asList(new ArrivalBeforeDepartureFilter(flightRepository),new DepartureBeforeNowFilter(flightRepository),new LongGroundTimeFilter(flightRepository));

    flightFilterService=new FlightFilterService(filters);
}

private List<FlightDTO> getFlightDTOs(){
    var flights=FlightBuilder.createFlights();
    return flights.stream().map(flightMapper::toDTO).toList();
}

@Test
void testFilterArrivalBeforeDeparture_andDBRemoval(){
    List<FlightDTO> flights=getFlightDTOs();

    List<FlightDTO> filtered=flightFilterService.filterByName("arrival-before-departure",flights);

    assertTrue(filtered.stream().allMatch(flight->flight.getSegments().stream().allMatch(segment->!segment.getArrivalDate().isBefore(segment.getDepartureDate()))));

    verify(flightRepository,atLeast(1)).deleteAllById(any());
}

@Test
void testFilterDepartureBeforeNow_andDBRemoval(){
    List<FlightDTO> flights=getFlightDTOs();

    List<FlightDTO> filtered=flightFilterService.filterByName("departure-before-now",flights);

    assertTrue(filtered.stream().allMatch(flight->flight.getSegments().stream().allMatch(segment->!segment.getDepartureDate().isBefore(LocalDateTime.now()))));

    verify(flightRepository,atLeast(1)).deleteAllById(any());
}

@Test
void testFilterLongGroundTime_andDBRemoval(){
    List<FlightDTO> flights=getFlightDTOs();

    List<FlightDTO> filtered=flightFilterService.filterByName("long-ground-time",flights);

    filtered.forEach(flight->{
        long totalGroundTime=0;
        var segments=flight.getSegments();
        for(int i=0;i<segments.size()-1;i++){
            long groundTime=Duration.between(segments.get(i).getArrivalDate(),segments.get(i+1).getDepartureDate()).toMinutes();
            totalGroundTime+=groundTime;
        }
        assertTrue(totalGroundTime<=120);
    });

    verify(flightRepository,atLeast(1)).deleteAllById(any());
}

@Test
void testFilterByUnknownName_ThrowsException(){
    List<FlightDTO> flights=getFlightDTOs();

    IllegalArgumentException ex=assertThrows(IllegalArgumentException.class,()->flightFilterService.filterByName("unknown-filter",flights));
    assertTrue(ex.getMessage().contains("Фильтр с именем 'unknown-filter' не найден"));
}
}