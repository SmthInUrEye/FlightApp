package com.grinding.filter;

import com.grinding.dto.FlightDTO;
import com.grinding.entity.FlightEntity;

import java.util.List;

public interface FlightFilter{

String getName();

List<FlightDTO> filter(List<FlightDTO> flights);
}
