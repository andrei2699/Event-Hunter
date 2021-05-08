package com.example.eventhunter.events.models;

import android.graphics.Bitmap;

public class EventCard {
    public final String eventId;
    public final String eventName;
    public final String organizerName;
    public final String eventDate;
    public final String eventLocation;
    public final Double ticketPrice;
    public final Bitmap eventImage;
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

    public int getAvailableSeatsNumber() {
        return availableSeatsNumber;
    }

    public void copy(EventCard eventCard) {
        this.availableSeatsNumber = eventCard.availableSeatsNumber;
    }
}
