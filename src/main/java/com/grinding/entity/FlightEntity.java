package com.grinding.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="flights")
public class FlightEntity{

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;

@OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
private List<SegmentEntity> segments=new ArrayList<>();

public FlightEntity(){
}

public Long getId(){
    return id;
}

public List<SegmentEntity> getSegments(){
    return segments;
}

public void setSegments(List<SegmentEntity> segments){
    this.segments=segments;
}
}
