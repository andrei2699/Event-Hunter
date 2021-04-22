package com.example.eventhunter.collaborator.ui.header;

import android.graphics.Bitmap;

import com.example.eventhunter.repository.PhotoManager;

import java.util.Objects;

public class CollaboratorHeader {
    private String collaboratorId;
    private String collaboratorName;

    public CollaboratorHeader() {
    }

    public CollaboratorHeader(String collaboratorName) {
        this.collaboratorName = collaboratorName;
    }

    public CollaboratorHeader(String collaboratorId, String collaboratorName, Bitmap collaboratorImage) {
        this.collaboratorId = collaboratorId;
        this.collaboratorName = collaboratorName;

        PhotoManager.getInstance().addBitmap(collaboratorId, collaboratorImage);
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

    public String getCollaboratorId() {
        return collaboratorId;
    }

    public String getCollaboratorName() {
        return collaboratorName;
    }

    public Bitmap getCollaboratorBitmap() {
        return PhotoManager.getInstance().getBitmap(collaboratorId);
    }
}
