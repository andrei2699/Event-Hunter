package com.example.eventhunter.reservation.dto;

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
}
