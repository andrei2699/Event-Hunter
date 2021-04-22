package com.example.eventhunter.utils.photoUpload;

import android.graphics.Bitmap;

import java.util.function.Consumer;

public interface PhotoUploadService {
    void launchCamera(Consumer<Bitmap> onImageTaken);

    void openGallery(Consumer<Bitmap> onImageSelected);
}
