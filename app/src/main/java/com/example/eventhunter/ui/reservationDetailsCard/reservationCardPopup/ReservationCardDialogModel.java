package com.example.eventhunter.ui.reservationDetailsCard.reservationCardPopup;

import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;

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
    public int ticketPrice;

    public ReservationCardDialogModel(String eventId, String eventName, String organizerName, String eventDate, String eventLocation, int availableSeatsNumber, int ticketPrice, List<CollaboratorHeader> collaborators) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.eventDate = eventDate;
        this.ticketPrice = ticketPrice;
        this.eventLocation = eventLocation;
        this.collaborators = collaborators;
        this.availableSeatsNumber = availableSeatsNumber;
    }
}
