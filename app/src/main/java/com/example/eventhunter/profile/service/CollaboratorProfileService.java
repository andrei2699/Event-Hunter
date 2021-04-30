package com.example.eventhunter.profile.service;

import com.example.eventhunter.profile.collaborator.CollaboratorModel;

import java.util.function.Consumer;

public interface CollaboratorProfileService extends ProfileService {
    void getCollaboratorProfileById(String id, Consumer<CollaboratorModel> collaboratorModelConsumer);

    void updateCollaboratorProfile(String id, CollaboratorModel collaboratorModel, Consumer<Boolean> updateConsumer);
}
