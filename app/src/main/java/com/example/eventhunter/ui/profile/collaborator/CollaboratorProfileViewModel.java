package com.example.eventhunter.ui.profile.collaborator;


import com.example.eventhunter.events.models.EventCard;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CollaboratorProfileViewModel extends ViewModel {
    private MutableLiveData<String> collaboratorName;
    private MutableLiveData<String> collaboratorAddress;
    private MutableLiveData<String> collaboratorPhoneNumber;
    private MutableLiveData<String> collaboratorEmail;

    private MutableLiveData<List<EventCard>> pastEvents;
    private MutableLiveData<List<EventCard>> futureEvents;

    public CollaboratorProfileViewModel() {
        pastEvents = new MutableLiveData<>();
        futureEvents = new MutableLiveData<>();
        collaboratorAddress = new MutableLiveData<>();
        collaboratorEmail = new MutableLiveData<>();
        collaboratorName = new MutableLiveData<>();
        collaboratorPhoneNumber = new MutableLiveData<>();
    }

    public void setPastEvents(List<EventCard> events) {
        this.pastEvents.setValue(events);
    }

    public MutableLiveData<List<EventCard>> getPastEvents() {
        return pastEvents;
    }

    public void setFutureEvents(List<EventCard> events) {
        this.futureEvents.setValue(events);
    }

    public MutableLiveData<List<EventCard>> getFutureEvents() {
        return futureEvents;
    }

    public MutableLiveData<String> getCollaboratorName() {
        return collaboratorName;
    }

    public void setCollaboratorName(MutableLiveData<String> collaboratorName) {
        this.collaboratorName = collaboratorName;
    }

    public MutableLiveData<String> getCollaboratorAddress() {
        return collaboratorAddress;
    }

    public void setCollaboratorAddress(MutableLiveData<String> collaboratorAddress) {
        this.collaboratorAddress = collaboratorAddress;
    }

    public MutableLiveData<String> getCollaboratorPhoneNumber() {
        return collaboratorPhoneNumber;
    }

    public void setCollaboratorPhoneNumber(MutableLiveData<String> collaboratorPhoneNumber) {
        this.collaboratorPhoneNumber = collaboratorPhoneNumber;
    }

    public MutableLiveData<String> getCollaboratorEmail() {
        return collaboratorEmail;
    }

    public void setCollaboratorEmail(MutableLiveData<String> collaboratorEmail) {
        this.collaboratorEmail = collaboratorEmail;
    }
}