package com.grinding.service;

import com.grinding.filter.ArrivalBeforeDepartureFilter;
import com.grinding.filter.DepartureBeforeNowFilter;
import com.grinding.filter.FlightFilter;
import com.grinding.filter.LongGroundTimeFilter;
import com.grinding.testing.Flight;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightFilterService{

private final List<FlightFilter> filters;

public FlightFilterService(List<FlightFilter> filters){
    this.filters=filters;
}

public List<Flight> filterAll(List<Flight> flights){
    List<Flight> result=flights;
    for(FlightFilter filter: filters){
        result=filter.filter(result);
    }
    return result;
}
}
