package com.example.eventhunter.profile.service;

import com.example.eventhunter.profile.regularUser.RegularUserModel;

import java.util.function.Consumer;

public interface RegularUserProfileService extends ProfileService {
    void getRegularUserProfileById(String id, Consumer<RegularUserModel> regularUserModelConsumer);

    void updateRegularUserProfileById(String id, RegularUserModel regularUserModel, Consumer<Boolean> updateConsumer);
}
