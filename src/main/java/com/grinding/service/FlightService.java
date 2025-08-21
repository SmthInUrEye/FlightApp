package com.grinding.service;


import com.grinding.dto.FlightDTO;
import com.grinding.entity.FlightEntity;
import com.grinding.mapper.FlightMapper;
import com.grinding.repository.FlightRepository;
import com.grinding.testing.Flight;
import com.grinding.testing.FlightBuilder;
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

public FlightDTO createFlight(FlightDTO flightDTO){
    FlightEntity entity=flightMapper.toEntity(flightDTO);
    FlightEntity savedEntity=flightRepository.save(entity);
    return flightMapper.toDTO(savedEntity);
}

public List<FlightDTO> getAllFlights(){
    List<FlightEntity> entities=flightRepository.findAll();
    return entities.stream().map(flightMapper::toDTO).collect(Collectors.toList());

}

public Optional<FlightDTO> getFlightById(Long id){
    return flightRepository.findById(id).map(flightMapper::toDTO);
}

public FlightDTO updateFlight(Long id,FlightDTO updatedFlight){
    return flightRepository.findById(id).map(existingEntity->{

        FlightEntity updatedEntity=flightMapper.toEntity(updatedFlight);
        updatedEntity.setId(id);

        FlightEntity savedEntity=flightRepository.save(updatedEntity);
        return flightMapper.toDTO(savedEntity);
    }).orElseThrow(()->new RuntimeException("Flight not found with ID "+id));
}

public void deleteFlight(Long id){
    flightRepository.deleteById(id);
}

public void generateDemoFlights(){
    List<Flight> demoFlights=FlightBuilder.createFlights();
    demoFlights.forEach(flight->{
        FlightDTO flightDTO=flightMapper.toDTO(flight);
        this.createFlight(flightDTO);
    });
}
}