package com.example.eventhunter.repository.eventOccurenceTransmitter;

import com.example.eventhunter.repository.EventArrayOccurrenceTransmitter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class EventArrayOccurrenceTransmitterTest {

    @Test
    public void test_emptyArray_shouldTerminateImmediately() {
        Consumer<Boolean>[] array = new Consumer[0];
        EventArrayOccurrenceTransmitter<Boolean> booleanEventArrayOccurrenceTransmitter = new EventArrayOccurrenceTransmitter<>(array);

        AtomicBoolean arrived = new AtomicBoolean(false);
        booleanEventArrayOccurrenceTransmitter.waitAsyncEvents(() -> arrived.set(true));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail();
            e.printStackTrace();
        }

        assertTrue(arrived.get());
    }

    @Test
    public void test_oneEvent() {
        Consumer<Boolean>[] consumers = new Consumer[1];

        AtomicBoolean consumerCalled = new AtomicBoolean(false);
        consumers[0] = aBoolean -> consumerCalled.set(true);

        EventArrayOccurrenceTransmitter<Boolean> booleanEventArrayOccurrenceTransmitter = new EventArrayOccurrenceTransmitter<>(consumers);

        AtomicBoolean eventCalled = new AtomicBoolean(false);
        booleanEventArrayOccurrenceTransmitter.waitAsyncEvents(() -> eventCalled.set(true));

        try {
            Thread.sleep(1000);
            for (Consumer<Boolean> consumer : consumers) {
                consumer.accept(true);
            }
        } catch (InterruptedException e) {
            fail();
            e.printStackTrace();
        }

        assertTrue(eventCalled.get());
        assertTrue(consumerCalled.get());
    }

    @Test
    public void test_twoEvent() {
        Consumer<Boolean>[] consumers = new Consumer[2];

        AtomicBoolean firstConsumerCalled = new AtomicBoolean(false);
        AtomicBoolean secondConsumerCalled = new AtomicBoolean(false);
        consumers[0] = aBoolean -> firstConsumerCalled.set(true);
        consumers[1] = aBoolean -> secondConsumerCalled.set(true);

        EventArrayOccurrenceTransmitter<Boolean> booleanEventArrayOccurrenceTransmitter = new EventArrayOccurrenceTransmitter<>(consumers);

        AtomicBoolean eventCalled = new AtomicBoolean(false);
        booleanEventArrayOccurrenceTransmitter.waitAsyncEvents(() -> eventCalled.set(true));

        try {
            Thread.sleep(10);
            consumers[0].accept(true);
            assertFalse(secondConsumerCalled.get());

            Thread.sleep(10);
            consumers[1].accept(true);
        } catch (InterruptedException e) {
            fail();
            e.printStackTrace();
        }

        assertTrue(eventCalled.get());
        assertTrue(firstConsumerCalled.get());
        assertTrue(secondConsumerCalled.get());
    }

    @Test
    public void test_multipleEvents() {
        int consumerCount = 5;
        Consumer<Boolean>[] consumers = new Consumer[consumerCount];

        AtomicBoolean[] consumersCalled = new AtomicBoolean[consumers.length];
        for (int i = 0; i < consumerCount; i++) {
            consumersCalled[i] = new AtomicBoolean(false);
        }

        for (int i = 0; i < consumerCount; i++) {
            int finalI = i;
            consumers[i] = aBoolean -> consumersCalled[finalI].set(true);
        }
        EventArrayOccurrenceTransmitter<Boolean> booleanEventArrayOccurrenceTransmitter = new EventArrayOccurrenceTransmitter<>(consumers);

        AtomicBoolean eventCalled = new AtomicBoolean(false);
        booleanEventArrayOccurrenceTransmitter.waitAsyncEvents(() -> eventCalled.set(true));

        try {
            for (int i = 0; i < consumerCount; i++) {
                Thread.sleep(10);
                consumers[i].accept(true);

                for (int j = i + 1; j < consumerCount; j++) {
                    assertFalse(consumersCalled[j].get());
                }
            }
        } catch (InterruptedException e) {
            fail();
            e.printStackTrace();
        }

        for (int i = 0; i < consumerCount; i++) {
            assertTrue(consumersCalled[i].get());
        }

        assertTrue(eventCalled.get());
    }

    @Test
    public void test_twoEventNotInOrder() {
        Consumer<Boolean>[] consumers = new Consumer[2];

        AtomicBoolean firstConsumerCalled = new AtomicBoolean(false);
        AtomicBoolean secondConsumerCalled = new AtomicBoolean(false);

        consumers[0] = aBoolean -> firstConsumerCalled.set(true);
        consumers[1] = aBoolean -> secondConsumerCalled.set(true);

        EventArrayOccurrenceTransmitter<Boolean> booleanEventArrayOccurrenceTransmitter = new EventArrayOccurrenceTransmitter<>(consumers);

        AtomicBoolean eventCalled = new AtomicBoolean(false);
        booleanEventArrayOccurrenceTransmitter.waitAsyncEvents(() -> eventCalled.set(true));

        try {
            Thread.sleep(10);
            consumers[1].accept(true);
            assertFalse(firstConsumerCalled.get());

            Thread.sleep(10);
            consumers[0].accept(true);
        } catch (InterruptedException e) {
            fail();
            e.printStackTrace();
        }

        assertTrue(eventCalled.get());
        assertTrue(firstConsumerCalled.get());
        assertTrue(secondConsumerCalled.get());
    }

    @Test
    public void test_multipleEventsNotInOrder() {
        int consumerCount = 10;
        Consumer<Boolean>[] consumers = new Consumer[consumerCount];

        AtomicBoolean[] consumersCalled = new AtomicBoolean[consumers.length];
        for (int i = 0; i < consumerCount; i++) {
            consumersCalled[i] = new AtomicBoolean(false);
        }

        for (int i = 0; i < consumerCount; i++) {
            int finalI = i;
            consumers[i] = aBoolean -> consumersCalled[finalI].set(true);
        }
        EventArrayOccurrenceTransmitter<Boolean> booleanEventArrayOccurrenceTransmitter = new EventArrayOccurrenceTransmitter<>(consumers);

        AtomicBoolean eventCalled = new AtomicBoolean(false);
        booleanEventArrayOccurrenceTransmitter.waitAsyncEvents(() -> eventCalled.set(true));

        try {
            List<Integer> callOrder = new ArrayList<>();
            for (int i = 0; i < consumerCount; i++) {
                callOrder.add(i);
            }

            Collections.shuffle(callOrder);

            for (int i = 0; i < consumerCount; i++) {
                Thread.sleep(5);
                consumers[callOrder.get(i)].accept(true);

                for (int j = i + 1; j < consumerCount; j++) {
                    assertFalse(consumersCalled[callOrder.get(j)].get());
                }
            }
        } catch (InterruptedException e) {
            fail();
            e.printStackTrace();
        }

        for (int i = 0; i < consumerCount; i++) {
            assertTrue(consumersCalled[i].get());
        }

        assertTrue(eventCalled.get());
    }
}