package com.grinding.controller;

import com.grinding.filter.ArrivalBeforeDepartureFilter;
import com.grinding.filter.DepartureBeforeNowFilter;
import com.grinding.filter.LongGroundTimeFilter;
import com.grinding.service.FlightService;
import com.grinding.testing.Flight;
import com.grinding.testing.FlightBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@Tag(name = "Flight API", description = "CRUD операции и фильтрация перелётов")
public class FlightController{

private final FlightService flightService;
private final DepartureBeforeNowFilter departureBeforeNowFilter;
private final ArrivalBeforeDepartureFilter arrivalBeforeDepartureFilter;
private final LongGroundTimeFilter longGroundTimeFilter;

public FlightController(FlightService flightService,DepartureBeforeNowFilter departureBeforeNowFilter,ArrivalBeforeDepartureFilter arrivalBeforeDepartureFilter,LongGroundTimeFilter longGroundTimeFilter){
    this.flightService=flightService;
    this.departureBeforeNowFilter=departureBeforeNowFilter;
    this.arrivalBeforeDepartureFilter=arrivalBeforeDepartureFilter;
    this.longGroundTimeFilter=longGroundTimeFilter;
}

@Operation(summary = "Создать новый перелёт", description = "Создаёт новый перелёт и возвращает созданный объект с ID")
@PostMapping
public ResponseEntity<Flight> createFlight(@RequestBody Flight flight){
    Flight created=flightService.createFlight(flight);
    return ResponseEntity.ok(created);
}

@Operation(summary = "Получить все перелёты", description = "Возвращает список всех перелётов из базы")
@GetMapping
public ResponseEntity<List<Flight>> getAllFlights(){
    return ResponseEntity.ok(flightService.getAllFlights());
}

@Operation(summary = "Получить перелёт по ID", description = "Возвращает перелёт по уникальному идентификатору")
@GetMapping("/{id}")
public ResponseEntity<Flight> getFlightById(@PathVariable Long id){
    return flightService.getFlightById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
}

@Operation(summary = "Обновить перелёт", description = "Обновляет данные перелёта по ID")
@PutMapping("/{id}")
public ResponseEntity<Flight> updateFlight(@PathVariable Long id,@RequestBody Flight flight){
    try{
        Flight updated=flightService.updateFlight(id,flight);
        return ResponseEntity.ok(updated);
    }catch(RuntimeException e){
        return ResponseEntity.notFound().build();
    }
}

@Operation(summary = "Удалить перелёт", description = "Удаляет перелёт по ID")
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteFlight(@PathVariable Long id){
    flightService.deleteFlight(id);
    return ResponseEntity.noContent().build();
}

@Operation(summary = "Фильтрация: Вылет после текущего момента", description = "Возвращает перелёты, у которых вылет запланирован на будущее")
@GetMapping("/filter/departure-before-now")
public List<Flight> getFlightsAfterNow(){
    return departureBeforeNowFilter.filter(flightService.getAllFlights());
}

@Operation(summary = "Фильтрация: Прилёт позже вылета", description = "Возвращает перелёты, у которых сегменты имеют корректные даты прилёта")
@GetMapping("/filter/arrival-before-departure")
public List<Flight> getFlightsWithValidSegments(){
    return arrivalBeforeDepartureFilter.filter(flightService.getAllFlights());
}

@Operation(summary = "Фильтрация: Время на земле не более 2 часов", description = "Возвращает перелёты с допустимым временем на земле между сегментами")
@GetMapping("/filter/long-ground-time")
public List<Flight> getFlightsWithShortGroundTime(){
    return longGroundTimeFilter.filter(flightService.getAllFlights());
}

@Operation(summary = "Генерация демо-перелётов", description = "Создаёт и сохраняет демонстрационный набор перелётов")
@PostMapping("/generate-demo")
public ResponseEntity<Void> generateDemoFlights() {
    List<Flight> demoFlights = FlightBuilder.createFlights();
    demoFlights.forEach(flightService::createFlight);
    return ResponseEntity.ok().build();
}

}
