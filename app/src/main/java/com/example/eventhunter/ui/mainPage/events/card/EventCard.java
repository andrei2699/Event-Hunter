package com.example.eventhunter.ui.mainPage.events.card;

import android.graphics.drawable.Drawable;

public class EventCard {
    public String eventId;
    public String eventName;
    public String organizerName;
    public String eventDate;
    public String eventLocation;
    public int ticketPrice;
    public int availableSeatsNumber;
    public Drawable eventImage;

    public EventCard(String eventId, String eventName, String organizerName, String eventDate, String eventLocation, int availableSeatsNumber, int ticketPrice) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.availableSeatsNumber = availableSeatsNumber;
        this.ticketPrice = ticketPrice;
    }

    public EventCard(String eventName, String organizerName, String eventDate, String eventLocation, int availableSeatsNumber, int ticketPrice, Drawable eventImage) {
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.availableSeatsNumber = availableSeatsNumber;
        this.eventImage = eventImage;
        this.ticketPrice = ticketPrice;
    }
}
