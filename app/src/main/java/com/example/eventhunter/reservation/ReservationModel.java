package com.example.eventhunter.reservation;

import android.graphics.Bitmap;

import com.example.eventhunter.reservation.dto.ReservationModelDTO;

public class ReservationModel extends ReservationModelDTO {

    public Bitmap eventPhoto;

    public ReservationModel() {
    }

    public ReservationModel(String eventId, String userId, int reservationId, String eventName, String eventLocation, String eventStartDate, String eventStartHour, Double ticketPrice, int reservedSeatsNumber, Bitmap eventPhoto) {
        super(eventId, userId, reservationId, eventName, eventLocation, eventStartDate, eventStartHour, ticketPrice, reservedSeatsNumber);
        this.eventPhoto = eventPhoto;
    }

    public Double totalPrice() {
        return ticketPrice * reservedSeatsNumber;
    }
}
