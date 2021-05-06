package com.example.eventhunter.reservation.reservationCardPopup;

public class ReservationCardDialogModel {
    private final String eventId;
    private final int availableSeatsNumber;
    private int chosenSeatsNumber;
    private final Double ticketPrice;

    public ReservationCardDialogModel(String eventId, int availableSeatsNumber, Double ticketPrice) {
        this.eventId = eventId;
        this.ticketPrice = ticketPrice;
        this.availableSeatsNumber = availableSeatsNumber;
        this.chosenSeatsNumber = 1;
    }

    public String getEventId() {
        return eventId;
    }

    public int getAvailableSeatsNumber() {
        return availableSeatsNumber;
    }

    public Double calculateTotalPrice() {
        return chosenSeatsNumber * ticketPrice;
    }

    public int getChosenSeatsNumber() {
        return chosenSeatsNumber;
    }

    public void setChosenSeatsNumber(int chosenSeatsNumber) {
        this.chosenSeatsNumber = chosenSeatsNumber;
    }
}
