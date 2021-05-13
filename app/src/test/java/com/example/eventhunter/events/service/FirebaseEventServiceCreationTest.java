package com.example.eventhunter.events.service;

import android.graphics.Bitmap;

import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;
import com.example.eventhunter.events.models.EventModel;
import com.example.eventhunter.events.service.dto.EventModelDTO;
import com.example.eventhunter.events.service.dto.UpdatableEventModelDTO;
import com.example.eventhunter.repository.PhotoRepository;
import com.example.eventhunter.repository.impl.FirebaseRepositoryImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseEventServiceCreationTest {

    @Mock
    private FirebaseRepositoryImpl<EventModelDTO> eventModelDTOFirebaseRepository;
    @Mock
    private FirebaseRepositoryImpl<UpdatableEventModelDTO> updatableEventModelDTOFirebaseRepository;
    @Mock
    private PhotoRepository photoRepository;

    @Captor
    private ArgumentCaptor<Consumer<EventModelDTO>> eventModelDTOConsumerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Consumer<Bitmap>> bitmapArgumentCaptorConsumer;

    @Captor
    private ArgumentCaptor<Consumer<Boolean>> booleanArgumentCaptorConsumer;

    @Captor
    private ArgumentCaptor<Consumer<Boolean>> booleanArgumentCaptorConsumer2;

    @Captor
    private ArgumentCaptor<Consumer<String>> stringArgumentCaptorConsumer;

    private FirebaseEventService firebaseEventService;

    @Before
    public void setUp() throws Exception {
        firebaseEventService = new FirebaseEventService(eventModelDTOFirebaseRepository, updatableEventModelDTOFirebaseRepository, photoRepository);
    }

    @After
    public void tearDown() throws Exception {
        firebaseEventService = null;
    }

    @Test
    public void createOneTimeEvent_withModel_withPhoto_noCollaborators() {
        String pathToCollection = "events/hriuf237";
        String pathToPhotos = "events/hriuf237";

        Bitmap eventPhoto = Mockito.mock(Bitmap.class);

        EventModel eventModel = new EventModel("First Event Name", "First Event Description", 120,
                "The Park", "One Time Event", "12/06/2021", "14/06/2021", "16", "23",
                50.0, "organizerId", "Organizer Name", new ArrayList<>(), eventPhoto);

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.createOneTimeEvent(eventModel, booleanConsumer -> {
            methodCalled.set(true);

            assertTrue(booleanConsumer);
        });

        verify(eventModelDTOFirebaseRepository, times(1)).createDocument(eq("events"), argThat(eventModelDTO -> {
            return eventModel.eventName.equals(eventModelDTO.eventName) && eventModel.eventLocation.equals(eventModelDTO.eventLocation) && eventModel.eventDescription.equals(eventModelDTO.eventDescription) &&
                    eventModel.eventStartHour.equals(eventModelDTO.eventStartHour) && eventModel.eventEndHour.equals(eventModelDTO.eventEndHour) && eventModel.eventStartDate.equals(eventModelDTO.eventStartDate) &&
                    eventModel.eventEndDate.equals(eventModelDTO.eventEndDate) && eventModel.ticketPrice.equals(eventModelDTO.ticketPrice) && eventModel.eventSeatNumber == eventModelDTO.eventSeatNumber &&
                    eventModel.organizerId.equals(eventModelDTO.organizerId) && eventModel.organizerName.equals(eventModelDTO.organizerName);
        }), stringArgumentCaptorConsumer.capture());

        Consumer<String> createConsumer = stringArgumentCaptorConsumer.getValue();

        createConsumer.accept("hriuf237");

        verify(photoRepository, times(1)).updatePhoto(eq(pathToPhotos), eq(eventPhoto), booleanArgumentCaptorConsumer.capture());

        Consumer<Boolean> updatePhotoConsumer = booleanArgumentCaptorConsumer.getValue();

        updatePhotoConsumer.accept(true);

        verify(eventModelDTOFirebaseRepository, times(1)).updateDocument(eq(pathToCollection), argThat(eventModelDTO -> {
            return eventModelDTO.eventId.equals("hriuf237");
        }), booleanArgumentCaptorConsumer2.capture());

        Consumer<Boolean> updateDocumentConsumer = booleanArgumentCaptorConsumer2.getValue();

        updateDocumentConsumer.accept(true);

        assertTrue(methodCalled.get());
    }

    @Test
    public void createOneTimeEvent_withModel_withPhoto_withCollaborators() {
        String pathToCollection = "events/asbeg123";
        String pathToPhotos = "events/asbeg123";

        CollaboratorHeader firstCollaboratorHeader = new CollaboratorHeader("firstCollaborator", "First Collaborator");
        CollaboratorHeader secondCollaboratorHeader = new CollaboratorHeader("secondCollaborator", "Second Collaborator");

        List<CollaboratorHeader> collaboratorHeaderList = new ArrayList<>();
        collaboratorHeaderList.add(firstCollaboratorHeader);
        collaboratorHeaderList.add(secondCollaboratorHeader);


        EventModel eventModel = new EventModel("First Event Name", "First Event Description", 120,
                "The Park", "One Time Event", "12/06/2021", "14/06/2021", "16", "23",
                50.0, "organizerId", "Organizer Name", collaboratorHeaderList, null);

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.createOneTimeEvent(eventModel, booleanConsumer -> {
            methodCalled.set(true);

            assertTrue(booleanConsumer);
        });

        verify(eventModelDTOFirebaseRepository, times(1)).createDocument(eq("events"), argThat(eventModelDTO -> {
            return eventModel.eventName.equals(eventModelDTO.eventName) && eventModel.eventLocation.equals(eventModelDTO.eventLocation) && eventModel.eventDescription.equals(eventModelDTO.eventDescription) &&
                    eventModel.eventStartHour.equals(eventModelDTO.eventStartHour) && eventModel.eventEndHour.equals(eventModelDTO.eventEndHour) && eventModel.eventStartDate.equals(eventModelDTO.eventStartDate) &&
                    eventModel.eventEndDate.equals(eventModelDTO.eventEndDate) && eventModel.ticketPrice.equals(eventModelDTO.ticketPrice) && eventModel.eventSeatNumber == eventModelDTO.eventSeatNumber &&
                    eventModel.organizerId.equals(eventModelDTO.organizerId) && eventModel.organizerName.equals(eventModelDTO.organizerName) && eventModel.collaborators.equals(eventModelDTO.collaborators);
        }), stringArgumentCaptorConsumer.capture());

        Consumer<String> createConsumer = stringArgumentCaptorConsumer.getValue();

        createConsumer.accept("asbeg123");

        verify(photoRepository, times(1)).updatePhoto(eq(pathToPhotos), nullable(Bitmap.class), booleanArgumentCaptorConsumer.capture());

        Consumer<Boolean> updatePhotoConsumer = booleanArgumentCaptorConsumer.getValue();

        updatePhotoConsumer.accept(true);

        verify(eventModelDTOFirebaseRepository, times(1)).updateDocument(eq(pathToCollection), argThat(eventModelDTO -> {
            return eventModelDTO.eventId.equals("asbeg123");
        }), booleanArgumentCaptorConsumer2.capture());

        Consumer<Boolean> updateDocumentConsumer = booleanArgumentCaptorConsumer2.getValue();

        updateDocumentConsumer.accept(true);

        assertTrue(methodCalled.get());
    }

}