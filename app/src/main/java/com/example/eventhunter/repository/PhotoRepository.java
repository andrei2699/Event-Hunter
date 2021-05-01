package com.example.eventhunter.repository;

import android.graphics.Bitmap;

import java.util.function.Consumer;

public interface PhotoRepository {
    void getPhoto(String pathToPhoto, Consumer<Bitmap> consumer);

    void updatePhoto(String pathToPhoto, Bitmap photo, Consumer<Boolean> updateStatus);
}
