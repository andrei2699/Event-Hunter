package com.example.eventhunter.profile.service;

public interface OrganizerProfileService extends ProfileService {
    void getOrganizerProfileById(String id);

    void updateOrganizerProfile(String id);
}
