package com.example.eventhunter.repository;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class EventArrayOccurrenceTransmitter<T> {
    public Consumer<T>[] eventConsumers;

    public EventArrayOccurrenceTransmitter(Consumer<T>[] eventConsumers) {
        this.eventConsumers = eventConsumers;
    }

    public void waitAsyncEvents(Runnable onFinished) {
        if (eventConsumers.length == 0) {
            onFinished.run();
            return;
        }

        AtomicInteger arrivedCount = new AtomicInteger(eventConsumers.length);

        for (int i = 0; i < eventConsumers.length; i++) {
            eventConsumers[i] = eventConsumers[i].andThen(e -> {
                arrivedCount.getAndDecrement();

                if (arrivedCount.get() == 0) {
                    onFinished.run();
                }
            });
        }
    }
}