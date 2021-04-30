package com.example.eventhunter.repository;

import java.util.function.Consumer;

public interface FirebaseRepository<T> {
    void getDocument(String pathToDocument, Consumer<T> consumer, Class<T> tClass);

    void createDocument(String pathToCollection, T document,  Consumer<Boolean> updateStatus);

    void updateDocument(String pathToDocument, T document, Consumer<Boolean> updateStatus);
}
