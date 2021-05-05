package com.example.eventhunter.events.models;

import android.graphics.Bitmap;

import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;
import com.example.eventhunter.events.service.dto.EventModelDTO;

import java.util.List;

public class EventModel extends EventModelDTO {
    public Bitmap eventPhoto;

    public EventModel() {
    }

    public EventModel(String eventName, String eventDescription, Integer eventSeatNumber,
                      String eventLocation, String eventType, String eventStartDate,
                      String eventEndDate, String eventStartHour, String eventEndHour,
                      Double ticketPrice, String organizerId, String organizerName,
                      List<CollaboratorHeader> collaborators, Bitmap eventPhoto) {

        super(eventName, eventDescription, eventSeatNumber, eventLocation, eventType,
                eventStartDate, eventEndDate, eventStartHour, eventEndHour, ticketPrice,
                organizerId, organizerName, collaborators);
        this.eventPhoto = eventPhoto;
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
        this.eventStartDate = modelDTO.eventStartDate;
        this.eventEndDate = modelDTO.eventEndDate;
        this.eventStartHour = modelDTO.eventStartHour;
        this.eventEndHour = modelDTO.eventEndHour;
        this.ticketPrice = modelDTO.ticketPrice;
        this.organizerName = modelDTO.organizerName;
        this.organizerId = modelDTO.organizerId;
        this.collaborators = modelDTO.collaborators;
    }

    public EventCard getEventCard() {
        return new EventCard(eventId, eventName, organizerName, eventStartDate, eventLocation,
                ticketPrice, eventSeatNumber, eventPhoto);
    }
}
