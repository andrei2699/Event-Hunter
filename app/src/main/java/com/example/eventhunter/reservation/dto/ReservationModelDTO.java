package com.example.eventhunter.reservation.dto;

import java.util.Objects;

public class ReservationModelDTO {

    public String eventId;
    public String userId;
    public int reservationId;
    public String eventName;
    public String eventLocation;
    public String eventStartDate;
    public String eventStartHour;
    public Double ticketPrice;
    public int reservedSeatsNumber;

    public ReservationModelDTO() {
    }

    public ReservationModelDTO(String eventId, String userId, int reservationId, String eventName, String eventLocation, String eventStartDate, String eventStartHour, Double ticketPrice, int reservedSeatsNumber) {
        this.eventId = eventId;
        this.userId = userId;
        this.reservationId = reservationId;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventStartDate = eventStartDate;
        this.eventStartHour = eventStartHour;
        this.ticketPrice = ticketPrice;
        this.reservedSeatsNumber = reservedSeatsNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationModelDTO that = (ReservationModelDTO) o;
        return reservationId == that.reservationId &&
                reservedSeatsNumber == that.reservedSeatsNumber &&
                Objects.equals(eventId, that.eventId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(eventName, that.eventName) &&
                Objects.equals(eventLocation, that.eventLocation) &&
                Objects.equals(eventStartDate, that.eventStartDate) &&
                Objects.equals(eventStartHour, that.eventStartHour) &&
                Objects.equals(ticketPrice, that.ticketPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, userId, reservationId, eventName, eventLocation, eventStartDate, eventStartHour, ticketPrice, reservedSeatsNumber);
    }
}
