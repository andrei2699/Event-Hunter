package com.example.eventhunter.events.service.dto;

import android.graphics.Bitmap;

import com.example.eventhunter.repository.PhotoManager;

public class EventCardDTO {

    private String eventId;
    private String eventName;
    private Integer eventSeatNumber;
    private String organizerName;
    private String eventDate;
    private String eventLocation;
    private Double ticketPrice;

    public EventCardDTO() {
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public Integer getEventSeatNumber() {
        return eventSeatNumber;
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

    public Bitmap getEventImage() {
        return PhotoManager.getInstance().getBitmap(eventId);
    }

    public void setEventImage(Bitmap eventImage) {
        PhotoManager.getInstance().addBitmap(eventId, eventImage);
    }
}
