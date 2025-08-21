package com.grinding.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class FlightDTO{

@JsonProperty(access = JsonProperty.Access.READ_ONLY)
private Long id;

private List<SegmentDTO> segments;

public FlightDTO(Long id,List<SegmentDTO> segments){
    this.id=id;
    this.segments=segments;
}

public FlightDTO(){
}

public FlightDTO(List<SegmentDTO> segments){
    this.segments=segments;
}

public Long getId(){
    return id;
}

public void setId(Long id){
    this.id=id;
}

public List<SegmentDTO> getSegments(){
    return segments;
}

public void setSegments(List<SegmentDTO> segments){
    this.segments=segments;
}

@Override
public boolean equals(Object o){
    if(o==null||getClass()!=o.getClass())
        return false;
    FlightDTO flightDTO=(FlightDTO)o;
    return Objects.equals(id,flightDTO.id)&&Objects.equals(segments,flightDTO.segments);
}

@Override
public int hashCode(){
    return Objects.hash(id,segments);
}

@Override
public String toString(){
    return "FlightDTO{"+"id="+id+", segments="+segments+'}';
}
}
