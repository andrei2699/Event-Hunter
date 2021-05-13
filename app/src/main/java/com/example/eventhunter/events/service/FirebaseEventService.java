package com.example.eventhunter.events.service;

import android.graphics.Bitmap;

import com.example.eventhunter.events.models.EventCard;
import com.example.eventhunter.events.models.EventModel;
import com.example.eventhunter.events.models.RepeatableEventModel;
import com.example.eventhunter.events.service.dto.EventModelDTO;
import com.example.eventhunter.events.service.dto.UpdatableEventModelDTO;
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

    private final FirebaseRepository<EventModelDTO> eventModelDTOFirebaseRepository;
    private final FirebaseRepository<UpdatableEventModelDTO> updatableEventModelDTOFirebaseRepository;
    private final PhotoRepository photoRepository;

    public FirebaseEventService(FirebaseRepository<EventModelDTO> eventModelDTOFirebaseRepository, FirebaseRepository<UpdatableEventModelDTO> updatableEventModelDTOFirebaseRepository, PhotoRepository photoRepository) {
        this.eventModelDTOFirebaseRepository = eventModelDTOFirebaseRepository;
        this.updatableEventModelDTOFirebaseRepository = updatableEventModelDTOFirebaseRepository;
        this.photoRepository = photoRepository;
    }

    @Override
    public void getEventAllDetails(String eventId, Consumer<EventModel> onEventReceived) {
        String documentPath = EVENTS_COLLECTION_PATH + "/" + eventId;
        String photoPath = EVENTS_STORAGE_FOLDER_PATH + "/" + eventId;
        EventModel eventModel = new EventModel();

        Consumer<Bitmap> bitmapConsumer = bitmap -> {
            if (bitmap != null) {
                eventModel.eventPhoto = bitmap;
            } else {
                eventModel.eventPhoto = null;
            }
        };

        Consumer<EventModelDTO> eventModelDTOConsumer = eventModelDTO -> {
            if (eventModelDTO != null) {
                eventModel.eventId = eventModelDTO.eventId;
                eventModel.eventName = eventModelDTO.eventName;
                eventModel.eventDescription = eventModelDTO.eventDescription;
                eventModel.eventType = eventModelDTO.eventType;
                eventModel.eventLocation = eventModelDTO.eventLocation;
                eventModel.eventStartDate = eventModelDTO.eventStartDate;
                eventModel.eventEndDate = eventModelDTO.eventEndDate;
                eventModel.eventStartHour = eventModelDTO.eventStartHour;
                eventModel.eventEndHour = eventModelDTO.eventEndHour;
                eventModel.eventSeatNumber = eventModelDTO.eventSeatNumber;
                eventModel.ticketPrice = eventModelDTO.ticketPrice;
                eventModel.organizerId = eventModelDTO.organizerId;
                eventModel.organizerName = eventModelDTO.organizerName;
                eventModel.collaborators = eventModelDTO.collaborators;
            } else {
                eventModel.eventId = "";
                eventModel.eventName = "";
                eventModel.eventDescription = "";
                eventModel.eventType = "";
                eventModel.eventLocation = "";
                eventModel.eventStartDate = "";
                eventModel.eventEndDate = "";
                eventModel.eventStartHour = "";
                eventModel.eventEndHour = "";
                eventModel.eventSeatNumber = 0;
                eventModel.ticketPrice = 0.0;
                eventModel.organizerId = "";
                eventModel.organizerName = "";
                eventModel.collaborators = null;
            }
        };

        EventOccurrenceTransmitter<Bitmap, EventModelDTO> transmitter = new EventOccurrenceTransmitter<>(bitmapConsumer, eventModelDTOConsumer);
        transmitter.waitAsyncEvents(() -> onEventReceived.accept(eventModel));

        photoRepository.getPhoto(photoPath, transmitter.firstEventConsumer);
        eventModelDTOFirebaseRepository.getDocument(documentPath, EventModelDTO.class, transmitter.secondEventConsumer);
    }

    @Override
    public void getAllFutureEventsCards(Consumer<EventCard> onEventReceived) {
        getAllEvents(eventModelDTO -> DateVerifier.dateInTheFuture(eventModelDTO.eventStartDate), onEventReceived);
    }

    @Override
    public void getAllFutureEventCardsForUser(String userId, Consumer<EventCard> onEventReceived) {
        getAllEvents(eventModelDTO -> (userId.equals(eventModelDTO.organizerId) ||
                        (eventModelDTO.collaborators != null && eventModelDTO.collaborators.stream().anyMatch(collaboratorHeader -> collaboratorHeader.getCollaboratorId().equals(userId))))
                        && DateVerifier.dateInTheFuture(eventModelDTO.eventStartDate),
                onEventReceived);
    }

    @Override
    public void getAllPastEventCardsForUser(String userId, Consumer<EventCard> onEventReceived) {
        getAllEvents(eventModelDTO -> (userId.equals(eventModelDTO.organizerId) ||
                        (eventModelDTO.collaborators != null && eventModelDTO.collaborators.stream().anyMatch(collaboratorHeader -> collaboratorHeader.getCollaboratorId().equals(userId))))
                        && DateVerifier.dateInThePast(eventModelDTO.eventStartDate),
                onEventReceived);
    }

    @Override
    public void createOneTimeEvent(EventModel model, Consumer<Boolean> onEventCreated) {

        EventModelDTO createEventModelDTO = new EventModelDTO(
                model.eventName, model.eventDescription, model.eventSeatNumber, model.eventLocation,
                model.eventType, model.eventStartDate, model.eventEndDate, model.eventStartHour,
                model.eventEndHour, model.ticketPrice, model.organizerId, model.organizerName, model.collaborators);

        eventModelDTOFirebaseRepository.createDocument(EVENTS_COLLECTION_PATH, createEventModelDTO, eventId -> {
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
            eventModelDTOFirebaseRepository.updateDocument(documentPath, createEventModelDTO, transmitter.secondEventConsumer);
        });
    }

    @Override
    public void createRepeatableEvent(RepeatableEventModel
                                              model, Consumer<Boolean> onEventCreated) {
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

    @Override
    public void updateEvent(String id, EventModel model, Consumer<Boolean> updateConsumer) {
        if(model == null) {
            updateConsumer.accept(false);
            return;
        }

        String completeDocumentPath = EVENTS_COLLECTION_PATH + "/" + id;

        UpdatableEventModelDTO updatableEventModelDTO = new UpdatableEventModelDTO();
        updatableEventModelDTO.eventSeatNumber = model.eventSeatNumber;

        this.updatableEventModelDTOFirebaseRepository.updateDocument(completeDocumentPath, updatableEventModelDTO, updateConsumer);
    }

    private void getAllEvents(Predicate<EventModelDTO> filterPredicate, Consumer<EventCard> onEventReceived) {
        eventModelDTOFirebaseRepository.getAllDocuments(EVENTS_COLLECTION_PATH, EventModelDTO.class, modelDTO -> {
            if (modelDTO.eventId != null && !modelDTO.eventId.isEmpty()) {
                if (filterPredicate.test(modelDTO)) {
                    String photoPath = EVENTS_STORAGE_FOLDER_PATH + "/" + modelDTO.eventId;
                    photoRepository.getPhoto(photoPath, bitmap ->
                            onEventReceived.accept(new EventCard(modelDTO.eventId, modelDTO.eventName,
                                    modelDTO.organizerName, modelDTO.eventStartDate,
                                    modelDTO.eventLocation, modelDTO.ticketPrice, modelDTO.eventSeatNumber,
                                    bitmap)));
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
