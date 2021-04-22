package com.example.eventhunter.events.service;

import android.app.Activity;
import android.graphics.Bitmap;

import com.example.eventhunter.events.createEventForm.EventFormViewModel;
import com.example.eventhunter.events.service.dto.EventCardDTO;
import com.example.eventhunter.events.service.dto.EventModelDTO;
import com.example.eventhunter.utils.photoUpload.FileUtil;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;

public class FirebaseEventService implements EventService {

    private static final String EVENTS_COLLECTION_PATH = "events";
    private static final String EVENTS_STORAGE_FOLDER_PATH = "events";

    private final Activity activity;
    private final FirebaseFirestore firestore;
    private final StorageReference eventsStorageReference;

    public FirebaseEventService(Activity activity) {
        this.activity = activity;
        firestore = FirebaseFirestore.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        eventsStorageReference = firebaseStorage.getReference(EVENTS_STORAGE_FOLDER_PATH);
    }

    @Override
    public void getEvent(String eventId, Observer<EventModelDTO> onEventReceived) {
        firestore.collection(EVENTS_COLLECTION_PATH).document(eventId).get().addOnSuccessListener(activity, documentSnapshot -> {
            if (documentSnapshot.exists()) {
                EventModelDTO eventModelDTO = documentSnapshot.toObject(EventModelDTO.class);
                onEventReceived.onChanged(eventModelDTO);
            } else {
                onEventReceived.onChanged(null);
            }
        }).addOnFailureListener(activity, e -> onEventReceived.onChanged(null));
    }

    @Override
    public void getEventPhoto(String eventId, Observer<Bitmap> onEventReceived) {
        eventsStorageReference.child(eventId).getDownloadUrl().addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {

                try {
                    Bitmap bitmap = FileUtil.bitmapFrom(activity, task.getResult());
                    onEventReceived.onChanged(bitmap);
                } catch (IOException e) {
                    onEventReceived.onChanged(null);
                    e.printStackTrace();
                }
                return;
            }
            onEventReceived.onChanged(null);
        });
    }

    @Override
    public void getAllEvents(Observer<List<EventCardDTO>> onEventsReceived) {
        List<EventCardDTO> allEventCards = new ArrayList<>();

        firestore.collection(EVENTS_COLLECTION_PATH).get().addOnCompleteListener(activity, task -> {

            QuerySnapshot result = task.getResult();

            if (task.isSuccessful() && result != null) {

                int resultSize = result.size();

                for (QueryDocumentSnapshot documentSnapshot : result) {
                    EventCardDTO eventCardDTO = documentSnapshot.toObject(EventCardDTO.class);

                    eventCardDTO.setEventId(documentSnapshot.getId());
                    getEventPhoto(documentSnapshot.getId(), bitmap -> {
                        eventCardDTO.setEventImage(bitmap);
                        allEventCards.add(eventCardDTO);

                        if (allEventCards.size() == resultSize) {
                            onEventsReceived.onChanged(allEventCards);
                        }
                    });
                }

                return;
            }

            onEventsReceived.onChanged(null);
        });
    }

    @Override
    public void getAllFutureEventsForUser(String userId, Observer<List<EventCardDTO>> onEventsReceived) {
        // todo filter for future events
        getAllEvents(onEventsReceived);
    }

    @Override
    public void getAllPastEventsForUser(String userId, Observer<List<EventCardDTO>> onEventsReceived) {
        // todo filter for past events
        getAllEvents(onEventsReceived);
    }

    @Override
    public void createEvent(EventFormViewModel model, String organizerName, Observer<Boolean> onEventCreated) {

        String ticketPriceString = getStringOr0(model.getEventTicketPrice().getValue());
        Double ticketPrice = Double.valueOf(ticketPriceString);

        String seatNumberString = getStringOr0(model.getEventSeatNumber().getValue());
        Integer seatNumber = Integer.valueOf(seatNumberString);

        EventModelDTO createEventModelDTO = new EventModelDTO(
                model.getEventName().getValue(), model.getEventDescription().getValue(),
                seatNumber, model.getEventLocation().getValue(),
                model.getEventType().getValue(), model.getEventStartDate().getValue(),
                model.getEventStartHour().getValue(), model.getEventEndHour().getValue(),
                ticketPrice, organizerName, model.getCollaboratorsDTO()
        );
        firestore.collection(EVENTS_COLLECTION_PATH).add(createEventModelDTO).addOnCompleteListener(activity, task -> {
            if (!task.isSuccessful()) {
                onEventCreated.onChanged(false);
            }
        }).continueWithTask(task -> {
            if (task.isSuccessful()) {

                Bitmap bitmap = model.getEventPhoto().getValue();
                if (bitmap == null) {
                    onEventCreated.onChanged(true);
                    return null;
                }

                DocumentReference result = task.getResult();
                if (result != null) {
                    String eventId = result.getId();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                    return eventsStorageReference.child(eventId).putBytes(byteArrayOutputStream.toByteArray());
                }
            }
            onEventCreated.onChanged(false);
            return null;
        }).continueWithTask(task -> {
            onEventCreated.onChanged(task.isSuccessful());
            return null;
        });
    }

    @NotNull
    private String getStringOr0(String seatNumberString) {
        if (seatNumberString == null || seatNumberString.isEmpty()) {
            seatNumberString = "0";
        }
        return seatNumberString;
    }
}
