package com.example.eventhunter.ui.reservationDetailsCard;

import android.graphics.drawable.Drawable;

public class ReservationDetailsCard {
    public String eventName;
    public Drawable eventImage;
    public String eventLocation;
    public String eventDate;
    public String eventStartHour;
    public int reservedSeats;
    public int ticketPrice;

    public ReservationDetailsCard(String eventName, String eventLocation, String eventDate, String eventStartHour, int reservedSeats, int ticketPrice) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventStartHour = eventStartHour;
        this.reservedSeats = reservedSeats;
        this.ticketPrice = ticketPrice;
    }

    public ReservationDetailsCard(String eventName, Drawable eventImage,  String eventLocation, String eventDate, String eventStartHour, int reservedSeats, int ticketPrice) {
        this.eventName = eventName;
        this.eventImage = eventImage;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventStartHour = eventStartHour;
        this.reservedSeats = reservedSeats;
        this.ticketPrice = ticketPrice;
    }
}

