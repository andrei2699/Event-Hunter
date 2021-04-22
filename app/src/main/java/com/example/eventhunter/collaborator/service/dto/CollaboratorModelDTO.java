package com.example.eventhunter.collaborator.service.dto;

public class CollaboratorModelDTO {
    public String collaboratorId;
    public String collaboratorName;

    public CollaboratorModelDTO() {
    }

    public CollaboratorModelDTO(String collaboratorId, String collaboratorName) {
        this.collaboratorId = collaboratorId;
        this.collaboratorName = collaboratorName;
    }

    public String getCollaboratorId() {
        return collaboratorId;
    }

    public String getCollaboratorName() {
        return collaboratorName;
    }
}
