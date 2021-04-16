package com.example.eventhunter.events.service.dto;

import com.example.eventhunter.ui.eventDetails.EventDetailsViewModel;

import java.util.List;

public class EventModelDTO {
    private String eventName;
    private String eventDescription;
    private String eventSeatNumber;
    private String eventLocation;
    private String eventType;
    private String eventDate;
    private String eventStartHour;
    private String eventEndHour;
    private String organizerName;
    private List<EventCollaboratorModelDTO> collaborators;

    public EventModelDTO() {
    }

    public EventModelDTO(String eventName, String eventDescription,
                         String eventSeatNumber, String eventLocation,
                         String eventType, String eventDate,
                         String eventStartHour, String eventEndHour, String organizerName,
                         List<EventCollaboratorModelDTO> collaborators) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventSeatNumber = eventSeatNumber;
        this.eventLocation = eventLocation;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.eventStartHour = eventStartHour;
        this.eventEndHour = eventEndHour;
        this.organizerName = organizerName;
        this.collaborators = collaborators;
    }

    public EventDetailsViewModel createLiveDataModel() {
        EventDetailsViewModel eventDetailsViewModel = new EventDetailsViewModel();

        eventDetailsViewModel.setEventName(eventName);
        eventDetailsViewModel.setEventDescription(eventDescription);
        eventDetailsViewModel.setEventSeatNumber(eventSeatNumber);
        eventDetailsViewModel.setEventLocation(eventLocation);
        eventDetailsViewModel.setEventType(eventType);
        eventDetailsViewModel.setEventDate(eventDate);
        eventDetailsViewModel.setEventStartHour(eventStartHour);
        eventDetailsViewModel.setEventEndHour(eventEndHour);
        eventDetailsViewModel.setEventOrganizerName(organizerName);

        return eventDetailsViewModel;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventSeatNumber() {
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

    public String getOrganizerName() {
        return organizerName;
    }

    public List<EventCollaboratorModelDTO> getCollaborators() {
        return collaborators;
    }
}
