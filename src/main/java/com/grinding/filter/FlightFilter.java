package com.grinding.filter;

import com.grinding.entity.FlightEntity;

import java.util.List;

public interface FlightFilter{

String getName();

List<FlightEntity> filter(List<FlightEntity> flights);
}
