package com.grinding;

import com.grinding.dto.FlightDTO;
import com.grinding.entity.FlightEntity;
import com.grinding.mapper.FlightMapper;
import com.grinding.repository.FlightRepository;
import com.grinding.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlightServiceTest{

@Mock
private FlightRepository flightRepository;

@Mock
private FlightMapper flightMapper;

@InjectMocks
private FlightService flightService;

private FlightDTO flightDTO;
private FlightEntity flightEntity;

@BeforeEach
void setUp(){
    MockitoAnnotations.openMocks(this);

    flightDTO=new FlightDTO();
    flightDTO.setId(1L);

    flightEntity=new FlightEntity();
    flightEntity.setId(1L);
}

@Test
void createFlight_shouldSaveAndReturnDTO(){
    when(flightMapper.toEntity(flightDTO)).thenReturn(flightEntity);
    when(flightRepository.save(flightEntity)).thenReturn(flightEntity);
    when(flightMapper.toDTO(flightEntity)).thenReturn(flightDTO);

    FlightDTO result=flightService.createFlight(flightDTO);

    assertNotNull(result);
    assertEquals(flightDTO.getId(),result.getId());

    verify(flightMapper).toEntity(flightDTO);
    verify(flightRepository).save(flightEntity);
    verify(flightMapper).toDTO(flightEntity);
}

@Test
void getAllFlights_shouldReturnListOfDTOs(){
    List<FlightEntity> entities=List.of(flightEntity);
    List<FlightDTO> dtos=List.of(flightDTO);

    when(flightRepository.findAll()).thenReturn(entities);
    when(flightMapper.toDTO(flightEntity)).thenReturn(flightDTO);

    List<FlightDTO> result=flightService.getAllFlights();

    assertNotNull(result);
    assertEquals(1,result.size());
    assertEquals(flightDTO.getId(),result.get(0).getId());

    verify(flightRepository).findAll();
    verify(flightMapper).toDTO(flightEntity);
}

@Test
void getFlightById_whenFound_shouldReturnDTO(){
    when(flightRepository.findById(1L)).thenReturn(Optional.of(flightEntity));
    when(flightMapper.toDTO(flightEntity)).thenReturn(flightDTO);

    Optional<FlightDTO> result=flightService.getFlightById(1L);

    assertTrue(result.isPresent());
    assertEquals(flightDTO.getId(),result.get().getId());

    verify(flightRepository).findById(1L);
    verify(flightMapper).toDTO(flightEntity);
}

@Test
void getFlightById_whenNotFound_shouldReturnEmpty(){
    when(flightRepository.findById(1L)).thenReturn(Optional.empty());

    Optional<FlightDTO> result=flightService.getFlightById(1L);

    assertTrue(result.isEmpty());

    verify(flightRepository).findById(1L);
    verifyNoInteractions(flightMapper);
}

@Test
void updateFlight_whenFound_shouldUpdateAndReturnDTO(){
    FlightDTO updatedDTO=new FlightDTO();
    updatedDTO.setId(1L);

    FlightEntity updatedEntity=new FlightEntity();
    updatedEntity.setId(1L);

    when(flightRepository.findById(1L)).thenReturn(Optional.of(flightEntity));
    when(flightMapper.toEntity(updatedDTO)).thenReturn(updatedEntity);
    when(flightRepository.save(updatedEntity)).thenReturn(updatedEntity);
    when(flightMapper.toDTO(updatedEntity)).thenReturn(updatedDTO);

    FlightDTO result=flightService.updateFlight(1L,updatedDTO);

    assertNotNull(result);
    assertEquals(updatedDTO.getId(),result.getId());

    verify(flightRepository).findById(1L);
    verify(flightMapper).toEntity(updatedDTO);
    verify(flightRepository).save(updatedEntity);
    verify(flightMapper).toDTO(updatedEntity);
}

@Test
void updateFlight_whenNotFound_shouldThrow(){
    when(flightRepository.findById(1L)).thenReturn(Optional.empty());

    RuntimeException ex=assertThrows(RuntimeException.class,()->{
        flightService.updateFlight(1L,flightDTO);
    });

    assertEquals("Flight not found with ID 1",ex.getMessage());

    verify(flightRepository).findById(1L);
    verifyNoMoreInteractions(flightRepository,flightMapper);
}

@Test
void deleteFlight_shouldCallRepository(){
    doNothing().when(flightRepository).deleteById(1L);

    flightService.deleteFlight(1L);

    verify(flightRepository).deleteById(1L);
}

}