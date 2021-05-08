package com.example.eventhunter.reservation.service;

import com.example.eventhunter.events.models.EventModel;
import com.example.eventhunter.events.service.EventService;
import com.example.eventhunter.profile.regularUser.RegularUserModel;
import com.example.eventhunter.profile.service.RegularUserProfileService;
import com.example.eventhunter.repository.EventOccurrenceTransmitter;
import com.example.eventhunter.reservation.ReservationModel;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class FirebaseReservationService implements ReservationService {

    private final EventService eventService;
    private final RegularUserProfileService profileService;

    public FirebaseReservationService(EventService eventService, RegularUserProfileService profileService) {
        this.eventService = eventService;
        this.profileService = profileService;
    }

    @Override
    public void createReservation(String eventId, String userId, int seatNumber, Consumer<Boolean> reservationCreated) {

        AtomicReference<EventModel> eventModelAtomicReference = new AtomicReference<>();
        AtomicReference<RegularUserModel> regularUserModelAtomicReference = new AtomicReference<>();

        Consumer<EventModel> eventModelConsumer = eventModelAtomicReference::set;
        Consumer<RegularUserModel> regularUserModelConsumer = regularUserModelAtomicReference::set;

        EventOccurrenceTransmitter<EventModel, RegularUserModel> transmitter = new EventOccurrenceTransmitter<>(eventModelConsumer, regularUserModelConsumer);

        transmitter.waitAsyncEvents(() -> {
            EventModel eventModel = eventModelAtomicReference.get();
            RegularUserModel regularUserModel = regularUserModelAtomicReference.get();

            ReservationModel reservationModel = new ReservationModel(eventId, userId, regularUserModel.reservationsNumber,
                    eventModel.eventName, eventModel.eventLocation, eventModel.eventStartDate, eventModel.eventStartHour,
                    eventModel.ticketPrice, seatNumber, eventModel.eventPhoto);

            regularUserModel.reservations.add(reservationModel);
            regularUserModel.reservationsNumber++;

            final Boolean[] successfulCreation = {true};

            Consumer<Boolean> updateEventConsumer = updateEvent -> successfulCreation[0] = successfulCreation[0] && updateEvent;

            Consumer<Boolean> updateProfileConsumer = updateProfile -> successfulCreation[0] = successfulCreation[0] && updateProfile;

            EventOccurrenceTransmitter<Boolean, Boolean> creationTransmitter = new EventOccurrenceTransmitter<>(updateEventConsumer, updateProfileConsumer);

            creationTransmitter.waitAsyncEvents(() -> reservationCreated.accept(successfulCreation[0]));

            eventModel.eventSeatNumber -= seatNumber;

            this.eventService.updateEvent(eventId, eventModel, creationTransmitter.firstEventConsumer);
            this.profileService.updateRegularUserProfileById(userId, regularUserModel, creationTransmitter.secondEventConsumer);
        });

        this.eventService.getEvent(eventId, transmitter.firstEventConsumer);
        this.profileService.getRegularUserProfileById(userId, transmitter.secondEventConsumer);
    }

    @Override
    public void cancelReservation(String reservationId, String eventId, Consumer<Boolean> reservationCanceled) {
//        this.eventService.getEvent(eventId, eventModel -> {
//            eventModel.
//        });
    }
}
