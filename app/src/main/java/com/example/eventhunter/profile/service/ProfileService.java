package com.example.eventhunter.profile.service;

import android.graphics.Bitmap;

import java.util.function.Consumer;

public interface ProfileService {

    void getProfilePhoto(String id, Consumer<Bitmap> consumer);

    void uploadProfilePhoto(String id, Bitmap photo, Consumer<Boolean> updateStatus);
}
