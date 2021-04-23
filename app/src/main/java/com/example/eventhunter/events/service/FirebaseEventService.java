package com.example.eventhunter.events.service;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.eventhunter.events.createEventForm.EventFormViewModel;
import com.example.eventhunter.events.service.dto.EventCardDTO;
import com.example.eventhunter.events.service.dto.EventModelDTO;
import com.example.eventhunter.ui.mainPage.events.card.EventCard;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

import androidx.lifecycle.Observer;

public class FirebaseEventService implements EventService {

    private static final String EVENTS_COLLECTION_PATH = "events";
    private static final String EVENTS_STORAGE_FOLDER_PATH = "events";
    private static final long ONE_MEGABYTE = 1024 * 1024;

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
        eventsStorageReference.child(eventId).getBytes(ONE_MEGABYTE).addOnCompleteListener(activity, task -> {
            Bitmap bitmap = null;
            if (task.isSuccessful()) {
                byte[] bitmapBytes = task.getResult();
                if (bitmapBytes != null) {
                    bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                }
            }
            onEventReceived.onChanged(bitmap);
        });
    }

    @Override
    public void getAllEventCards(Observer<EventCard> onEventReceived) {

        firestore.collection(EVENTS_COLLECTION_PATH).get().addOnCompleteListener(activity, task -> {

            QuerySnapshot result = task.getResult();

            if (task.isSuccessful() && result != null) {

                for (QueryDocumentSnapshot documentSnapshot : result) {
                    EventCardDTO eventCardDTO = documentSnapshot.toObject(EventCardDTO.class);

                    eventCardDTO.setEventId(documentSnapshot.getId());
                    getEventPhoto(documentSnapshot.getId(), bitmap -> {
                        eventCardDTO.setEventImage(bitmap);

                        EventCard eventCard = new EventCard(eventCardDTO.getEventId(), eventCardDTO.getEventName(),
                                eventCardDTO.getOrganizerName(), eventCardDTO.getEventDate(),
                                eventCardDTO.getEventLocation(), eventCardDTO.getTicketPrice(),
                                eventCardDTO.getEventSeatNumber(), eventCardDTO.getEventImage());

                        onEventReceived.onChanged(eventCard);
                    });
                }
            }
        });
    }

    @Override
    public void getAllFutureEventCardsForUser(String userId, Observer<EventCard> onEventReceived) {
        // todo filter for future events
        getAllEventCards(onEventReceived);
    }

    @Override
    public void getAllPastEventCardsForUser(String userId, Observer<EventCard> onEventReceived) {
        // todo filter for past events
        getAllEventCards(onEventReceived);
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
