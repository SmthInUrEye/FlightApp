package com.grinding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grinding.controller.FlightController;
import com.grinding.dto.FlightDTO;
import com.grinding.dto.SegmentDTO;
import com.grinding.mapper.FlightMapper;
import com.grinding.service.FlightFilterService;
import com.grinding.service.FlightService;
import com.grinding.testing.Flight;
import com.grinding.testing.FlightBuilder;
import com.grinding.testing.Segment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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

    private FlightDTO toDTO(Flight flight, Long id) {
        FlightDTO dto = flightMapper.toDTO(flight);
        dto.setId(id);
        return dto;
    }

    private SegmentDTO toSegmentDTO(Segment segment) {
        SegmentDTO dto = new SegmentDTO();
        dto.setDepartureDate(segment.getDepartureDate());
        dto.setArrivalDate(segment.getArrivalDate());
        return dto;
    }

    @Test
    void testCreateFlight() throws Exception {
        Flight flight = FlightBuilder.createFlights().get(0);
        FlightDTO responseDTO = toDTO(flight, 1L);

        when(flightService.createFlight(any(FlightDTO.class))).thenReturn(responseDTO);

        FlightDTO requestDTO = flightMapper.toDTO(flight);

        mockMvc.perform(post("/api/flights")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(requestDTO)))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testGetAllFlights() throws Exception {

        List<Flight> flights = FlightBuilder.createFlights();
        AtomicLong idGen = new AtomicLong(1L);

        List<FlightDTO> dtos = flights.stream()
         .map(flight -> toDTO(flight, idGen.getAndIncrement()))
         .collect(Collectors.toList());

        when(flightService.getAllFlights()).thenReturn(dtos);

        mockMvc.perform(get("/api/flights"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$", hasSize(dtos.size())))
         .andExpect(jsonPath("$[0].id").value(1))
         .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void testGetFlightById_found() throws Exception {
        Flight flight = FlightBuilder.createFlights().get(0);
        FlightDTO dto = toDTO(flight, 10L);

        when(flightService.getFlightById(10L)).thenReturn(Optional.of(dto));

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
        FlightDTO responseDTO = toDTO(flight, 5L);

        when(flightService.updateFlight(5L, any(FlightDTO.class))).thenReturn(responseDTO);

        FlightDTO requestDTO = flightMapper.toDTO(flight);

        mockMvc.perform(put("/api/flights/5")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(requestDTO)))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void testUpdateFlight_notFound() throws Exception {
        Flight flight = FlightBuilder.createFlights().get(0);
        FlightDTO requestDTO = flightMapper.toDTO(flight);

        when(flightService.updateFlight(999L, any(FlightDTO.class)))
         .thenThrow(new RuntimeException("Flight not found with ID 999"));

        mockMvc.perform(put("/api/flights/999")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(requestDTO)))
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
        FlightDTO dto = toDTO(flight, 33L);

        List<FlightDTO> allFlights = List.of(dto);
        List<FlightDTO> filteredFlights = List.of(dto);

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

    @Test
    void testFlightDTOStructure() throws Exception {

        SegmentDTO segmentDTO = new SegmentDTO();
        segmentDTO.setDepartureDate(LocalDateTime.of(2024, 1, 15, 10, 0));
        segmentDTO.setArrivalDate(LocalDateTime.of(2024, 1, 15, 12, 0));

        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setId(1L);
        flightDTO.setSegments(List.of(segmentDTO));

        when(flightService.getFlightById(1L)).thenReturn(Optional.of(flightDTO));

        mockMvc.perform(get("/api/flights/1"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.id").value(1))
         .andExpect(jsonPath("$.segments", hasSize(1)));
    }
}