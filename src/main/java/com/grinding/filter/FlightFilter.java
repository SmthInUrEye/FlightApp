package com.grinding.filter;

import com.grinding.testing.Flight;

import java.util.List;

public interface FlightFilter {
List<Flight> filter(List<Flight> flights);
}
