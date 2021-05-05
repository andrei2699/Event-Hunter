package com.example.eventhunter.profile.service;

import com.example.eventhunter.profile.regularUser.RegularUserModel;

import java.util.function.Consumer;

public interface RegularUserProfileService {
    void getAllReservations(String id, Consumer<RegularUserModel> regularUserModelConsumer);
}
