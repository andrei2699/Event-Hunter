package com.example.eventhunter.collaborator.ui.header;

import android.graphics.Bitmap;

public class CollaboratorHeaderWithImage extends CollaboratorHeader {
    private final Bitmap collaboratorImage;

    public CollaboratorHeaderWithImage(String collaboratorId, String collaboratorName, Bitmap collaboratorImage) {
        super(collaboratorId, collaboratorName);
        this.collaboratorImage = collaboratorImage;
    }

    public Bitmap getCollaboratorImage() {
        return collaboratorImage;
    }
}
