package com.grinding.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class SegmentDTO {
    private Long id;
    private  LocalDateTime departureDate;
    private  LocalDateTime arrivalDate;

    public SegmentDTO(Long id, LocalDateTime departureDate, LocalDateTime arrivalDate) {
        this.id =id;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
    }

    public SegmentDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SegmentDTO that = (SegmentDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(departureDate, that.departureDate) && Objects.equals(arrivalDate, that.arrivalDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departureDate, arrivalDate);
    }

    @Override
    public String toString() {
        return "SegmentDTO{" +
         "id=" + id +
         ", departureDate=" + departureDate +
         ", arrivalDate=" + arrivalDate +
         '}';
    }
}
