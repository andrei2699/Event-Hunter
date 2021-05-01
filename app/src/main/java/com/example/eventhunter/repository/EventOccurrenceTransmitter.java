package com.example.eventhunter.repository;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class EventOccurrenceTransmitter<T, Q> {
    public Consumer<T> firstEventConsumer;
    public Consumer<Q> secondEventConsumer;

    public EventOccurrenceTransmitter(Consumer<T> firstEventConsumer, Consumer<Q> secondEventConsumer) {
        this.firstEventConsumer = firstEventConsumer;
        this.secondEventConsumer = secondEventConsumer;
    }


    public void waitAsyncEvents(Runnable onFinished) {
        AtomicBoolean firstEventArrived = new AtomicBoolean(false);
        AtomicBoolean secondEventArrived = new AtomicBoolean(false);

        firstEventConsumer = firstEventConsumer.andThen(e -> {
            firstEventArrived.set(true);
            if (secondEventArrived.get()) {
                onFinished.run();
            }
        });

        secondEventConsumer = secondEventConsumer.andThen(e -> {
            secondEventArrived.set(true);
            if (firstEventArrived.get()) {
                onFinished.run();
            }
        });
    }
}