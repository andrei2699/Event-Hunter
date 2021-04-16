package com.example.eventhunter.events.service;

import com.example.eventhunter.events.createEventForm.EventFormViewModel;

import java.util.List;

import androidx.lifecycle.Observer;


public interface EventService {
    void getEvent(String eventId, Observer<EventFormViewModel> onEventReceived);

    void getAllEvents(Observer<List<EventFormViewModel>> onEventsReceived);
}
