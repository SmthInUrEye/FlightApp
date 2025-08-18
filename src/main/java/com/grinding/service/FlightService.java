package com.grinding.service;


import com.grinding.entity.FlightEntity;
import com.grinding.mapper.FlightMapper;
import com.grinding.repository.FlightRepository;
import com.grinding.testing.Flight;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService{

private final FlightRepository flightRepository;
private final FlightMapper flightMapper;


public FlightService(FlightRepository flightRepository,FlightMapper flightMapper){
    this.flightRepository=flightRepository;
    this.flightMapper=flightMapper;
}

public FlightEntity createFlight(Flight flight){
    FlightEntity entity=flightMapper.toEntity(flight);
    return flightRepository.save(entity);
}

public List<FlightEntity> getAllFlights(){
    return flightRepository.findAll();
}

public Optional<FlightEntity> getFlightById(Long id){
    return flightRepository.findById(id);
}

public FlightEntity updateFlight(Long id,Flight updatedFlight){
    return flightRepository.findById(id).map(existing->{

        existing.setSegments(flightMapper.toEntity(updatedFlight).getSegments());
        return flightRepository.save(existing);
    }).orElseThrow(()->new RuntimeException("Flight not found with ID "+id));
}

public void deleteFlight(Long id){
    flightRepository.deleteById(id);
}


}