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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseEventServiceTest {

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
    public void getAllEventDetails_eventExists_noCollaborators_noPhoto() {
        String eventId = "qwerty";
        String pathToDocument = "events/" + eventId;
        String pathToPhoto = "events/" + eventId;

        EventModelDTO eventModelDTO = new EventModelDTO("First Event Name", "First Event Description", 120, "The Park", "One Time Event", "12/06/2021", "14/06/2021", "16", "23", 50.0, "organizerId", "Organizer Name", new ArrayList<>());
        eventModelDTO.eventId = eventId;

        doAnswer(ans -> {
            Consumer<EventModelDTO> consumer = ans.getArgument(2);
            consumer.accept(eventModelDTO);
            return null;
        }).when(eventModelDTOFirebaseRepository).getDocument(eq(pathToDocument), eq(EventModelDTO.class), any(Consumer.class));

        doAnswer(ans -> {
            Consumer<Bitmap> consumer = ans.getArgument(1);
            consumer.accept(null);
            return null;
        }).when(photoRepository).getPhoto(eq(pathToPhoto), any(Consumer.class));

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.getEventAllDetails(eventId, eventModel -> {
            methodCalled.set(true);

            assertEquals(eventId, eventModel.eventId);
            assertEquals("First Event Name", eventModel.eventName);
            assertEquals("First Event Description", eventModel.eventDescription);
            assertEquals(120, (long) eventModel.eventSeatNumber);
            assertEquals("The Park", eventModel.eventLocation);
            assertEquals("One Time Event", eventModel.eventType);
            assertEquals("12/06/2021", eventModel.eventStartDate);
            assertEquals("14/06/2021", eventModel.eventEndDate);
            assertEquals("16", eventModel.eventStartHour);
            assertEquals("23", eventModel.eventEndHour);
            assertEquals(50.0, (double) eventModel.ticketPrice, 0.1);
            assertEquals("organizerId", eventModel.organizerId);
            assertEquals("Organizer Name", eventModel.organizerName);
            assertEquals(0, eventModel.collaborators.size());
            assertNull(eventModel.eventPhoto);
        });

        assertTrue(methodCalled.get());
    }

    @Test
    public void getAllEventDetails_eventExists_noCollaborators_withPhoto() {
        String eventId = "asdfg";
        String pathToDocument = "events/" + eventId;
        String pathToPhoto = "events/" + eventId;

        EventModelDTO eventModelDTO = new EventModelDTO("Second Event Name", "Second Event Description", 8000, "Budapest", "Repeatable Event", "23/10/2022", "28/10/2022", "10", "22", 200.0, "uiop", "Sziget", new ArrayList<>());
        eventModelDTO.eventId = eventId;

        Bitmap eventPhoto = Mockito.mock(Bitmap.class);

        doAnswer(ans -> {
            Consumer<EventModelDTO> consumer = ans.getArgument(2);
            consumer.accept(eventModelDTO);
            return null;
        }).when(eventModelDTOFirebaseRepository).getDocument(eq(pathToDocument), eq(EventModelDTO.class), any(Consumer.class));

        doAnswer(ans -> {
            Consumer<Bitmap> consumer = ans.getArgument(1);
            consumer.accept(eventPhoto);
            return null;
        }).when(photoRepository).getPhoto(eq(pathToPhoto), any(Consumer.class));

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.getEventAllDetails(eventId, eventModel -> {
            methodCalled.set(true);

            assertEquals(eventId, eventModel.eventId);
            assertEquals("Second Event Name", eventModel.eventName);
            assertEquals("Second Event Description", eventModel.eventDescription);
            assertEquals(8000, (long) eventModel.eventSeatNumber);
            assertEquals("Budapest", eventModel.eventLocation);
            assertEquals("Repeatable Event", eventModel.eventType);
            assertEquals("23/10/2022", eventModel.eventStartDate);
            assertEquals("28/10/2022", eventModel.eventEndDate);
            assertEquals("10", eventModel.eventStartHour);
            assertEquals("22", eventModel.eventEndHour);
            assertEquals(200.0, (double) eventModel.ticketPrice, 0.1);
            assertEquals("uiop", eventModel.organizerId);
            assertEquals("Sziget", eventModel.organizerName);
            assertEquals(0, eventModel.collaborators.size());
            assertEquals(eventPhoto, eventModel.eventPhoto);
        });

        assertTrue(methodCalled.get());
    }

    @Test
    public void getAllEventDetails_eventExists_withCollaborators_withPhoto() {
        String eventId = "asdfg";
        String pathToDocument = "events/" + eventId;
        String pathToPhoto = "events/" + eventId;

        CollaboratorHeader firstCollaboratorHeader = new CollaboratorHeader("firstCollaborator", "First Collaborator");
        CollaboratorHeader secondCollaboratorHeader = new CollaboratorHeader("secondCollaborator", "Second Collaborator");

        List<CollaboratorHeader> collaboratorHeaderList = new ArrayList<>();
        collaboratorHeaderList.add(firstCollaboratorHeader);
        collaboratorHeaderList.add(secondCollaboratorHeader);

        EventModelDTO eventModelDTO = new EventModelDTO("Third Event Name", "Third Event Description", 200, "Scart", "One Time Event", "19/06/2021", "19/06/2021", "14", "19", 30.0, "organizer", "John Doe", collaboratorHeaderList);
        eventModelDTO.eventId = eventId;

        Bitmap eventPhoto = Mockito.mock(Bitmap.class);

        doAnswer(ans -> {
            Consumer<EventModelDTO> consumer = ans.getArgument(2);
            consumer.accept(eventModelDTO);
            return null;
        }).when(eventModelDTOFirebaseRepository).getDocument(eq(pathToDocument), eq(EventModelDTO.class), any(Consumer.class));

        doAnswer(ans -> {
            Consumer<Bitmap> consumer = ans.getArgument(1);
            consumer.accept(eventPhoto);
            return null;
        }).when(photoRepository).getPhoto(eq(pathToPhoto), any(Consumer.class));

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.getEventAllDetails(eventId, eventModel -> {
            methodCalled.set(true);

            assertEquals(eventId, eventModel.eventId);
            assertEquals("Third Event Name", eventModel.eventName);
            assertEquals("Third Event Description", eventModel.eventDescription);
            assertEquals(200, (long) eventModel.eventSeatNumber);
            assertEquals("Scart", eventModel.eventLocation);
            assertEquals("One Time Event", eventModel.eventType);
            assertEquals("19/06/2021", eventModel.eventStartDate);
            assertEquals("19/06/2021", eventModel.eventEndDate);
            assertEquals("14", eventModel.eventStartHour);
            assertEquals("19", eventModel.eventEndHour);
            assertEquals(30.0, (double) eventModel.ticketPrice, 0.1);
            assertEquals("organizer", eventModel.organizerId);
            assertEquals("John Doe", eventModel.organizerName);
            assertEquals(2, eventModel.collaborators.size());
            assertEquals(eventPhoto, eventModel.eventPhoto);

            CollaboratorHeader collaboratorHeader1 = eventModel.collaborators.get(0);

            assertEquals("firstCollaborator", collaboratorHeader1.getCollaboratorId());
            assertEquals("First Collaborator", collaboratorHeader1.getCollaboratorName());

            CollaboratorHeader collaboratorHeader2 = eventModel.collaborators.get(1);

            assertEquals("secondCollaborator", collaboratorHeader2.getCollaboratorId());
            assertEquals("Second Collaborator", collaboratorHeader2.getCollaboratorName());
        });

        assertTrue(methodCalled.get());
    }

    @Test
    public void getAllEventDetails_eventDoesNotExist() {
        String eventId = "inexistent";
        String pathToDocument = "events/" + eventId;
        String pathToPhoto = "events/" + eventId;

        doAnswer(ans -> {
            Consumer<EventModelDTO> consumer = ans.getArgument(2);
            consumer.accept(null);
            return null;
        }).when(eventModelDTOFirebaseRepository).getDocument(eq(pathToDocument), eq(EventModelDTO.class), any(Consumer.class));

        doAnswer(ans -> {
            Consumer<Bitmap> consumer = ans.getArgument(1);
            consumer.accept(null);
            return null;
        }).when(photoRepository).getPhoto(eq(pathToPhoto), any(Consumer.class));

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.getEventAllDetails(eventId, eventModel -> {
            methodCalled.set(true);

            assertEquals("", eventModel.eventId);
            assertEquals("", eventModel.eventName);
            assertEquals("", eventModel.eventDescription);
            assertEquals(0, (long) eventModel.eventSeatNumber);
            assertEquals("", eventModel.eventLocation);
            assertEquals("", eventModel.eventType);
            assertEquals("", eventModel.eventStartDate);
            assertEquals("", eventModel.eventEndDate);
            assertEquals("", eventModel.eventStartHour);
            assertEquals("", eventModel.eventEndHour);
            assertEquals(0.0, (double) eventModel.ticketPrice, 0.1);
            assertEquals("", eventModel.organizerId);
            assertEquals("", eventModel.organizerName);
            assertNull(eventModel.collaborators);
            assertNull(eventModel.eventPhoto);
        });

        assertTrue(methodCalled.get());
    }

    @Test
    public void updateEvent_eventExists() {
        String eventId = "zxcvb";
        String pathToDocument = "events/" + eventId;

        CollaboratorHeader firstCollaboratorHeader = new CollaboratorHeader("firstCollaborator", "First Collaborator");
        CollaboratorHeader secondCollaboratorHeader = new CollaboratorHeader("secondCollaborator", "Second Collaborator");

        List<CollaboratorHeader> collaboratorHeaderList = new ArrayList<>();
        collaboratorHeaderList.add(firstCollaboratorHeader);
        collaboratorHeaderList.add(secondCollaboratorHeader);

        Bitmap eventPhoto = Mockito.mock(Bitmap.class);

        EventModel eventModel = new EventModel("Third Event Name", "Third Event Description", 200, "Scart", "One Time Event", "19/06/2021", "19/06/2021", "14", "19", 30.0, "organizer", "John Doe", collaboratorHeaderList, eventPhoto);
        eventModel.eventId = eventId;

        int seatNumber = 200;

        UpdatableEventModelDTO updatableEventModelDTO = new UpdatableEventModelDTO();
        updatableEventModelDTO.eventSeatNumber = seatNumber;

        doAnswer(ans -> {
            Consumer<Boolean> consumer = ans.getArgument(2);
            consumer.accept(true);
            return null;
        }).when(updatableEventModelDTOFirebaseRepository).updateDocument(eq(pathToDocument), argThat(updatableEvent -> {
            return updatableEvent.eventSeatNumber == updatableEventModelDTO.eventSeatNumber;
        }), any(Consumer.class));

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.updateEvent(eventId, eventModel, booleanConsumer -> {
            methodCalled.set(true);
            assertTrue(booleanConsumer);
        });

        assertTrue(methodCalled.get());
    }

    @Test
    public void updateEvent_eventDoesNotExist() {
        String eventId = "inexistent";

        int seatNumber = 200;

        UpdatableEventModelDTO updatableEventModelDTO = new UpdatableEventModelDTO();
        updatableEventModelDTO.eventSeatNumber = seatNumber;

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        firebaseEventService.updateEvent(eventId, null, booleanConsumer -> {
            methodCalled.set(true);
            assertFalse(booleanConsumer);
        });

        assertTrue(methodCalled.get());
    }

    @Test
    public void getAllFutureEventsCards_hasOnlyFutureEvents_noCollaborators_withPhotos() {

        String pathToCollection = "events";

        int eventsCount = 3;

        Bitmap eventPhoto1 = Mockito.mock(Bitmap.class);
        Bitmap eventPhoto2 = Mockito.mock(Bitmap.class);
        Bitmap eventPhoto3 = Mockito.mock(Bitmap.class);

        List<Bitmap> photos = new ArrayList<>();
        photos.add(eventPhoto1);
        photos.add(eventPhoto2);
        photos.add(eventPhoto3);

        List<EventModelDTO> events = new ArrayList<>();

        for (int i = 0; i < eventsCount; i++) {

            EventModelDTO eventModelDTO = new EventModelDTO("Third Event Name" + i, "Third Event Description" + i, i * 10,
                    "Scart" + i, "One Time Event", "19/06/203" + i, "20/06/203" + i, "14", "19",
                    (i + 1) * 20.0, "organizer" + i, "John Doe" + i, new ArrayList<>());
            eventModelDTO.eventId = "event" + i;
            events.add(eventModelDTO);
        }

        AtomicInteger count = new AtomicInteger();

        firebaseEventService.getAllFutureEventsCards(eventCard -> {

            assertEquals("event" + count, eventCard.eventId);
            assertEquals("Third Event Name" + count, eventCard.eventName);
            assertEquals("John Doe" + count, eventCard.organizerName);
            assertEquals("19/06/203" + count, eventCard.eventDate);
            assertEquals("Scart" + count, eventCard.eventLocation);
            assertEquals((count.get() + 1) * 20.0, (double) eventCard.ticketPrice, 0.1);
            assertEquals(photos.get(count.get()), eventCard.eventImage);

            count.getAndIncrement();
        });

        verify(eventModelDTOFirebaseRepository, times(1)).getAllDocuments(eq(pathToCollection), eq(EventModelDTO.class), eventModelDTOConsumerArgumentCaptor.capture());

        Consumer<EventModelDTO> eventModelDTOConsumer = eventModelDTOConsumerArgumentCaptor.getValue();

        for (int i = 0; i < eventsCount; i++) {
            eventModelDTOConsumer.accept(events.get(i));
        }

        for (int i = 0; i < eventsCount; i++) {
            verify(photoRepository).getPhoto(eq("events/event" + i), bitmapArgumentCaptorConsumer.capture());
            Consumer<Bitmap> bitmapConsumer = bitmapArgumentCaptorConsumer.getValue();
            bitmapConsumer.accept(photos.get(i));
        }

        assertEquals(3, count.get());
    }

    @Test
    public void getAllFutureEventsCards_bothPastAndFutureEvents_withCollaborators_noPhotos() {

        String pathToCollection = "events";

        int eventsCount = 3;

        CollaboratorHeader firstCollaboratorHeader = new CollaboratorHeader("firstCollaborator", "First Collaborator");
        CollaboratorHeader secondCollaboratorHeader = new CollaboratorHeader("secondCollaborator", "Second Collaborator");

        List<CollaboratorHeader> collaboratorHeaderList = new ArrayList<>();
        collaboratorHeaderList.add(firstCollaboratorHeader);
        collaboratorHeaderList.add(secondCollaboratorHeader);

        List<EventModelDTO> events = new ArrayList<>();

        EventModelDTO eventModelDTO;

        for (int i = 0; i < eventsCount; i++) {

            if (i == 0) {
                eventModelDTO = new EventModelDTO("Third Event Name" + i, "Third Event Description" + i, i * 10,
                        "Scart" + i, "One Time Event", "19/06/201" + i, "20/06/2010", "14", "19",
                        (i + 1) * 20.0, "organizer" + i, "John Doe" + i, collaboratorHeaderList);
            } else {
                eventModelDTO = new EventModelDTO("Third Event Name" + i, "Third Event Description" + i, i * 10,
                        "Scart" + i, "One Time Event", "19/06/203" + i, "20/06/203" + i, "14", "19",
                        (i + 1) * 20.0, "organizer" + i, "John Doe" + i, collaboratorHeaderList);
            }
            eventModelDTO.eventId = "event" + i;
            events.add(eventModelDTO);
        }

        AtomicInteger count = new AtomicInteger();

        firebaseEventService.getAllFutureEventsCards(eventCard -> {
            count.getAndIncrement();

            assertEquals("event" + count, eventCard.eventId);
            assertEquals("Third Event Name" + count, eventCard.eventName);
            assertEquals("John Doe" + count, eventCard.organizerName);
            assertEquals("19/06/203" + count, eventCard.eventDate);
            assertEquals("Scart" + count, eventCard.eventLocation);
            assertEquals((count.get() + 1) * 20.0, (double) eventCard.ticketPrice, 0.1);
            assertNull(eventCard.eventImage);
        });

        verify(eventModelDTOFirebaseRepository, times(1)).getAllDocuments(eq(pathToCollection), eq(EventModelDTO.class), eventModelDTOConsumerArgumentCaptor.capture());

        Consumer<EventModelDTO> eventModelDTOConsumer = eventModelDTOConsumerArgumentCaptor.getValue();

        for (int i = 0; i < eventsCount; i++) {
            eventModelDTOConsumer.accept(events.get(i));
        }

        for (int i = 1; i < eventsCount; i++) {
            verify(photoRepository).getPhoto(eq("events/event" + i), bitmapArgumentCaptorConsumer.capture());
            Consumer<Bitmap> bitmapConsumer = bitmapArgumentCaptorConsumer.getValue();
            bitmapConsumer.accept(null);
        }

        assertEquals(2, count.get());
    }

    @Test
    public void getAllFutureEventsCards_allPastEvents_withCollaborators_withPhotos() {

        String pathToCollection = "events";

        int eventsCount = 3;

        CollaboratorHeader firstCollaboratorHeader = new CollaboratorHeader("firstCollaborator", "First Collaborator");
        CollaboratorHeader secondCollaboratorHeader = new CollaboratorHeader("secondCollaborator", "Second Collaborator");

        List<CollaboratorHeader> collaboratorHeaderList = new ArrayList<>();
        collaboratorHeaderList.add(firstCollaboratorHeader);
        collaboratorHeaderList.add(secondCollaboratorHeader);

        List<EventModelDTO> events = new ArrayList<>();

        EventModelDTO eventModelDTO;

        for (int i = 0; i < eventsCount; i++) {

            eventModelDTO = new EventModelDTO("Third Event Name" + i, "Third Event Description" + i, i * 10,
                    "Scart" + i, "One Time Event", "19/06/201" + i, "20/06/201" + i, "14", "19",
                    (i + 1) * 20.0, "organizer" + i, "John Doe" + i, collaboratorHeaderList);

            eventModelDTO.eventId = "event" + i;
            events.add(eventModelDTO);
        }

        firebaseEventService.getAllFutureEventsCards(eventCard -> {
            fail();
        });

        verify(eventModelDTOFirebaseRepository, times(1)).getAllDocuments(eq(pathToCollection), eq(EventModelDTO.class), eventModelDTOConsumerArgumentCaptor.capture());

        Consumer<EventModelDTO> eventModelDTOConsumer = eventModelDTOConsumerArgumentCaptor.getValue();

        for (int i = 0; i < eventsCount; i++) {
            eventModelDTOConsumer.accept(events.get(i));
        }

        verify(photoRepository, times(0)).getPhoto(any(String.class), any(Consumer.class));
    }
}