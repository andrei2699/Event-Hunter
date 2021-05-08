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

        this.eventService.getEventAllDetails(eventId, transmitter.firstEventConsumer);
        this.profileService.getRegularUserProfileById(userId, transmitter.secondEventConsumer);
    }

    @Override
    public void cancelReservation(int reservationId, String eventId, String userId, int seatNumber, Consumer<Boolean> reservationCanceled) {

        AtomicReference<EventModel> eventModelAtomicReference = new AtomicReference<>();
        AtomicReference<RegularUserModel> regularUserModelAtomicReference = new AtomicReference<>();

        Consumer<EventModel> eventModelConsumer = eventModelAtomicReference::set;
        Consumer<RegularUserModel> regularUserModelConsumer = regularUserModelAtomicReference::set;

        EventOccurrenceTransmitter<EventModel, RegularUserModel> transmitter = new EventOccurrenceTransmitter<>(eventModelConsumer, regularUserModelConsumer);

        transmitter.waitAsyncEvents(() -> {
            EventModel eventModel = eventModelAtomicReference.get();
            RegularUserModel regularUserModel = regularUserModelAtomicReference.get();

            for (ReservationModel reservation : regularUserModel.reservations) {
                if (reservation.reservationId == reservationId) {
                    regularUserModel.reservations.remove(reservation);
                    break;
                }
            }

            final Boolean[] successfulCancel = {true};

            Consumer<Boolean> updateEventConsumer = updateEvent -> successfulCancel[0] = successfulCancel[0] && updateEvent;

            Consumer<Boolean> updateProfileConsumer = updateProfile -> successfulCancel[0] = successfulCancel[0] && updateProfile;

            EventOccurrenceTransmitter<Boolean, Boolean> cancelTransmitter = new EventOccurrenceTransmitter<>(updateEventConsumer, updateProfileConsumer);

            cancelTransmitter.waitAsyncEvents(() -> reservationCanceled.accept(successfulCancel[0]));

            eventModel.eventSeatNumber += seatNumber;

            this.eventService.updateEvent(eventId, eventModel, cancelTransmitter.firstEventConsumer);
            this.profileService.updateRegularUserProfileById(userId, regularUserModel, cancelTransmitter.secondEventConsumer);
        });

        this.eventService.getEventAllDetails(eventId, transmitter.firstEventConsumer);
        this.profileService.getRegularUserProfileById(userId, transmitter.secondEventConsumer);
    }
}
