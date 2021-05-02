package com.example.eventhunter.events.models;

import android.graphics.Bitmap;

import com.example.eventhunter.events.service.dto.EventModelDTO;

public class EventModel extends EventModelDTO {
    public Bitmap eventPhoto;

    public EventModel() {
    }

    public EventModel(EventModelDTO eventModelDTO) {
        set(eventModelDTO);
    }

    public void set(EventModelDTO modelDTO) {
        this.eventId = modelDTO.eventId;
        this.eventName = modelDTO.eventName;
        this.eventDescription = modelDTO.eventDescription;
        this.eventSeatNumber = modelDTO.eventSeatNumber;
        this.eventLocation = modelDTO.eventLocation;
        this.eventType = modelDTO.eventType;
        this.eventDate = modelDTO.eventDate;
        this.eventStartHour = modelDTO.eventStartHour;
        this.eventEndHour = modelDTO.eventEndHour;
        this.ticketPrice = modelDTO.ticketPrice;
        this.organizerName = modelDTO.organizerName;
        this.organizerId = modelDTO.organizerId;
        this.collaborators = modelDTO.collaborators;
    }

    public EventCard getEventCard() {
        return new EventCard(eventId, eventName, organizerName, eventDate, eventLocation,
                ticketPrice, eventSeatNumber, eventPhoto);
    }
}
