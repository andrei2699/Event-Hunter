package com.example.eventhunter.events.eventDetails;

import android.graphics.Bitmap;

import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventDetailsViewModel extends ViewModel {
    private final MutableLiveData<String> eventName;
    private final MutableLiveData<String> eventDescription;
    private final MutableLiveData<String> eventSeatNumber;
    private final MutableLiveData<String> eventLocation;
    private final MutableLiveData<String> eventType;
    private final MutableLiveData<String> eventDate;
    private final MutableLiveData<String> eventStartHour;
    private final MutableLiveData<String> eventEndHour;
    private final MutableLiveData<String> eventOrganizerName;
    private final MutableLiveData<String> eventTicketPrice;
    private final MutableLiveData<List<CollaboratorHeader>> eventCollaborators;
    private final MutableLiveData<Bitmap> eventPhoto;

    public EventDetailsViewModel() {
        eventOrganizerName = new MutableLiveData<>();
        eventName = new MutableLiveData<>();
        eventDescription = new MutableLiveData<>();
        eventSeatNumber = new MutableLiveData<>();
        eventLocation = new MutableLiveData<>();
        eventType = new MutableLiveData<>();
        eventDate = new MutableLiveData<>();
        eventStartHour = new MutableLiveData<>();
        eventEndHour = new MutableLiveData<>();
        eventTicketPrice = new MutableLiveData<>();
        eventCollaborators = new MutableLiveData<>();
        eventPhoto = new MutableLiveData<>();
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

    public void setEventType(String eventType) {
        this.eventType.setValue(eventType);
    }

    public void setEventDate(String eventDate) {
        this.eventDate.setValue(eventDate);
    }

    public void setEventStartHour(String eventStartHour) {
        this.eventStartHour.setValue(eventStartHour);
    }

    public void setEventEndHour(String eventEndHour) {
        this.eventEndHour.setValue(eventEndHour);
    }

    public void setEventCollaborators(List<CollaboratorHeader> eventCollaborators) {
        this.eventCollaborators.setValue(eventCollaborators);
    }

    public void setEventOrganizerName(String eventOrganizerName) {
        this.eventOrganizerName.setValue(eventOrganizerName);
    }

    public void setEventTicketPrice(String ticketPrice) {
        this.eventTicketPrice.setValue(ticketPrice);
    }

    public void setEventPhoto(Bitmap photo) {
        this.eventPhoto.setValue(photo);
    }

    public MutableLiveData<String> getEventName() {
        return eventName;
    }

    public MutableLiveData<String> getEventDescription() {
        return eventDescription;
    }

    public MutableLiveData<String> getEventTicketPrice() {
        return eventTicketPrice;
    }

    public MutableLiveData<String> getEventSeatNumber() {
        return eventSeatNumber;
    }

    public MutableLiveData<String> getEventLocation() {
        return eventLocation;
    }

    public MutableLiveData<String> getEventType() {
        return eventType;
    }

    public MutableLiveData<String> getEventDate() {
        return eventDate;
    }

    public MutableLiveData<String> getEventStartHour() {
        return eventStartHour;
    }

    public MutableLiveData<String> getEventEndHour() {
        return eventEndHour;
    }

    public MutableLiveData<List<CollaboratorHeader>> getEventCollaborators() {
        return eventCollaborators;
    }

    public MutableLiveData<String> getEventOrganizerName() {
        return eventOrganizerName;
    }

    public MutableLiveData<Bitmap> getEventPhoto() {
        return eventPhoto;
    }

    public int getEventSeatNumberValue() {
        String value = eventSeatNumber.getValue();
        if (value == null || value.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    public Double getTicketPriceValue() {
        String value = eventTicketPrice.getValue();
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(value);
    }
}