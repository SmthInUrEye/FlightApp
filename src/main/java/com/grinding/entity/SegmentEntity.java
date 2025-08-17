package com.grinding.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="segments")
public class SegmentEntity{

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;

@Column(name="departure_date", nullable=false)
private LocalDateTime departureDate;

@Column(name="arrival_date", nullable=false)
private LocalDateTime arrivalDate;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "flight_id", nullable = false)

private FlightEntity flight;
public SegmentEntity(){
}

public SegmentEntity(LocalDateTime departureDate,LocalDateTime arrivalDate){
    this.departureDate=departureDate;
    this.arrivalDate=arrivalDate;
}

public Long getId(){
    return id;
}

public LocalDateTime getDepartureDate(){
    return departureDate;
}

public void setDepartureDate(LocalDateTime departureDate){
    this.departureDate=departureDate;
}

public LocalDateTime getArrivalDate(){
    return arrivalDate;
}

public void setArrivalDate(LocalDateTime arrivalDate){
    this.arrivalDate=arrivalDate;
}

public FlightEntity getFlight() {
    return flight;
}

public void setFlight(FlightEntity flight) {
    this.flight = flight;
}

}