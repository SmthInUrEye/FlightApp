package com.grinding.controller;

import com.grinding.dto.FlightDTO;
import com.grinding.mapper.FlightMapper;
import com.grinding.service.FlightFilterService;
import com.grinding.service.FlightService;
import com.grinding.testing.Flight;
import com.grinding.testing.FlightBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@Tag(name="Flight API", description="CRUD операции и фильтрация перелётов")
public class FlightController{

private final FlightService flightService;
private final FlightFilterService flightFilterService;
private final FlightMapper flightMapper;

public FlightController(FlightService flightService, FlightFilterService flightFilterService, FlightMapper flightMapper, FlightMapper flightMapper1){
    this.flightService=flightService;
    this.flightFilterService=flightFilterService;
    this.flightMapper = flightMapper1;
}

@Operation(summary="Создать новый перелёт", description="Создаёт новый перелёт и возвращает созданный объект с ID")
@PostMapping
public ResponseEntity<FlightDTO> createFlight(@RequestBody FlightDTO flight){
    FlightDTO created=flightService.createFlight(flight);
    return ResponseEntity.ok(created);
}

@Operation(summary="Получить все перелёты", description="Возвращает список всех перелётов из базы")
@GetMapping
public ResponseEntity<List<FlightDTO>> getAllFlights(){
    return ResponseEntity.ok(flightService.getAllFlights());
}

@Operation(summary="Получить перелёт по ID", description="Возвращает перелёт по уникальному идентификатору")
@GetMapping("/{id}")
public ResponseEntity<FlightDTO> getFlightById(@PathVariable Long id){
    return flightService.getFlightById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
}

@Operation(summary="Обновить перелёт", description="Обновляет данные перелёта по ID")
@PutMapping("/{id}")
public ResponseEntity<FlightDTO> updateFlight(@PathVariable Long id,@RequestBody FlightDTO flight){
    try{
        FlightDTO updated=flightService.updateFlight(id,flight);
        return ResponseEntity.ok(updated);
    }catch(RuntimeException e){
        return ResponseEntity.notFound().build();
    }
}

@Operation(summary="Удалить перелёт", description="Удаляет перелёт по ID")
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteFlight(@PathVariable Long id){
    flightService.deleteFlight(id);
    return ResponseEntity.noContent().build();
}

@Operation(summary="Указать фильтр в формате: *departure-before-now*", description="Возвращает отфильтрованные перелёты")
@GetMapping("/filter/{name}")
public List<FlightDTO> filterByName(@PathVariable String name){
    List<FlightDTO> allFlights=flightService.getAllFlights();
    return flightFilterService.filterByName(name,allFlights);
}

@Operation(summary="Генерация демо-перелётов", description="Создаёт и сохраняет демонстрационный набор перелётов")
@PostMapping("/generate-demo")
public ResponseEntity<Void> generateDemoFlights(){
    List<Flight> demoFlights=FlightBuilder.createFlights();

    demoFlights.forEach(flight -> {
        FlightDTO flightDTO = flightMapper.toDTO(flight);
        flightService.createFlight(flightDTO);
    });

    return ResponseEntity.ok().build();
}

}
