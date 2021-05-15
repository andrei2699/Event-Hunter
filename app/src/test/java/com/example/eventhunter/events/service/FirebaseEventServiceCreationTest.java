package com.example.eventhunter.events.service;

import android.graphics.Bitmap;

import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;
import com.example.eventhunter.events.models.EventModel;
import com.example.eventhunter.events.models.RepeatableEventModel;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
    public void createOneTimeEvent_withModel_noPhoto_withCollaborators() {
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

    @Test
    public void createOneTimeEvent_withModel_noPhoto_noCollaborators() {
        String pathToCollection = "events/iuhn76";
        String pathToPhotos = "events/iuhn76";

        EventModel eventModel = new EventModel("First Event Name", "First Event Description", 120,
                "The Park", "One Time Event", "12/06/2021", "14/06/2021", "16", "23",
                50.0, "organizerId", "Organizer Name", null, null);

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.createOneTimeEvent(eventModel, booleanConsumer -> {
            methodCalled.set(true);

            assertTrue(booleanConsumer);
        });

        verify(eventModelDTOFirebaseRepository, times(1)).createDocument(eq("events"), argThat(eventModelDTO -> {
            return eventModel.eventName.equals(eventModelDTO.eventName) && eventModel.eventLocation.equals(eventModelDTO.eventLocation) && eventModel.eventDescription.equals(eventModelDTO.eventDescription) &&
                    eventModel.eventStartHour.equals(eventModelDTO.eventStartHour) && eventModel.eventEndHour.equals(eventModelDTO.eventEndHour) && eventModel.eventStartDate.equals(eventModelDTO.eventStartDate) &&
                    eventModel.eventEndDate.equals(eventModelDTO.eventEndDate) && eventModel.ticketPrice.equals(eventModelDTO.ticketPrice) && eventModel.eventSeatNumber == eventModelDTO.eventSeatNumber &&
                    eventModel.organizerId.equals(eventModelDTO.organizerId) && eventModel.organizerName.equals(eventModelDTO.organizerName) && eventModelDTO.collaborators == null;
        }), stringArgumentCaptorConsumer.capture());

        Consumer<String> createConsumer = stringArgumentCaptorConsumer.getValue();

        createConsumer.accept("iuhn76");

        verify(photoRepository, times(1)).updatePhoto(eq(pathToPhotos), nullable(Bitmap.class), booleanArgumentCaptorConsumer.capture());

        Consumer<Boolean> updatePhotoConsumer = booleanArgumentCaptorConsumer.getValue();

        updatePhotoConsumer.accept(true);

        verify(eventModelDTOFirebaseRepository, times(1)).updateDocument(eq(pathToCollection), argThat(eventModelDTO -> {
            return eventModelDTO.eventId.equals("iuhn76");
        }), booleanArgumentCaptorConsumer2.capture());

        Consumer<Boolean> updateDocumentConsumer = booleanArgumentCaptorConsumer2.getValue();

        updateDocumentConsumer.accept(true);

        assertTrue(methodCalled.get());
    }

    @Test
    public void createOneTimeEvent_withModel_noPhoto_noCollaborators_nullId() {
        String pathToCollection = "events/iuhn76";
        String pathToPhotos = "events/iuhn76";

        EventModel eventModel = new EventModel("First Event Name", "First Event Description", 120,
                "The Park", "One Time Event", "12/06/2021", "14/06/2021", "16", "23",
                50.0, "organizerId", "Organizer Name", null, null);

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.createOneTimeEvent(eventModel, booleanConsumer -> {
            methodCalled.set(true);

            assertFalse(booleanConsumer);
        });

        verify(eventModelDTOFirebaseRepository, times(1)).createDocument(eq("events"), argThat(eventModelDTO -> {
            return eventModel.eventName.equals(eventModelDTO.eventName) && eventModel.eventLocation.equals(eventModelDTO.eventLocation) && eventModel.eventDescription.equals(eventModelDTO.eventDescription) &&
                    eventModel.eventStartHour.equals(eventModelDTO.eventStartHour) && eventModel.eventEndHour.equals(eventModelDTO.eventEndHour) && eventModel.eventStartDate.equals(eventModelDTO.eventStartDate) &&
                    eventModel.eventEndDate.equals(eventModelDTO.eventEndDate) && eventModel.ticketPrice.equals(eventModelDTO.ticketPrice) && eventModel.eventSeatNumber == eventModelDTO.eventSeatNumber &&
                    eventModel.organizerId.equals(eventModelDTO.organizerId) && eventModel.organizerName.equals(eventModelDTO.organizerName) && eventModelDTO.collaborators == null;
        }), stringArgumentCaptorConsumer.capture());

        Consumer<String> createConsumer = stringArgumentCaptorConsumer.getValue();

        createConsumer.accept(null);

        assertTrue(methodCalled.get());
    }

    @Test
    public void createOneTimeEvent_noModel() {

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.createOneTimeEvent(null, booleanConsumer -> {
            methodCalled.set(true);

            assertFalse(booleanConsumer);
        });

        assertTrue(methodCalled.get());
    }

    @Test
    public void createOneTimeEvent_emptyModel() {
        String pathToCollection = "events/empMod";
        String pathToPhotos = "events/empMod";

        EventModel eventModel = new EventModel("", "", 0,
                "", "", "", "", "", "",
                0.0, "", "", null, null);

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.createOneTimeEvent(eventModel, booleanConsumer -> {
            methodCalled.set(true);

            assertTrue(booleanConsumer);
        });

        verify(eventModelDTOFirebaseRepository, times(1)).createDocument(eq("events"), argThat(eventModelDTO -> {
            return eventModel.eventName.equals(eventModelDTO.eventName) && eventModel.eventLocation.equals(eventModelDTO.eventLocation) && eventModel.eventDescription.equals(eventModelDTO.eventDescription) &&
                    eventModel.eventStartHour.equals(eventModelDTO.eventStartHour) && eventModel.eventEndHour.equals(eventModelDTO.eventEndHour) && eventModel.eventStartDate.equals(eventModelDTO.eventStartDate) &&
                    eventModel.eventEndDate.equals(eventModelDTO.eventEndDate) && eventModel.ticketPrice.equals(eventModelDTO.ticketPrice) && eventModel.eventSeatNumber == eventModelDTO.eventSeatNumber &&
                    eventModel.organizerId.equals(eventModelDTO.organizerId) && eventModel.organizerName.equals(eventModelDTO.organizerName) && eventModelDTO.collaborators == null;
        }), stringArgumentCaptorConsumer.capture());

        Consumer<String> createConsumer = stringArgumentCaptorConsumer.getValue();

        createConsumer.accept("empMod");

        verify(photoRepository, times(1)).updatePhoto(eq(pathToPhotos), nullable(Bitmap.class), booleanArgumentCaptorConsumer.capture());

        Consumer<Boolean> updatePhotoConsumer = booleanArgumentCaptorConsumer.getValue();

        updatePhotoConsumer.accept(true);

        verify(eventModelDTOFirebaseRepository, times(1)).updateDocument(eq(pathToCollection), argThat(eventModelDTO -> {
            return eventModelDTO.eventId.equals("empMod");
        }), booleanArgumentCaptorConsumer2.capture());

        Consumer<Boolean> updateDocumentConsumer = booleanArgumentCaptorConsumer2.getValue();

        updateDocumentConsumer.accept(true);

        assertTrue(methodCalled.get());
    }

    @Test
    public void createRepeatableEvent_zeroRepetitions() {

        RepeatableEventModel repeatableEventModel = new RepeatableEventModel("First Event Name", "First Event Description", 120,
                "The Park", "One Time Event", "12/06/2021", "14/06/2021", "16", "23",
                50.0, "organizerId", "Organizer Name", null, null, 0);

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.createRepeatableEvent(repeatableEventModel, booleanConsumer -> {
            methodCalled.set(true);

            assertFalse(booleanConsumer);
        });

        assertTrue(methodCalled.get());
    }

    @Test
    public void createRepeatableEvent_withCollaborators_noPhoto_withRepetitions() {
        int repetitions = 5;

        CollaboratorHeader firstCollaboratorHeader = new CollaboratorHeader("firstCollaborator", "First Collaborator");
        CollaboratorHeader secondCollaboratorHeader = new CollaboratorHeader("secondCollaborator", "Second Collaborator");

        List<CollaboratorHeader> collaboratorHeaderList = new ArrayList<>();
        collaboratorHeaderList.add(firstCollaboratorHeader);
        collaboratorHeaderList.add(secondCollaboratorHeader);


        RepeatableEventModel repeatableEventModel = new RepeatableEventModel("First Event Name", "First Event Description", 120,
                "The Park", "One Time Event", "12/06/2021", "14/06/2021", "16", "23",
                50.0, "organizerId", "Organizer Name", collaboratorHeaderList, null, repetitions);

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.createRepeatableEvent(repeatableEventModel, booleanConsumer -> {
            methodCalled.set(true);

            assertTrue(booleanConsumer);
        });

        verify(eventModelDTOFirebaseRepository, times(repetitions)).createDocument(eq("events"), any(EventModelDTO.class), stringArgumentCaptorConsumer.capture());

        for (int i = 0; i < repetitions; i++) {
            String pathToCollection = "events/qwerty" + i;
            String pathToPhotos = "events/qwerty" + i;

            Consumer<String> createConsumer = stringArgumentCaptorConsumer.getValue();

            createConsumer.accept("qwerty" + i);

            verify(photoRepository, times(1)).updatePhoto(eq(pathToPhotos), nullable(Bitmap.class), booleanArgumentCaptorConsumer.capture());

            Consumer<Boolean> updatePhotoConsumer = booleanArgumentCaptorConsumer.getValue();

            updatePhotoConsumer.accept(true);

            int finalI = i;
            verify(eventModelDTOFirebaseRepository, times(1)).updateDocument(eq(pathToCollection), argThat(eventModelDTO -> {
                return eventModelDTO.eventId.equals("qwerty" + finalI);
            }), booleanArgumentCaptorConsumer2.capture());

            Consumer<Boolean> updateDocumentConsumer = booleanArgumentCaptorConsumer2.getValue();

            updateDocumentConsumer.accept(true);

        }

        assertTrue(methodCalled.get());
    }

    @Test
    public void createRepeatableEvent_noCollaborators_noPhoto_with3Repetitions_nullStartDate() {
        int repetitions = 3;

//        List<String> startDates = new ArrayList<>();
//        List<String> endDates = new ArrayList<>();
//
//        startDates.add("15/06/2021");
//        startDates.add("22/06/2021");
//        startDates.add("29/06/2021");
//
//        endDates.add("18/06/2021");
//        endDates.add("25/06/2021");
//        endDates.add("02/07/2021");

        RepeatableEventModel repeatableEventModel = new RepeatableEventModel("First Event Name", "First Event Description", 120,
                "The Park", "One Time Event", null, "18/06/2021", "16", "23",
                50.0, "organizerId", "Organizer Name", null, null, repetitions);

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.createRepeatableEvent(repeatableEventModel, booleanConsumer -> {
            methodCalled.set(true);

            assertTrue(booleanConsumer);
        });

        verify(eventModelDTOFirebaseRepository, times(repetitions)).createDocument(eq("events"), any(EventModelDTO.class), stringArgumentCaptorConsumer.capture());

        for (int i = 0; i < repetitions; i++) {
            int finalI = i;

            String pathToCollection = "events/qwerty" + finalI;
            String pathToPhotos = "events/qwerty" + finalI;

            Consumer<String> createConsumer = stringArgumentCaptorConsumer.getValue();

            createConsumer.accept("qwerty" + finalI);

            verify(photoRepository, times(1)).updatePhoto(eq(pathToPhotos), nullable(Bitmap.class), booleanArgumentCaptorConsumer.capture());

            Consumer<Boolean> updatePhotoConsumer = booleanArgumentCaptorConsumer.getValue();

            updatePhotoConsumer.accept(true);

            verify(eventModelDTOFirebaseRepository, times(1)).updateDocument(eq(pathToCollection), argThat(eventModelDTO -> {
                return eventModelDTO.eventId.equals("qwerty" + finalI) &&  eventModelDTO.eventStartDate.equals("");
            }), booleanArgumentCaptorConsumer2.capture());

//            eventModelDTO.eventStartDate.equals(startDates.get(finalI)) && eventModelDTO.eventEndDate.equals(endDates.get(finalI));

            Consumer<Boolean> updateDocumentConsumer = booleanArgumentCaptorConsumer2.getValue();

            updateDocumentConsumer.accept(true);

        }

        assertTrue(methodCalled.get());
    }

    @Test
    public void createRepeatableEvent_noCollaborators_noPhoto_with3Repetitions_wrongDate() {
        int repetitions = 3;

        RepeatableEventModel repeatableEventModel = new RepeatableEventModel("First Event Name", "First Event Description", 120,
                "The Park", "One Time Event", "startdate", "enddate", "16", "23",
                50.0, "organizerId", "Organizer Name", null, null, repetitions);

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.createRepeatableEvent(repeatableEventModel, booleanConsumer -> {
            methodCalled.set(true);

            assertTrue(booleanConsumer);
        });

        verify(eventModelDTOFirebaseRepository, times(repetitions)).createDocument(eq("events"), any(EventModelDTO.class), stringArgumentCaptorConsumer.capture());

        for (int i = 0; i < repetitions; i++) {
            int finalI = i;

            String pathToCollection = "events/qwerty" + finalI;
            String pathToPhotos = "events/qwerty" + finalI;

            Consumer<String> createConsumer = stringArgumentCaptorConsumer.getValue();

            createConsumer.accept("qwerty" + finalI);

            verify(photoRepository, times(1)).updatePhoto(eq(pathToPhotos), nullable(Bitmap.class), booleanArgumentCaptorConsumer.capture());

            Consumer<Boolean> updatePhotoConsumer = booleanArgumentCaptorConsumer.getValue();

            updatePhotoConsumer.accept(true);

            verify(eventModelDTOFirebaseRepository, times(1)).updateDocument(eq(pathToCollection), argThat(eventModelDTO -> {
                return eventModelDTO.eventId.equals("qwerty" + finalI) &&  eventModelDTO.eventStartDate.equals("") && eventModelDTO.eventEndDate.equals("");
            }), booleanArgumentCaptorConsumer2.capture());

            Consumer<Boolean> updateDocumentConsumer = booleanArgumentCaptorConsumer2.getValue();

            updateDocumentConsumer.accept(true);

        }

        assertTrue(methodCalled.get());
    }

    @Test
    public void createRepeatableEvent_withCollaborators_withPhoto_withRepetitions() {
        int repetitions = 3;

        CollaboratorHeader firstCollaboratorHeader = new CollaboratorHeader("firstCollaborator", "First Collaborator");
        CollaboratorHeader secondCollaboratorHeader = new CollaboratorHeader("secondCollaborator", "Second Collaborator");

        List<CollaboratorHeader> collaboratorHeaderList = new ArrayList<>();
        collaboratorHeaderList.add(firstCollaboratorHeader);
        collaboratorHeaderList.add(secondCollaboratorHeader);

        Bitmap eventPhoto = Mockito.mock(Bitmap.class);

        RepeatableEventModel repeatableEventModel = new RepeatableEventModel("First Event Name", "First Event Description", 120,
                "The Park", "One Time Event", "12/06/2021", "14/06/2021", "16", "23",
                50.0, "organizerId", "Organizer Name", collaboratorHeaderList, eventPhoto, repetitions);

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.createRepeatableEvent(repeatableEventModel, booleanConsumer -> {
            methodCalled.set(true);

            assertTrue(booleanConsumer);
        });

        verify(eventModelDTOFirebaseRepository, times(repetitions)).createDocument(eq("events"), any(EventModelDTO.class), stringArgumentCaptorConsumer.capture());

        for (int i = 0; i < repetitions; i++) {
            String pathToCollection = "events/qwerty" + i;
            String pathToPhotos = "events/qwerty" + i;

            Consumer<String> createConsumer = stringArgumentCaptorConsumer.getValue();

            createConsumer.accept("qwerty" + i);

            verify(photoRepository, times(1)).updatePhoto(eq(pathToPhotos), eq(eventPhoto), booleanArgumentCaptorConsumer.capture());

            Consumer<Boolean> updatePhotoConsumer = booleanArgumentCaptorConsumer.getValue();

            updatePhotoConsumer.accept(true);

            int finalI = i;
            verify(eventModelDTOFirebaseRepository, times(1)).updateDocument(eq(pathToCollection), argThat(eventModelDTO -> {
                return eventModelDTO.eventId.equals("qwerty" + finalI);
            }), booleanArgumentCaptorConsumer2.capture());

            Consumer<Boolean> updateDocumentConsumer = booleanArgumentCaptorConsumer2.getValue();

            updateDocumentConsumer.accept(true);

        }

        assertTrue(methodCalled.get());
    }
}