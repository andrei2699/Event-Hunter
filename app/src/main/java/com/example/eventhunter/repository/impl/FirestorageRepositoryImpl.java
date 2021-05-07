package com.example.eventhunter.repository.impl;

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

    private final StorageReference storageReference;

    public FirestorageRepositoryImpl() {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    @Override
    public void getPhoto(String pathToPhoto, Consumer<Bitmap> consumer) {
        int length = pathToPhoto.length();
        if (pathToPhoto.substring(length - 4).equals("null") || pathToPhoto.contains("null")) {
            consumer.accept(null);
            return;
        }

        storageReference.child(pathToPhoto).getBytes(ONE_MEGABYTE).addOnCompleteListener(task -> {
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
        uploadTask.addOnFailureListener(e -> updateStatus.accept(false))
                .addOnSuccessListener(e -> updateStatus.accept(true));
    }
}
