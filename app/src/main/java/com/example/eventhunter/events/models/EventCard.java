package com.example.eventhunter.events.models;

import android.graphics.Bitmap;

public class EventCard {
    private final String eventId;
    private final String eventName;
    private final String organizerName;
    private final String eventDate;
    private final String eventLocation;
    private final Double ticketPrice;
    private final Bitmap eventImage;
    private int availableSeatsNumber;

    public EventCard(String eventId, String eventName, String organizerName, String eventDate, String eventLocation, Double ticketPrice, int availableSeatsNumber, Bitmap eventImage) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.ticketPrice = ticketPrice;
        this.availableSeatsNumber = availableSeatsNumber;
        this.eventImage = eventImage;
    }

    public void subtractReservedSeatsFromAvailableSeats(int numberOfReservedSeats) {
        this.availableSeatsNumber -= numberOfReservedSeats;
    }

    public void addCanceledSeatsToAvailableSeats(int numberOfCanceledSeats) {
        this.availableSeatsNumber -= numberOfCanceledSeats;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public int getAvailableSeatsNumber() {
        return availableSeatsNumber;
    }

    public Bitmap getEventImage() {
        return eventImage;
    }
}
