package com.example.eventhunter.ui.mainpage.events.card;

import android.graphics.drawable.Drawable;

public class EventCard {
    public String eventId;
    public String eventName;
    public String organizerName;
    public String eventDate;
    public String eventLocation;
    public int availableSeatsNumber;
    public Drawable eventImage;

    public EventCard(String eventId, String eventName, String organizerName, String eventDate, String eventLocation, int availableSeatsNumber) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.availableSeatsNumber = availableSeatsNumber;
    }

    public EventCard(String eventId, String eventName, String organizerName, String eventDate, String eventLocation, int availableSeatsNumber, Drawable eventImage) {
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.availableSeatsNumber = availableSeatsNumber;
        this.eventImage = eventImage;
    }
}
