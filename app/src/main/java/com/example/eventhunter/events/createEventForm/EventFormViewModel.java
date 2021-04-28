package com.example.eventhunter.events.createEventForm;

import android.graphics.Bitmap;

import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventFormViewModel extends ViewModel {

    private final MutableLiveData<String> eventName;
    private final MutableLiveData<String> eventDescription;
    private final MutableLiveData<String> eventSeatNumber;
    private final MutableLiveData<String> eventLocation;
    private final MutableLiveData<String> eventTicketPrice;
    private final MutableLiveData<String> eventType;
    private final MutableLiveData<String> eventStartDate;
    private final MutableLiveData<String> eventEndDate;
    private final MutableLiveData<String> eventStartHour;
    private final MutableLiveData<String> eventEndHour;
    private final MutableLiveData<String> eventRepetitions;
    private final MutableLiveData<Bitmap> eventPhoto;
    private final MutableLiveData<List<CollaboratorHeader>> collaborators;

    public EventFormViewModel() {
        eventName = new MutableLiveData<>();
        eventDescription = new MutableLiveData<>();
        eventSeatNumber = new MutableLiveData<>();
        eventLocation = new MutableLiveData<>();
        eventTicketPrice = new MutableLiveData<>();
        eventType = new MutableLiveData<>();
        eventStartDate = new MutableLiveData<>();
        eventEndDate = new MutableLiveData<>();
        eventStartHour = new MutableLiveData<>();
        eventEndHour = new MutableLiveData<>();
        eventRepetitions = new MutableLiveData<>();
        eventPhoto = new MutableLiveData<>();
        collaborators = new MutableLiveData<>();
        collaborators.setValue(new ArrayList<>());
    }

    public void removeValues() {
        eventName.setValue(null);
        eventDescription.setValue(null);
        eventSeatNumber.setValue(null);
        eventLocation.setValue(null);
        eventTicketPrice.setValue(null);
        eventType.setValue(null);
        eventStartDate.setValue(null);
        eventEndDate.setValue(null);
        eventStartHour.setValue(null);
        eventEndHour.setValue(null);
        eventRepetitions.setValue(null);
        eventPhoto.setValue(null);
    }

    public void setEventName(String eventName) {
        this.eventName.setValue(eventName);
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription.setValue(eventDescription);
    }

    public void setEventSeatNumber(String eventSeatNumber) {
        this.eventSeatNumber.setValue(eventSeatNumber);
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation.setValue(eventLocation);
    }

    public void setEventTicketPrice(String eventTicketPrice) {
        this.eventTicketPrice.setValue(eventTicketPrice);
    }

    public void setEventType(String eventType) {
        this.eventType.setValue(eventType);
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate.setValue(eventStartDate);
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate.setValue(eventEndDate);
    }

    public void setEventStartHour(String eventStartHour) {
        this.eventStartHour.setValue(eventStartHour);
    }

    public void setEventEndHour(String eventEndHour) {
        this.eventEndHour.setValue(eventEndHour);
    }

    public void setEventRepetitions(String eventRepetitions) {
        this.eventRepetitions.setValue(eventRepetitions);
    }

    public void setEventPhoto(Bitmap eventPhoto) {
        this.eventPhoto.setValue(eventPhoto);
    }

    public void addCollaborator(CollaboratorHeader collaboratorHeader) {
        List<CollaboratorHeader> collaborators = this.collaborators.getValue();
        if (collaborators == null) {
            collaborators = new ArrayList<>();
        }
        if (!collaborators.contains(collaboratorHeader)) {
            collaborators.add(collaboratorHeader);
            this.collaborators.setValue(collaborators);
        }
    }

    public void removeCollaborator(CollaboratorHeader collaboratorHeader) {
        List<CollaboratorHeader> collaborators = this.collaborators.getValue();
        if (collaborators != null && collaborators.contains(collaboratorHeader)) {
            collaborators.remove(collaboratorHeader);
            this.collaborators.setValue(collaborators);
        }
    }

    public MutableLiveData<String> getEventName() {
        return eventName;
    }

    public MutableLiveData<String> getEventDescription() {
        return eventDescription;
    }

    public MutableLiveData<String> getEventSeatNumber() {
        return eventSeatNumber;
    }

    public MutableLiveData<String> getEventLocation() {
        return eventLocation;
    }

    public MutableLiveData<String> getEventTicketPrice() {
        return eventTicketPrice;
    }

    public MutableLiveData<String> getEventType() {
        return eventType;
    }

    public MutableLiveData<String> getEventStartDate() {
        return eventStartDate;
    }

    public MutableLiveData<String> getEventEndDate() {
        return eventEndDate;
    }

    public MutableLiveData<String> getEventStartHour() {
        return eventStartHour;
    }

    public MutableLiveData<String> getEventEndHour() {
        return eventEndHour;
    }

    public MutableLiveData<String> getEventRepetitions() {
        return eventRepetitions;
    }

    public MutableLiveData<List<CollaboratorHeader>> getCollaborators() {
        return collaborators;
    }

    public MutableLiveData<Bitmap> getEventPhoto() {
        return eventPhoto;
    }

    public List<CollaboratorHeader> getCollaboratorsDTO() {
        return this.collaborators.getValue();
    }
}