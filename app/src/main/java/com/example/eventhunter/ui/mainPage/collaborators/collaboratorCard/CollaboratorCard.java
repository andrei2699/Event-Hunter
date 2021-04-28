package com.example.eventhunter.ui.mainPage.collaborators.collaboratorCard;

import android.graphics.drawable.Drawable;

public class CollaboratorCard {
    public String collaboratorId;
    public String collaboratorName;
    public String collaboratorEmail;
    public Drawable collaboratorImage;

    public CollaboratorCard(String collaboratorId, String collaboratorName, String collaboratorEmail) {
        this.collaboratorName = collaboratorName;
        this.collaboratorEmail = collaboratorEmail;
        this.collaboratorId = collaboratorId;
    }

    public CollaboratorCard(String collaboratorName, String collaboratorEmail) {
        this.collaboratorName = collaboratorName;
        this.collaboratorEmail = collaboratorEmail;
    }

    public CollaboratorCard(String collaboratorName, String collaboratorEmail, Drawable collaboratorImage) {
        this.collaboratorName = collaboratorName;
        this.collaboratorEmail = collaboratorEmail;
        this.collaboratorImage = collaboratorImage;
    }
}
