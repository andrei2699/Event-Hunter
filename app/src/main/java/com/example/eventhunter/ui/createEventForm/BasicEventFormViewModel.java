package com.example.eventhunter.ui.createEventForm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public abstract class BasicEventFormViewModel extends ViewModel {

    private MutableLiveData<String> eventName;
    private MutableLiveData<String> eventDescription;
    private MutableLiveData<Integer> eventSeatNumber;
    private MutableLiveData<String> eventLocation;
    private MutableLiveData<String> eventType;

}