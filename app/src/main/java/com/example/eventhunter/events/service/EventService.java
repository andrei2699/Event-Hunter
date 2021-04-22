package com.example.eventhunter.events.service;

import android.graphics.Bitmap;

import com.example.eventhunter.events.createEventForm.EventFormViewModel;
import com.example.eventhunter.events.service.dto.EventCardDTO;
import com.example.eventhunter.events.service.dto.EventModelDTO;

import java.util.List;

import androidx.lifecycle.Observer;

public interface EventService {
    void getEvent(String eventId, Observer<EventModelDTO> onEventReceived);

    void getEventPhoto(String eventId, Observer<Bitmap> onEventReceived);

    void getAllFutureEventsForUser(String userId, Observer<List<EventCardDTO>> onEventsReceived);

    void getAllPastEventsForUser(String userId, Observer<List<EventCardDTO>> onEventsReceived);

    void getAllEvents(Observer<List<EventCardDTO>> onEventsReceived);

    void createEvent(EventFormViewModel model, String organizerName, Observer<Boolean> onEventCreated);
}
