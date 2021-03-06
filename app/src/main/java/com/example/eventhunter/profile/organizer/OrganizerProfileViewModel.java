package com.example.eventhunter.profile.organizer;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrganizerProfileViewModel extends ViewModel {

    private MutableLiveData<String> organizerId;
    private MutableLiveData<String> organizerName;
    private MutableLiveData<String> organizerAddress;
    private MutableLiveData<String> organizerPhoneNumber;
    private MutableLiveData<String> organizerEmail;
    private MutableLiveData<String> organizerType;
    private MutableLiveData<String> organizerNumberOfOrganizedEvents;
    private MutableLiveData<Bitmap> organizerPhoto;

    public OrganizerProfileViewModel() {
        organizerName = new MutableLiveData<>();
        organizerId = new MutableLiveData<>();
        organizerAddress = new MutableLiveData<>();
        organizerPhoneNumber = new MutableLiveData<>();
        organizerEmail = new MutableLiveData<>();
        organizerType = new MutableLiveData<>();
        organizerNumberOfOrganizedEvents = new MutableLiveData<>();
        organizerPhoto = new MutableLiveData<>();
    }

    public MutableLiveData<String> getOrganizerType() {
        return organizerType;
    }

    public MutableLiveData<String> getOrganizerId() {
        return organizerId;
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

    public MutableLiveData<Bitmap> getOrganizerPhoto() {
        return organizerPhoto;
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

    public void setOrganizerId(String organizerId) {
        this.organizerId.setValue(organizerId);
    }

    public void setOrganizerEmail(String organizerEmail) {
        this.organizerEmail.setValue(organizerEmail);
    }

    public void setOrganizerPhoto(Bitmap organizerPhoto) {
        this.organizerPhoto.setValue(organizerPhoto);
    }

    public void setOrganizerType(String organizerType) {
        this.organizerType.setValue(organizerType);
    }

    public void setOrganizerNumberOfOrganizedEvents(String organizerNumberOfOrganizedEvents) {
        this.organizerNumberOfOrganizedEvents.setValue(organizerNumberOfOrganizedEvents);
    }
}