package com.example.eventhunter.events.service;

import android.graphics.Bitmap;

import com.example.eventhunter.events.models.EventModel;
import com.example.eventhunter.events.models.RepeatableEventModel;
import com.example.eventhunter.events.service.dto.EventModelDTO;
import com.example.eventhunter.repository.EventArrayOccurrenceTransmitter;
import com.example.eventhunter.repository.EventOccurrenceTransmitter;
import com.example.eventhunter.repository.FirebaseRepository;
import com.example.eventhunter.repository.PhotoRepository;
import com.example.eventhunter.utils.DateVerifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FirebaseEventService implements EventService {

    private static final String EVENTS_COLLECTION_PATH = "events";
    private static final String EVENTS_STORAGE_FOLDER_PATH = "events";

    private final FirebaseRepository<EventModelDTO> eventCardDTOFirebaseRepository;
    private final PhotoRepository photoRepository;

    public FirebaseEventService(FirebaseRepository<EventModelDTO> eventCardDTOFirebaseRepository, PhotoRepository photoRepository) {
        this.eventCardDTOFirebaseRepository = eventCardDTOFirebaseRepository;
        this.photoRepository = photoRepository;
    }

    @Override
    public void getEvent(String eventId, Consumer<EventModel> onEventReceived) {
        String documentPath = EVENTS_COLLECTION_PATH + "/" + eventId;
        String photoPath = EVENTS_STORAGE_FOLDER_PATH + "/" + eventId;
        EventModel eventModel = new EventModel();

        Consumer<Bitmap> bitmapConsumer = bitmap -> eventModel.eventPhoto = bitmap;
        Consumer<EventModelDTO> eventModelDTOConsumer = eventModel::set;

        EventOccurrenceTransmitter<Bitmap, EventModelDTO> transmitter = new EventOccurrenceTransmitter<>(bitmapConsumer, eventModelDTOConsumer);
        transmitter.waitAsyncEvents(() -> onEventReceived.accept(eventModel));

        photoRepository.getPhoto(photoPath, transmitter.firstEventConsumer);
        eventCardDTOFirebaseRepository.getDocument(documentPath, EventModelDTO.class, transmitter.secondEventConsumer);
    }

    @Override
    public void getAllFutureEvents(Consumer<EventModel> onEventReceived) {
        getAllEvents(eventModelDTO -> DateVerifier.dateInTheFuture(eventModelDTO.eventStartDate), onEventReceived);
    }

    @Override
    public void getAllFutureEventCardsForUser(String userId, Consumer<EventModel> onEventReceived) {
        getAllEvents(eventModelDTO -> (userId.equals(eventModelDTO.organizerId) ||
                        eventModelDTO.collaborators.stream().anyMatch(collaboratorHeader -> collaboratorHeader.getCollaboratorName().equals(userId)))
                        && DateVerifier.dateInTheFuture(eventModelDTO.eventStartDate),
                onEventReceived);
    }

    @Override
    public void getAllPastEventCardsForUser(String userId, Consumer<EventModel> onEventReceived) {
        getAllEvents(eventModelDTO -> (userId.equals(eventModelDTO.organizerId) ||
                        eventModelDTO.collaborators.stream().anyMatch(collaboratorHeader -> collaboratorHeader.getCollaboratorName().equals(userId)))
                        && DateVerifier.dateInThePast(eventModelDTO.eventStartDate),
                onEventReceived);
    }

    @Override
    public void createOneTimeEvent(EventModel model, Consumer<Boolean> onEventCreated) {

        EventModelDTO createEventModelDTO = new EventModelDTO(
                model.eventName, model.eventDescription, model.eventSeatNumber, model.eventLocation,
                model.eventType, model.eventStartDate, model.eventEndDate, model.eventStartHour,
                model.eventEndHour, model.ticketPrice, model.organizerId, model.organizerName, model.collaborators
        );

        eventCardDTOFirebaseRepository.createDocument(EVENTS_COLLECTION_PATH, createEventModelDTO, eventId -> {
            if (eventId == null) {
                onEventCreated.accept(false);
                return;
            }
            String photoPath = EVENTS_STORAGE_FOLDER_PATH + "/" + eventId;
            String documentPath = EVENTS_COLLECTION_PATH + "/" + eventId;
            createEventModelDTO.eventId = eventId;

            AtomicBoolean eventCreatedSuccessfully = new AtomicBoolean(true);

            Consumer<Boolean> booleanConsumer = aBoolean -> eventCreatedSuccessfully.set(eventCreatedSuccessfully.get() && aBoolean);
            Consumer<Boolean> booleanConsumer1 = aBoolean -> eventCreatedSuccessfully.set(eventCreatedSuccessfully.get() && aBoolean);

            EventOccurrenceTransmitter<Boolean, Boolean> transmitter = new EventOccurrenceTransmitter<>(booleanConsumer, booleanConsumer1);
            transmitter.waitAsyncEvents(() -> onEventCreated.accept(eventCreatedSuccessfully.get()));

            photoRepository.updatePhoto(photoPath, model.eventPhoto, transmitter.firstEventConsumer);
            eventCardDTOFirebaseRepository.updateDocument(documentPath, createEventModelDTO, transmitter.secondEventConsumer);
        });
    }

    @Override
    public void createRepeatableEvent(RepeatableEventModel model, Consumer<Boolean> onEventCreated) {
        if (model.repetitions <= 0) {
            onEventCreated.accept(true);
            return;
        }

        Consumer<Boolean>[] consumers = new Consumer[model.repetitions];

        AtomicBoolean eventsCreatedSuccessfully = new AtomicBoolean(true);

        for (int i = 0; i < model.repetitions; i++) {
            consumers[i] = (aBoolean -> eventsCreatedSuccessfully.set(eventsCreatedSuccessfully.get() && aBoolean));
        }

        EventArrayOccurrenceTransmitter<Boolean> eventArrayOccurrenceTransmitter = new EventArrayOccurrenceTransmitter<>(consumers);
        eventArrayOccurrenceTransmitter.waitAsyncEvents(() -> onEventCreated.accept(eventsCreatedSuccessfully.get()));

        for (int i = 0; i < model.repetitions; i++) {
            this.createOneTimeEvent(model, consumers[i]);

            model.eventStartDate = getDateOverOneWeek(model.eventStartDate);
            model.eventEndDate = getDateOverOneWeek(model.eventEndDate);
        }
    }

    private void getAllEvents(Predicate<EventModelDTO> filterPredicate, Consumer<EventModel> onEventReceived) {
        eventCardDTOFirebaseRepository.getAllDocuments(EVENTS_COLLECTION_PATH, EventModelDTO.class, modelDTO -> {
            if (modelDTO.eventId != null && !modelDTO.eventId.isEmpty()) {
                if (filterPredicate.test(modelDTO)) {
                    String photoPath = EVENTS_STORAGE_FOLDER_PATH + "/" + modelDTO.eventId;
                    photoRepository.getPhoto(photoPath, bitmap -> {
                        EventModel model = new EventModel(modelDTO);
                        model.eventPhoto = bitmap;
                        onEventReceived.accept(model);
                    });
                }
            }
        });
    }

    private String getDateOverOneWeek(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (parsedDate == null) {
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedDate);

        calendar.add(Calendar.DATE, 7);

        return sdf.format(calendar.getTime());
    }
}
