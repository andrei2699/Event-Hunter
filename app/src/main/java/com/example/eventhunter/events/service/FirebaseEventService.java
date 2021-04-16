package com.example.eventhunter.events.service;

import android.app.Activity;

import com.example.eventhunter.events.createEventForm.EventFormViewModel;
import com.example.eventhunter.events.service.dto.EventModelDTO;
import com.example.eventhunter.repository.DocumentModelMapper;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.Observer;

public class FirebaseEventService implements EventService {

    private static final String EVENTS_COLLECTION_PATH = "events";

    private final Activity activity;
    private final FirebaseFirestore firestore;

    public FirebaseEventService(Activity activity) {
        this.activity = activity;
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void getEvent(String eventId, Observer<EventFormViewModel> onEventReceived) {
        firestore.collection(EVENTS_COLLECTION_PATH).document(eventId).get().addOnSuccessListener(activity, documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Map<String, Object> data = documentSnapshot.getData();
                DocumentModelMapper<EventModelDTO> documentModelMapper = new DocumentModelMapper<>(EventModelDTO.class);
                try {
                    EventModelDTO modelDTO = documentModelMapper.getModel(data);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
//                onEventReceived.onChanged(firebaseDocumentMapper.);
            } else {
                onEventReceived.onChanged(null);
            }
        })
                .addOnFailureListener(activity, e -> onEventReceived.onChanged(null));
    }

    @Override
    public void getAllEvents(Observer<List<EventFormViewModel>> onEventsReceived) {

    }
}
