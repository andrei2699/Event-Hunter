package com.example.eventhunter.ui.profile.organizer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrganizerProfileViewModel extends ViewModel {

    private MutableLiveData<String> organizerName;
    private MutableLiveData<String> organizerAddress;
    private MutableLiveData<String> organizerPhoneNumber;
    private MutableLiveData<String> organizerEmail;
    private MutableLiveData<String> organizerType;
    private MutableLiveData<String> organizerNumberOfOrganizedEvents;

    public OrganizerProfileViewModel() {
        organizerName = new MutableLiveData<>();
        organizerAddress = new MutableLiveData<>();
        organizerPhoneNumber = new MutableLiveData<>();
        organizerEmail = new MutableLiveData<>();
        organizerType = new MutableLiveData<>();
        organizerNumberOfOrganizedEvents = new MutableLiveData<>();
    }

    public MutableLiveData<String> getOrganizerType() {
        return organizerType;
    }

    public MutableLiveData<String> getOrganizerName() {
        return organizerName;
    }

    public MutableLiveData<String> getOrganizerAddress() {
        return organizerAddress;
    }

    public MutableLiveData<String> getOrganizerPhoneNumber() {
        return organizerPhoneNumber;
    }

    public MutableLiveData<String> getOrganizerEmail() {
        return organizerEmail;
    }

    public MutableLiveData<String> getOrganizerNumberOfOrganizedEvents() {
        return organizerNumberOfOrganizedEvents;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName.setValue(organizerName);
    }

    public void setOrganizerAddress(String organizerAddress) {
        this.organizerAddress.setValue(organizerAddress);
    }

    public void setOrganizerPhoneNumber(String organizerPhoneNumber) {
        this.organizerPhoneNumber.setValue(organizerPhoneNumber);
    }

    public void setOrganizerEmail(String organizerEmail) {
        this.organizerEmail.setValue(organizerEmail);
    }

    public void setOrganizerType(String organizerType) {
        this.organizerType.setValue(organizerType);
    }

    public void setOrganizerNumberOfOrganizedEvents(String organizerNumberOfOrganizedEvents) {
        this.organizerNumberOfOrganizedEvents.setValue(organizerNumberOfOrganizedEvents);
    }
}