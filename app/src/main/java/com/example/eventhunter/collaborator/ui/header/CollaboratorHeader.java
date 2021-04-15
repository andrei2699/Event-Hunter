package com.example.eventhunter.collaborator.ui.header;

import android.graphics.drawable.Drawable;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollaboratorHeader that = (CollaboratorHeader) o;
        return Objects.equals(collaboratorName, that.collaboratorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collaboratorName);
    }
}
