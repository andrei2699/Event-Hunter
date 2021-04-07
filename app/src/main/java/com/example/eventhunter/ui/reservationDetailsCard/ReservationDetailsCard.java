package com.example.eventhunter.ui.reservationDetailsCard;

import android.graphics.drawable.Drawable;

public class ReservationDetailsCard {
    public String eventName;
    public Drawable eventImage;
    public int reservedSeatsNumber;
    public int totalPrice;

    public ReservationDetailsCard(String eventName, int reservedSeatsNumber, int totalPrice) {
        this.eventName = eventName;
        this.reservedSeatsNumber = reservedSeatsNumber;
        this.totalPrice = totalPrice;
    }

    public ReservationDetailsCard(String eventName, Drawable eventImage, int reservedSeatsNumber, int totalPrice) {
        this.eventName = eventName;
        this.eventImage = eventImage;
        this.reservedSeatsNumber = reservedSeatsNumber;
        this.totalPrice = totalPrice;
    }
}

