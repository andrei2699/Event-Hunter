package com.example.eventhunter.events.service.dto;

import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;

import java.util.List;

public class EventModelDTO {
    private String eventName;
    private String eventDescription;
    private Integer eventSeatNumber;
    private String eventLocation;
    private String eventType;
    private String eventDate;
    private String eventStartHour;
    private String eventEndHour;
    private Double ticketPrice;
    private String organizerName;
    private List<CollaboratorHeader> collaborators;

    public EventModelDTO() {
    }

    public EventModelDTO(String eventName, String eventDescription,
                         Integer eventSeatNumber, String eventLocation,
                         String eventType, String eventDate,
                         String eventStartHour, String eventEndHour, Double ticketPrice, String organizerName,
                         List<CollaboratorHeader> collaborators) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventSeatNumber = eventSeatNumber;
        this.eventLocation = eventLocation;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.eventStartHour = eventStartHour;
        this.eventEndHour = eventEndHour;
        this.ticketPrice = ticketPrice;
        this.organizerName = organizerName;
        this.collaborators = collaborators;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public Integer getEventSeatNumber() {
        return eventSeatNumber;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventStartHour() {
        return eventStartHour;
    }

    public String getEventEndHour() {
        return eventEndHour;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public List<CollaboratorHeader> getCollaborators() {
        return collaborators;
    }
}
