package com.example.eventhunter.repository;

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

        boolean[] arrived = new boolean[eventConsumers.length];

        for (int i = 0; i < eventConsumers.length; i++) {
            int finalI = i;
            eventConsumers[i] = eventConsumers[i].andThen(e -> {
                arrived[finalI] = true;

                boolean haveAllArrived = true;
                for (boolean b : arrived) {
                    if (!b) {
                        haveAllArrived = false;
                        break;
                    }
                }
                if (haveAllArrived) {
                    onFinished.run();
                }
            });
        }
    }
}