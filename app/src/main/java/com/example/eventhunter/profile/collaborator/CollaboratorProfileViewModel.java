package com.example.eventhunter.profile.collaborator;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventhunter.events.models.EventCard;

import java.util.List;

public class CollaboratorProfileViewModel extends ViewModel {
    private MutableLiveData<String> collaboratorId;
    private MutableLiveData<String> collaboratorName;
    private MutableLiveData<String> collaboratorAddress;
    private MutableLiveData<String> collaboratorPhoneNumber;
    private MutableLiveData<String> collaboratorEmail;

    private MutableLiveData<List<EventCard>> pastEvents;
    private MutableLiveData<List<EventCard>> futureEvents;

    public CollaboratorProfileViewModel() {
        pastEvents = new MutableLiveData<>();
        futureEvents = new MutableLiveData<>();
        collaboratorId = new MutableLiveData<>();
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

    public void setCollaboratorName(String collaboratorName) {
        this.collaboratorName.setValue(collaboratorName);
    }

    public MutableLiveData<String> getCollaboratorAddress() {
        return collaboratorAddress;
    }

    public MutableLiveData<String> getCollaboratorId() {
        return collaboratorId;
    }

    public void setCollaboratorAddress(String collaboratorAddress) {
        this.collaboratorAddress.setValue(collaboratorAddress);
    }

    public void setCollaboratorId(String collaboratorId) {
        this.collaboratorId.setValue(collaboratorId);
    }

    public MutableLiveData<String> getCollaboratorPhoneNumber() {
        return collaboratorPhoneNumber;
    }

    public void setCollaboratorPhoneNumber(String collaboratorPhoneNumber) {
        this.collaboratorPhoneNumber.setValue(collaboratorPhoneNumber);
    }

    public MutableLiveData<String> getCollaboratorEmail() {
        return collaboratorEmail;
    }

    public void setCollaboratorEmail(String collaboratorEmail) {
        this.collaboratorEmail.setValue(collaboratorEmail);
    }
}