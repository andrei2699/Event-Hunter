package com.example.eventhunter.events.service;

import com.example.eventhunter.events.createEventForm.EventFormViewModel;
import com.example.eventhunter.events.models.EventModel;

import java.util.function.Consumer;

public interface EventService {
    void getEvent(String eventId, Consumer<EventModel> onEventReceived);

    void getAllFutureEventCardsForUser(String userId, Consumer<EventModel> onEventReceived);

    void getAllPastEventCardsForUser(String userId, Consumer<EventModel> onEventReceived);

    void getAllFutureEvents(Consumer<EventModel> onEventReceived);

    void createOneTimeEvent(EventFormViewModel model, String organizerId, String organizerName, Consumer<Boolean> onEventCreated);

    void createRepeatableEvent(EventFormViewModel model, String organizerId, String organizerName, Consumer<Boolean> onEventCreated);
}
