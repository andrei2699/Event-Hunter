package com.example.eventhunter.ui.mainPage.organizers.organizerCard;

import android.graphics.drawable.Drawable;

public class OrganizerCard {
    public String organizerId;
    public String organizerName;
    public String organizerEmail;
    public String organizerEventsType;
    public Drawable organizerImage;

    public OrganizerCard(String organizerId, String organizerName, String organizerEmail, String organizerEventsType) {
        this.organizerId = organizerId;
        this.organizerName = organizerName;
        this.organizerEmail = organizerEmail;
        this.organizerEventsType = organizerEventsType;
    }

    public OrganizerCard(String organizerName, String organizerEmail, String organizerEventsType, Drawable organizerImage) {
        this.organizerName = organizerName;
        this.organizerEmail = organizerEmail;
        this.organizerEventsType = organizerEventsType;
        this.organizerImage = organizerImage;
    }
}
