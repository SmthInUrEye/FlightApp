package com.grinding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grinding.controller.FlightController;
import com.grinding.entity.FlightEntity;
import com.grinding.mapper.FlightMapper;
import com.grinding.service.FlightFilterService;
import com.grinding.service.FlightService;
import com.grinding.testing.Flight;
import com.grinding.testing.FlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
@Import(FlightMapper.class)
class FlightControllerMvcTests {

@Autowired
private MockMvc mockMvc;

@MockBean
private FlightService flightService;

@MockBean
private FlightFilterService flightFilterService;

@Autowired
private ObjectMapper objectMapper;

@Autowired
private FlightMapper flightMapper;

private FlightEntity toEntityWithId(Flight flight, long id) {
    FlightEntity entity = flightMapper.toEntity(flight);
    entity.setId(id);
    return entity;
}

@Test
void testCreateFlight() throws Exception {
    Flight flight = FlightBuilder.createFlights().get(0);
    FlightEntity entity = toEntityWithId(flight, 1L);

    when(flightService.createFlight(any(Flight.class))).thenReturn(entity);

    mockMvc.perform(post("/api/flights")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(objectMapper.writeValueAsString(flight)))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.id", is(1)));
}

@Test
void testGetAllFlights() throws Exception {
    List<Flight> flights = FlightBuilder.createFlights();
    AtomicLong idGen = new AtomicLong(1L);
    List<FlightEntity> entities = flights.stream()
                                   .map(f -> toEntityWithId(f, idGen.getAndIncrement()))
                                   .toList();

    when(flightService.getAllFlights()).thenReturn(entities);

    mockMvc.perform(get("/api/flights"))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$", hasSize(entities.size())))
     .andExpect(jsonPath("$[0].id").value(1))
     .andExpect(jsonPath("$[1].id").value(2));
}

@Test
void testGetFlightById_found() throws Exception {
    Flight flight = FlightBuilder.createFlights().get(0);
    FlightEntity entity = toEntityWithId(flight, 10L);

    when(flightService.getFlightById(10L)).thenReturn(Optional.of(entity));

    mockMvc.perform(get("/api/flights/10"))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.id").value(10));
}

@Test
void testGetFlightById_notFound() throws Exception {
    when(flightService.getFlightById(999L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/flights/999"))
     .andExpect(status().isNotFound());
}

@Test
void testUpdateFlight_success() throws Exception {
    Flight flight = FlightBuilder.createFlights().get(0);
    FlightEntity updatedEntity = toEntityWithId(flight, 5L);

    when(flightService.updateFlight(5L, flight)).thenReturn(updatedEntity);

    mockMvc.perform(put("/api/flights/5")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(objectMapper.writeValueAsString(flight)))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.id").value(5));
}

@Test
void testUpdateFlight_notFound() throws Exception {
    Flight flight = FlightBuilder.createFlights().get(0);

    when(flightService.updateFlight(999L, flight))
     .thenThrow(new RuntimeException("Flight not found with ID 999"));

    mockMvc.perform(put("/api/flights/999")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(objectMapper.writeValueAsString(flight)))
     .andExpect(status().isNotFound());
}

@Test
void testDeleteFlight() throws Exception {
    mockMvc.perform(delete("/api/flights/3"))
     .andExpect(status().isNoContent());
}

@Test
void testFilterByName() throws Exception {
    String filterName = "long-ground-time";

    Flight flight = FlightBuilder.createFlights().get(0);
    FlightEntity entity = toEntityWithId(flight, 33L);

    List<FlightEntity> allFlights = List.of(entity);
    List<FlightEntity> filteredFlights = List.of(entity);

    when(flightService.getAllFlights()).thenReturn(allFlights);
    when(flightFilterService.filterByName(filterName, allFlights)).thenReturn(filteredFlights);

    mockMvc.perform(get("/api/flights/filter/{name}", filterName))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$", hasSize(1)))
     .andExpect(jsonPath("$[0].id").value(33));
}

@Test
void testGenerateDemoFlights() throws Exception {
    mockMvc.perform(post("/api/flights/generate-demo"))
     .andExpect(status().isOk());
}
}