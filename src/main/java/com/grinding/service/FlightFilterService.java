package com.grinding.service;

import com.grinding.entity.FlightEntity;
import com.grinding.filter.FlightFilter;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightFilterService{

private final List<FlightFilter> filters;

public FlightFilterService(List<FlightFilter> filters){
    this.filters=filters;
}

public List<FlightEntity> filterByName(String name, List<FlightEntity> flights) {
    return filters.stream()
            .filter(filter -> filter.getName().equalsIgnoreCase(name))
            .findFirst()
            .map(filter -> filter.filter(flights))
            .orElseThrow(() -> new IllegalArgumentException("Фильтр с именем '" + name + "' не найден"));
}

}
