package com.grinding.filter;

import com.grinding.dto.FlightDTO;

import java.util.List;

public interface FlightFilter{

String getName();

List<FlightDTO> filter(List<FlightDTO> flights);

default void removeInvalidFlights(List<FlightDTO> flights) {
}
}
