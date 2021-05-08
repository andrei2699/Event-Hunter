package com.example.eventhunter.events.service;

import com.example.eventhunter.events.models.EventCard;
import com.example.eventhunter.events.models.EventModel;
import com.example.eventhunter.events.models.RepeatableEventModel;

import java.util.function.Consumer;

public interface EventService {
    void getEventAllDetails(String eventId, Consumer<EventModel> onEventReceived);

    void getAllFutureEventCardsForUser(String userId, Consumer<EventCard> onEventReceived);

    void getAllPastEventCardsForUser(String userId, Consumer<EventCard> onEventReceived);

    void getAllFutureEventsCards(Consumer<EventCard> onEventReceived);

    void createOneTimeEvent(EventModel model, Consumer<Boolean> onEventCreated);

    void createRepeatableEvent(RepeatableEventModel model, Consumer<Boolean> onEventCreated);

    void updateEvent(String id, EventModel model, Consumer<Boolean> updateConsumer);
}
