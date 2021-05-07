package com.example.eventhunter.profile.service;

import com.example.eventhunter.profile.organizer.OrganizerModel;

import java.util.function.Consumer;

public interface OrganizerProfileService extends ProfileService {
    void getOrganizerProfileById(String id, Consumer<OrganizerModel> collaboratorModelConsumer);

    void updateOrganizerProfile(String id, OrganizerModel organizerModel, Consumer<Boolean> updateConsumer);

    void updateOrganizerEventCount(String id, int amount, Consumer<Boolean> consumer);
}
