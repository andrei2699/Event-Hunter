package com.example.eventhunter.reservation;

import android.graphics.Bitmap;

public class ReservationDetailsCard {
    public int reservationId;
    public String eventId;
    public String userId;
    public String eventName;
    public Bitmap eventImage;
    public String eventLocation;
    public String eventDate;
    public String eventStartHour;
    public int reservedSeats;
    public Double ticketPrice;

    public ReservationDetailsCard(int reservationId, String eventId, String userId, String eventName, Bitmap eventImage, String eventLocation, String eventDate, String eventStartHour, int reservedSeats, Double ticketPrice) {
        this.reservationId = reservationId;
        this.eventId = eventId;
        this.userId = userId;
        this.eventName = eventName;
        this.eventImage = eventImage;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventStartHour = eventStartHour;
        this.reservedSeats = reservedSeats;
        this.ticketPrice = ticketPrice;
    }
}

