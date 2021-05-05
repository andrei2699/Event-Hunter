package com.example.eventhunter.repository;

import java.util.function.Consumer;

public interface FirebaseRepository<T> {
    void getDocument(String pathToDocument, Class<T> tClass, Consumer<T> consumer);

    void getAllDocuments(String pathToCollection, Class<T> tclass, Consumer<T> documentReceivedConsumer);

    void createDocument(String pathToCollection, T document, Consumer<String> updateStatus);

    void updateDocument(String pathToDocument, T document, Consumer<Boolean> updateStatus);
}
