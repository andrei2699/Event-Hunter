package com.example.eventhunter.ui.createEventForm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventFormViewModel extends ViewModel {

    private MutableLiveData<String> eventName;
    private MutableLiveData<String> eventDescription;
    private MutableLiveData<String> eventSeatNumber;
    private MutableLiveData<String> eventLocation;
    private MutableLiveData<String> eventType;
    private MutableLiveData<String> eventStartDate;
    private MutableLiveData<String> eventEndDate;
    private MutableLiveData<String> eventStartHour;
    private MutableLiveData<String> eventEndHour;
    private MutableLiveData<String> eventRepetitions;


    public EventFormViewModel() {
        eventName = new MutableLiveData<>();
        eventDescription = new MutableLiveData<>();
        eventSeatNumber = new MutableLiveData<>();
        eventLocation = new MutableLiveData<>();
        eventType = new MutableLiveData<>();
        eventStartDate = new MutableLiveData<>();
        eventEndDate = new MutableLiveData<>();
        eventStartHour = new MutableLiveData<>();
        eventEndHour = new MutableLiveData<>();
        eventRepetitions = new MutableLiveData<>();
    }

    public void removeValues() {
        eventName.setValue(null);
        eventDescription.setValue(null);
        eventSeatNumber.setValue(null);
        eventLocation.setValue(null);
        eventType.setValue(null);
        eventStartDate.setValue(null);
        eventEndDate.setValue(null);
        eventStartHour.setValue(null);
        eventEndHour.setValue(null);
        eventRepetitions.setValue(null);
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
}