package com.example.eventhunter.repository.impl;

import android.app.Activity;

import com.example.eventhunter.repository.FirebaseRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.function.Consumer;

public class FirebaseRepositoryImpl<T> implements FirebaseRepository<T> {

    private final FirebaseFirestore firestore;

    public FirebaseRepositoryImpl() {
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void getDocument(String pathToDocument, Consumer<T> consumer, Class<T> tclass) {
        firestore.document(pathToDocument).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                T modelDTO = documentSnapshot.toObject(tclass);
                consumer.accept(modelDTO);
            } else {
                consumer.accept(null);
            }
        }).addOnFailureListener(e -> consumer.accept(null));
    }

    @Override
    public void createDocument(String pathToCollection, T document, Consumer<Boolean> updateStatus) {
        firestore.collection(pathToCollection).add(document).addOnSuccessListener(e -> updateStatus.accept(true)).addOnFailureListener(e -> updateStatus.accept(false));
    }

    @Override
    public void updateDocument(String pathToDocument, T document, Consumer<Boolean> updateStatus) {
        firestore.document(pathToDocument).set(document).addOnSuccessListener(e -> updateStatus.accept(true)).addOnFailureListener(e -> updateStatus.accept(false));
    }
}
