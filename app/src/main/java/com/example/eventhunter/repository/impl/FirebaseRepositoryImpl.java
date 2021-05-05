package com.example.eventhunter.repository.impl;

import com.example.eventhunter.repository.DocumentModelMapper;
import com.example.eventhunter.repository.FirebaseRepository;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.function.Consumer;

public class FirebaseRepositoryImpl<T> implements FirebaseRepository<T> {

    private final FirebaseFirestore firestore;

    public FirebaseRepositoryImpl() {
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void getDocument(String pathToDocument, Class<T> tclass, Consumer<T> consumer) {
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
    public void getAllDocuments(String pathToCollection, Class<T> tclass, Consumer<T> documentReceivedConsumer) {
        firestore.collection(pathToCollection).get().addOnCompleteListener(task -> {

            QuerySnapshot result = task.getResult();

            if (task.isSuccessful() && result != null) {

                for (QueryDocumentSnapshot documentSnapshot : result) {
                    T modelDTO = documentSnapshot.toObject(tclass);
                    documentReceivedConsumer.accept(modelDTO);
                }
            }
        });
    }

    @Override
    public void createDocument(String pathToCollection, T document, Consumer<String> updateStatus) {
        firestore.collection(pathToCollection).add(document)
                .addOnSuccessListener(documentReference -> updateStatus.accept(documentReference.getId()))
                .addOnFailureListener(e -> updateStatus.accept(null));
    }

    @Override
    public void updateDocument(String pathToDocument, T document, Consumer<Boolean> updateStatus) {
        Class<T> tclass = (Class<T>) document.getClass();
        DocumentModelMapper<T> documentModelMapper = new DocumentModelMapper<>(tclass);
        Map<String, Object> map = documentModelMapper.createMap(document);
        firestore.document(pathToDocument).update(map).addOnSuccessListener(e -> updateStatus.accept(true)).addOnFailureListener(e -> updateStatus.accept(false));
    }
}
