package com.example.eventhunter.reservation;

import android.graphics.Bitmap;

import java.util.Objects;

public class ReservationDetailsCard {
    public String eventName;
    public Bitmap eventImage;
    public String eventLocation;
    public String eventDate;
    public String eventStartHour;
    public int reservedSeats;
    public Double ticketPrice;

    public ReservationDetailsCard(String eventName, Bitmap eventImage, String eventLocation, String eventDate, String eventStartHour, int reservedSeats, Double ticketPrice) {
        this.eventName = eventName;
        this.eventImage = eventImage;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventStartHour = eventStartHour;
        this.reservedSeats = reservedSeats;
        this.ticketPrice = ticketPrice;
    }
}

