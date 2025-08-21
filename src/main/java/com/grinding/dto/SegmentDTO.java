package com.grinding.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Objects;

public class SegmentDTO{

@JsonIgnore
private Long id;

@JsonFormat(pattern="dd.MM.yyyy HH:mm", shape=JsonFormat.Shape.STRING)
@Schema(description = "Дата и время прибытия", example = "21.08.2025 11:10", type = "string", pattern = "dd.MM.yyyy HH:mm")
private LocalDateTime departureDate;

@JsonFormat(pattern="dd.MM.yyyy HH:mm", shape=JsonFormat.Shape.STRING)
@Schema(description = "Дата и время отправления", example = "21.08.2025 09:10", type = "string", pattern = "dd.MM.yyyy HH:mm")
private LocalDateTime arrivalDate;

public SegmentDTO(Long id,LocalDateTime departureDate,LocalDateTime arrivalDate){
    this.id=id;
    this.departureDate=departureDate;
    this.arrivalDate=arrivalDate;
}

public SegmentDTO(){
}

public Long getId(){
    return id;
}

public void setId(Long id){
    this.id=id;
}

public LocalDateTime getDepartureDate(){
    return departureDate;
}

public LocalDateTime getArrivalDate(){
    return arrivalDate;
}

public void setDepartureDate(LocalDateTime departureDate){
    this.departureDate=departureDate;
}

public void setArrivalDate(LocalDateTime arrivalDate){
    this.arrivalDate=arrivalDate;
}

@Override
public boolean equals(Object o){
    if(o==null||getClass()!=o.getClass())
        return false;
    SegmentDTO that=(SegmentDTO)o;
    return Objects.equals(id,that.id)&&Objects.equals(departureDate,that.departureDate)&&Objects.equals(arrivalDate,that.arrivalDate);
}

@Override
public int hashCode(){
    return Objects.hash(id,departureDate,arrivalDate);
}

@Override
public String toString(){
    return "SegmentDTO{"+", departureDate="+departureDate+", arrivalDate="+arrivalDate+'}';
}
}
