package com.example.eventhunter.reservation;

import android.graphics.Bitmap;

import com.example.eventhunter.reservation.dto.ReservationModelDTO;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ReservationModel that = (ReservationModel) o;
        return Objects.equals(eventPhoto, that.eventPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), eventPhoto);
    }
}
