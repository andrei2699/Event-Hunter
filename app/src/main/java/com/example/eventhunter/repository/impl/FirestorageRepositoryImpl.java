package com.example.eventhunter.repository.impl;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.eventhunter.repository.PhotoRepository;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.function.Consumer;

public class FirestorageRepositoryImpl implements PhotoRepository {
    private static final long ONE_MEGABYTE = 1024 * 1024;

    private final Activity activity;
    private final StorageReference storageReference;

    public FirestorageRepositoryImpl(Activity activity) {
        this.activity = activity;
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    @Override
    public void getPhoto(String pathToPhoto, Consumer<Bitmap> consumer) {
        storageReference.child(pathToPhoto).getBytes(ONE_MEGABYTE).addOnCompleteListener(activity, task -> {
            Bitmap bitmap = null;
            if (task.isSuccessful()) {
                byte[] bitmapBytes = task.getResult();
                if (bitmapBytes != null) {
                    bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                }
            }
            consumer.accept(bitmap);
        });
    }

    @Override
    public void updatePhoto(String pathToPhoto, Bitmap photo, Consumer<Boolean> updateStatus) {

        if (photo == null) {
            updateStatus.accept(true);
            return;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        storageReference.child(pathToPhoto).putBytes(byteArrayOutputStream.toByteArray());

        UploadTask uploadTask = storageReference.child(pathToPhoto).putBytes(byteArrayOutputStream.toByteArray());
        uploadTask.addOnFailureListener(activity, e -> updateStatus.accept(false))
                .addOnSuccessListener(activity, e -> updateStatus.accept(true));
    }
}
