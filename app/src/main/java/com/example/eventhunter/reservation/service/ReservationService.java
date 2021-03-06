package com.example.eventhunter.reservation.service;

import java.util.function.Consumer;

public interface ReservationService {

    void createReservation(String eventId, String userId, int seatNumber, Consumer<Boolean> reservationCreated);

    void cancelReservation(int reservationId, String eventId, String userId, int seatNumber, Consumer<Boolean> reservationCreated);
}
