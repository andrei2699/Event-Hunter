package com.example.eventhunter.ui.collaboratorHeader;

import android.graphics.drawable.Drawable;

public class CollaboratorHeader {
    public String collaboratorName;
    public Drawable collaboratorImage;

    public CollaboratorHeader(String collaboratorName) {
        this.collaboratorName = collaboratorName;
    }

    public CollaboratorHeader(String collaboratorName, Drawable collaboratorImage) {
        this.collaboratorName = collaboratorName;
        this.collaboratorImage = collaboratorImage;
    }
}
