package com.example.eventhunter.ui.mainPage.collaborators.collaboratorCard;

import android.graphics.Bitmap;

public class CollaboratorCard {
    public String collaboratorId;
    public String collaboratorName;
    public String collaboratorEmail;
    public Bitmap collaboratorImage;

    public CollaboratorCard() {

    }

    public CollaboratorCard(String collaboratorId, String collaboratorName, String collaboratorEmail, Bitmap collaboratorImage) {
        this.collaboratorId = collaboratorId;
        this.collaboratorName = collaboratorName;
        this.collaboratorEmail = collaboratorEmail;
        this.collaboratorImage = collaboratorImage;
    }
}
