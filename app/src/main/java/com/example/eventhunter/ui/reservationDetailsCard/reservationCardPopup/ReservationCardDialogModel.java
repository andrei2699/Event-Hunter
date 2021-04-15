package com.example.eventhunter.ui.reservationDetailsCard.reservationCardPopup;

import com.example.eventhunter.ui.collaboratorHeader.CollaboratorHeader;

import java.util.List;

public class ReservationCardDialogModel {
    public String eventName;
    public String eventId;
    public String organizerName;
    public String eventDate;
    public String eventLocation;
    public List<CollaboratorHeader> collaborators;
    public int availableSeatsNumber;
    public int chosenSeatsNumber;

    public ReservationCardDialogModel(String eventId, String eventName, String organizerName, String eventDate, String eventLocation, int availableSeatsNumber, List<CollaboratorHeader> collaborators) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.collaborators = collaborators;
        this.availableSeatsNumber = availableSeatsNumber;
    }
}
