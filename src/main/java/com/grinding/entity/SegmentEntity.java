package com.grinding.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="segments")
public class SegmentEntity{

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@JsonIgnore
private Long id;

@Column(name="departure_date", nullable=false)
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
private LocalDateTime departureDate;

@Column(name="arrival_date", nullable=false)
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
private LocalDateTime arrivalDate;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "flight_id", nullable = false)
@JsonBackReference
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