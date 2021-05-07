package com.example.eventhunter.ui.mainPage.organizers.organizerCard;

import android.graphics.Bitmap;

public class OrganizerCard {
    public String organizerId;
    public String organizerName;
    public String organizerEmail;
    public String organizerEventsType;
    public Bitmap organizerImage;

    public OrganizerCard() {
    }

    public OrganizerCard(String organizerId, String organizerName, String organizerEmail, String organizerEventsType, Bitmap organizerImage) {
        this.organizerId = organizerId;
        this.organizerName = organizerName;
        this.organizerEmail = organizerEmail;
        this.organizerEventsType = organizerEventsType;
        this.organizerImage = organizerImage;
    }
}
