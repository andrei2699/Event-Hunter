package com.example.eventhunter.events.service.dto;

import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;

import java.util.List;

public class EventModelDTO {
    public String eventId;
    public String eventName;
    public String eventDescription;
    public Integer eventSeatNumber;
    public String eventLocation;
    public String eventType;
    public String eventStartDate;
    public String eventEndDate;
    public String eventStartHour;
    public String eventEndHour;
    public Double ticketPrice;
    public String organizerId;
    public String organizerName;
    public List<CollaboratorHeader> collaborators;

    public EventModelDTO() {
    }

    public EventModelDTO(String eventName, String eventDescription,
                         Integer eventSeatNumber, String eventLocation, String eventType,
                         String eventStartDate, String eventEndDate, String eventStartHour,
                         String eventEndHour, Double ticketPrice, String organizerId,
                         String organizerName, List<CollaboratorHeader> collaborators) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventSeatNumber = eventSeatNumber;
        this.eventLocation = eventLocation;
        this.eventType = eventType;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventStartHour = eventStartHour;
        this.eventEndHour = eventEndHour;
        this.ticketPrice = ticketPrice;
        this.organizerId = organizerId;
        this.organizerName = organizerName;
        this.collaborators = collaborators;
    }
}
