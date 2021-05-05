package com.example.eventhunter.events.models;

import android.graphics.Bitmap;

import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;

import java.util.List;

public class RepeatableEventModel extends EventModel {
    public int repetitions;

    public RepeatableEventModel(String eventName, String eventDescription, Integer eventSeatNumber,
                                String eventLocation, String eventType, String eventStartDate,
                                String eventEndDate, String eventStartHour, String eventEndHour,
                                Double ticketPrice, String organizerId, String organizerName,
                                List<CollaboratorHeader> collaborators, Bitmap eventPhoto,
                                int repetitions) {

        super(eventName, eventDescription, eventSeatNumber, eventLocation, eventType,
                eventStartDate, eventEndDate, eventStartHour, eventEndHour, ticketPrice,
                organizerId, organizerName, collaborators, eventPhoto);
        this.repetitions = repetitions;
    }
}
