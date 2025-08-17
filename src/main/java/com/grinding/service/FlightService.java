package com.grinding.service;

import com.grinding.entity.FlightEntity;
import com.grinding.mapper.FlightMapper;
import com.grinding.repository.FlightRepository;
import com.grinding.testing.Flight;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightService{

private final FlightRepository flightRepository;
private final FlightMapper flightMapper;

public FlightService(FlightRepository flightRepository,FlightMapper flightMapper){
    this.flightRepository=flightRepository;
    this.flightMapper=flightMapper;
}

public Flight createFlight(Flight flight){
    FlightEntity entity=flightMapper.toEntity(flight);
    FlightEntity saved=flightRepository.save(entity);
    return flightMapper.toModel(saved);
}

public List<Flight> getAllFlights(){
    return flightRepository.findAll().stream().map(flightMapper::toModel).collect(Collectors.toList());
}

public Optional<Flight> getFlightById(Long id){
    return flightRepository.findById(id).map(flightMapper::toModel);
}

public Flight updateFlight(Long id,Flight updatedFlight){
    return flightRepository.findById(id).map(existing->{

        existing.setSegments(flightMapper.toEntity(updatedFlight).getSegments());
        FlightEntity saved=flightRepository.save(existing);
        return flightMapper.toModel(saved);
    }).orElseThrow(()->new RuntimeException("Flight not found with ID "+id));
}

public void deleteFlight(Long id){
    flightRepository.deleteById(id);
}

}