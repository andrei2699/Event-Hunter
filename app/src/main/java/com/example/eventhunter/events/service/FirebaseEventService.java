package com.example.eventhunter.events.service;

import android.graphics.Bitmap;

import com.example.eventhunter.events.createEventForm.EventFormViewModel;
import com.example.eventhunter.events.models.EventModel;
import com.example.eventhunter.events.service.dto.EventModelDTO;
import com.example.eventhunter.repository.EventOccurrenceTransmitter;
import com.example.eventhunter.repository.FirebaseRepository;
import com.example.eventhunter.repository.PhotoRepository;
import com.example.eventhunter.utils.DateVerifier;

import org.jetbrains.annotations.NotNull;

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
    public void createOneTimeEvent(EventFormViewModel model, String organizerId, String organizerName, Consumer<Boolean> onEventCreated) {

        String ticketPriceString = getStringOr0(model.getEventTicketPrice().getValue());
        Double ticketPrice = Double.valueOf(ticketPriceString);

        String seatNumberString = getStringOr0(model.getEventSeatNumber().getValue());
        Integer seatNumber = Integer.valueOf(seatNumberString);

        EventModelDTO createEventModelDTO = new EventModelDTO(
                model.getEventName().getValue(), model.getEventDescription().getValue(),
                seatNumber, model.getEventLocation().getValue(),
                model.getEventType().getValue(), model.getEventStartDate().getValue(),
                model.getEventEndDate().getValue(), model.getEventStartHour().getValue(), model.getEventEndHour().getValue(),
                ticketPrice, organizerId, organizerName, model.getCollaboratorsDTO()
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

            photoRepository.updatePhoto(photoPath, model.getEventPhoto().getValue(), transmitter.firstEventConsumer);
            eventCardDTOFirebaseRepository.updateDocument(documentPath, createEventModelDTO, transmitter.secondEventConsumer);
        });
    }

    @Override
    public void createRepeatableEvent(EventFormViewModel model, String organizerId, String organizerName, Consumer<Boolean> onEventCreated) {
        throw new RuntimeException("Not Implemented");
    }

    @NotNull
    private String getStringOr0(String seatNumberString) {
        if (seatNumberString == null || seatNumberString.isEmpty()) {
            seatNumberString = "0";
        }
        return seatNumberString;
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
}
