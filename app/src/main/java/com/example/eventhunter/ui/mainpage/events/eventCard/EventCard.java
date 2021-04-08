package com.example.eventhunter.ui.mainpage.events.eventCard;

import android.graphics.drawable.Drawable;

public class EventCard {
    public String eventName;
    public String organizerName;
    public String eventDate;
    public String eventLocation;
    public int availableSeatsNumber;
    public Drawable eventImage;

    public EventCard(String eventName, String organizerName, String eventDate, String eventLocation, int availableSeatsNumber) {
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.availableSeatsNumber = availableSeatsNumber;
    }

    public EventCard(String eventName, String organizerName, String eventDate, String eventLocation, int availableSeatsNumber, Drawable eventImage) {
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.availableSeatsNumber = availableSeatsNumber;
        this.eventImage = eventImage;
    }
}
