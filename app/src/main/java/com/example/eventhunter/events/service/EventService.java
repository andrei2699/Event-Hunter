package com.example.eventhunter.events.service;

import android.graphics.Bitmap;

import com.example.eventhunter.events.createEventForm.EventFormViewModel;
import com.example.eventhunter.events.service.dto.EventModelDTO;
import com.example.eventhunter.events.models.EventCard;

import androidx.lifecycle.Observer;

public interface EventService {
    void getEvent(String eventId, Observer<EventModelDTO> onEventReceived);

    void getEventPhoto(String eventId, Observer<Bitmap> onEventReceived);

    void getAllFutureEventCardsForUser(String userId, Observer<EventCard> onEventReceived);

    void getAllPastEventCardsForUser(String userId, Observer<EventCard> onEventReceived);

    void getAllEventCards(Observer<EventCard> onEventReceived);

    void createEvent(EventFormViewModel model, String organizerName, Observer<Boolean> onEventCreated);
}
