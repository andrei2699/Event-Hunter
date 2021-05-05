package com.example.eventhunter.events.service;

import com.example.eventhunter.events.models.EventModel;
import com.example.eventhunter.events.models.RepeatableEventModel;

import java.util.function.Consumer;

public interface EventService {
    void getEvent(String eventId, Consumer<EventModel> onEventReceived);

    void getAllFutureEventCardsForUser(String userId, Consumer<EventModel> onEventReceived);

    void getAllPastEventCardsForUser(String userId, Consumer<EventModel> onEventReceived);

    void getAllFutureEvents(Consumer<EventModel> onEventReceived);

    void createOneTimeEvent(EventModel model, Consumer<Boolean> onEventCreated);

    void createRepeatableEvent(RepeatableEventModel model, Consumer<Boolean> onEventCreated);
}
