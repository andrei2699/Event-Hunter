package com.example.eventhunter.profile.service;

import android.graphics.Bitmap;

import com.example.eventhunter.events.models.EventModel;
import com.example.eventhunter.events.service.FirebaseEventService;
import com.example.eventhunter.profile.regularUser.RegularUserModel;
import com.example.eventhunter.profile.service.dto.CollaboratorModelDTO;
import com.example.eventhunter.profile.service.dto.OrganizerModelDTO;
import com.example.eventhunter.profile.service.dto.RegularUserModelDTO;
import com.example.eventhunter.profile.service.dto.UpdatableCollaboratorModelDTO;
import com.example.eventhunter.profile.service.dto.UpdatableOrganizerModelDTO;
import com.example.eventhunter.profile.service.dto.UpdatableRegularUserModelDTO;
import com.example.eventhunter.repository.PhotoRepository;
import com.example.eventhunter.repository.impl.FirebaseRepositoryImpl;
import com.example.eventhunter.reservation.ReservationModel;
import com.example.eventhunter.reservation.dto.ReservationModelDTO;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RegularUserProfileServiceTest {

    @Mock
    private FirebaseRepositoryImpl<CollaboratorModelDTO> collaboratorRepository;
    @Mock
    private FirebaseRepositoryImpl<OrganizerModelDTO> organizerRepository;
    @Mock
    private FirebaseRepositoryImpl<RegularUserModelDTO> regularUserRepository;
    @Mock
    private FirebaseRepositoryImpl<UpdatableOrganizerModelDTO> updatableOrganizerModelDTOFirebaseRepository;
    @Mock
    private FirebaseRepositoryImpl<UpdatableCollaboratorModelDTO> updatableCollaboratorModelDTOFirebaseRepository;
    @Mock
    private FirebaseRepositoryImpl<UpdatableRegularUserModelDTO> updatableRegularUserModelDTOFirebaseRepository;
    @Mock
    private PhotoRepository photoRepository;
    @Mock
    private FirebaseEventService eventService;

    @Captor
    private ArgumentCaptor<Consumer<Boolean>> booleanConsumerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Consumer<RegularUserModelDTO>> regularUserModelConsumerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Consumer<EventModel>> eventModelConsumerArgumentCaptor;

    private RegularUserProfileService regularUserProfileService;


    @Before
    public void setUp() {
        regularUserProfileService = new FirebaseProfileService(photoRepository, eventService, collaboratorRepository,
                organizerRepository, regularUserRepository, updatableOrganizerModelDTOFirebaseRepository,
                updatableCollaboratorModelDTOFirebaseRepository, updatableRegularUserModelDTOFirebaseRepository);
    }

    @After
    public void tearDown() {
        regularUserProfileService = null;
    }

    @Test
    public void getRegularUserProfileById_profileExists_noReservations() {

        String userId = "1234";
        String pathToDocument = "users/" + userId;

        RegularUserModelDTO regularUserModelDTO = new RegularUserModelDTO();
        regularUserModelDTO.id = "1234";
        regularUserModelDTO.email = "test@gmail.com";
        regularUserModelDTO.name = "My Test Name";
        regularUserModelDTO.userType = "Regular User";
        regularUserModelDTO.reservationsNumber = 0;
        regularUserModelDTO.reservations = new ArrayList<>();

        doAnswer(ans -> {
            Consumer<RegularUserModelDTO> consumer = ans.getArgument(2);
            consumer.accept(regularUserModelDTO);
            return null;
        }).when(regularUserRepository).getDocument(eq(pathToDocument), eq(RegularUserModelDTO.class), any(Consumer.class));


        AtomicBoolean methodCalled = new AtomicBoolean(false);

        regularUserProfileService.getRegularUserProfileById(userId, regularUserModel -> {
            methodCalled.set(true);

            assertEquals(userId, regularUserModel.id);
            assertEquals("test@gmail.com", regularUserModel.email);
            assertEquals("My Test Name", regularUserModel.name);
            assertEquals("Regular User", regularUserModel.userType);
            assertEquals(0, regularUserModel.reservationsNumber);
            assertEquals(0, regularUserModel.reservations.size());
        });

        assertTrue(methodCalled.get());
    }

    @Test
    public void getRegularUserProfileById_profileExists_butIsStillAGenericProfile() {
        String userId = "abc";
        String pathToDocument = "users/" + userId;

        RegularUserModelDTO regularUserModelDTO = new RegularUserModelDTO();
        regularUserModelDTO.id = "abc";
        regularUserModelDTO.email = "test@yahoo.com";
        regularUserModelDTO.name = "Name";
        regularUserModelDTO.userType = "Regular User";

        doAnswer(ans -> {
            Consumer<RegularUserModelDTO> consumer = ans.getArgument(2);
            consumer.accept(regularUserModelDTO);
            return null;
        }).when(regularUserRepository).getDocument(eq(pathToDocument), eq(RegularUserModelDTO.class), any(Consumer.class));


        AtomicBoolean methodCalled = new AtomicBoolean(false);

        regularUserProfileService.getRegularUserProfileById(userId, regularUserModel -> {
            methodCalled.set(true);

            assertEquals(0, regularUserModel.reservationsNumber);
            assertEquals(0, regularUserModel.reservations.size());

            assertEquals(userId, regularUserModel.id);
            assertEquals("test@yahoo.com", regularUserModel.email);
            assertEquals("Name", regularUserModel.name);
            assertEquals("Regular User", regularUserModel.userType);

        });

        assertTrue(methodCalled.get());
    }

    @Test
    public void getRegularUserProfileById_profileExists_checkReservationNumber() {
        String userId = "abc";
        String pathToDocument = "users/" + userId;

        doAnswer(ans -> {
            RegularUserModelDTO regularUserModelDTO = new RegularUserModelDTO();
            regularUserModelDTO.id = "abc";
            regularUserModelDTO.email = "test@yahoo.com";
            regularUserModelDTO.name = "Name";
            regularUserModelDTO.userType = "Regular User";
            regularUserModelDTO.reservationsNumber = 2;
            regularUserModelDTO.reservations = new ArrayList<>();

            Consumer<RegularUserModelDTO> consumer = ans.getArgument(2);
            consumer.accept(regularUserModelDTO);
            return null;
        }).when(regularUserRepository).getDocument(eq(pathToDocument), eq(RegularUserModelDTO.class), any(Consumer.class));


        AtomicBoolean methodCalled = new AtomicBoolean(false);

        Consumer<RegularUserModel> regularUserModelConsumer = regularUserModel -> {
            methodCalled.set(true);

            assertEquals(userId, regularUserModel.id);
            assertEquals("test@yahoo.com", regularUserModel.email);
            assertEquals("Name", regularUserModel.name);
            assertEquals("Regular User", regularUserModel.userType);

            assertEquals(2, regularUserModel.reservationsNumber);
        };

        regularUserProfileService.getRegularUserProfileById(userId, regularUserModelConsumer);

        assertTrue(methodCalled.get());
    }

    @Test
    public void getRegularUserProfileById_profileDoesNotExist() {
        String userId = "inexistent";
        String pathToDocument = "users/" + userId;

        doAnswer(ans -> {
            Consumer<RegularUserModelDTO> consumer = ans.getArgument(2);
            consumer.accept(null);
            return null;
        }).when(regularUserRepository).getDocument(eq(pathToDocument), eq(RegularUserModelDTO.class), any(Consumer.class));

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        regularUserProfileService.getRegularUserProfileById(userId, regularUserModel -> {
            methodCalled.set(true);

            assertEquals("", regularUserModel.id);
            assertEquals("", regularUserModel.email);
            assertEquals("", regularUserModel.name);
            assertEquals("Regular User", regularUserModel.userType);
            assertEquals(0, regularUserModel.reservationsNumber);
            assertEquals(0, regularUserModel.reservations.size());
        });

        assertTrue(methodCalled.get());
    }

    @Test
    public void getRegularUserProfileById_profileExists_hasOneReservationAndNoEventImage() {
        String userId = "124javvbc";
        String pathToDocument = "users/" + userId;

        String eventid = "eventid";

        RegularUserModelDTO regularUserModelDTO = new RegularUserModelDTO();
        regularUserModelDTO.id = "1234";
        regularUserModelDTO.email = "test@gmail.com";
        regularUserModelDTO.name = "My Test Name";
        regularUserModelDTO.userType = "Regular User";
        regularUserModelDTO.reservationsNumber = 1;
        regularUserModelDTO.reservations = new ArrayList<>();
        regularUserModelDTO.reservations.add(new ReservationModelDTO(eventid, userId, 0, "First Event", "Street", "07/05/2021", "12", 15.2, 25));

        EventModel eventModel = new EventModel(eventid, "", 20, "", "", "", "", "", "", 20.1, "", "", new ArrayList<>(), null);

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        regularUserProfileService.getRegularUserProfileById(userId, regularUserModel -> {
            methodCalled.set(true);

            assertEquals("1234", regularUserModel.id);
            assertEquals("test@gmail.com", regularUserModel.email);
            assertEquals("My Test Name", regularUserModel.name);
            assertEquals("Regular User", regularUserModel.userType);
            assertEquals(1, regularUserModel.reservationsNumber);
            assertEquals(1, regularUserModel.reservations.size());

            ReservationModel reservationModel = regularUserModel.reservations.get(0);

            assertEquals(eventid, reservationModel.eventId);
            assertEquals(userId, reservationModel.userId);
            assertEquals(0, reservationModel.reservationId);
            assertEquals("First Event", reservationModel.eventName);
            assertEquals("Street", reservationModel.eventLocation);
            assertEquals("07/05/2021", reservationModel.eventStartDate);
            assertEquals("12", reservationModel.eventStartHour);
            assertEquals(15.2, reservationModel.ticketPrice, 0.01);
            assertEquals(25, reservationModel.reservedSeatsNumber);
            assertNull(reservationModel.eventPhoto);
        });

        verify(regularUserRepository).getDocument(eq(pathToDocument), eq(RegularUserModelDTO.class), regularUserModelConsumerArgumentCaptor.capture());

        Consumer<RegularUserModelDTO> value = regularUserModelConsumerArgumentCaptor.getValue();
        value.accept(regularUserModelDTO);


        verify(eventService, times(1)).getEventAllDetails(eq(eventid), eventModelConsumerArgumentCaptor.capture());

        Consumer<EventModel> eventModelConsumer = eventModelConsumerArgumentCaptor.getValue();
        eventModelConsumer.accept(eventModel);

        assertTrue(methodCalled.get());
    }

    @Test
    public void getRegularUserProfileById_profileExists_hasOneReservationAndHasEventImage() {
        String userId = "66161";
        String pathToDocument = "users/" + userId;
        Bitmap mockedEventPhoto = Mockito.mock(Bitmap.class);

        String eventid = "evid12";

        RegularUserModelDTO regularUserModelDTO = new RegularUserModelDTO();
        regularUserModelDTO.id = "877";
        regularUserModelDTO.email = "test@gmail.com";
        regularUserModelDTO.name = "My Test Name";
        regularUserModelDTO.userType = "Regular User";
        regularUserModelDTO.reservationsNumber = 1;
        regularUserModelDTO.reservations = new ArrayList<>();
        regularUserModelDTO.reservations.add(new ReservationModelDTO(eventid, userId, 0, "First Event", "Street", "05/01/2021", "12", 11.6, 1));

        EventModel eventModel = new EventModel(eventid, "", 20, "", "", "", "", "", "", 20.1, "", "", new ArrayList<>(), mockedEventPhoto);

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        regularUserProfileService.getRegularUserProfileById(userId, regularUserModel -> {
            methodCalled.set(true);

            assertEquals("877", regularUserModel.id);
            assertEquals("test@gmail.com", regularUserModel.email);
            assertEquals("My Test Name", regularUserModel.name);
            assertEquals("Regular User", regularUserModel.userType);
            assertEquals(1, regularUserModel.reservationsNumber);
            assertEquals(1, regularUserModel.reservations.size());

            ReservationModel reservationModel = regularUserModel.reservations.get(0);

            assertEquals(eventid, reservationModel.eventId);
            assertEquals(userId, reservationModel.userId);
            assertEquals(0, reservationModel.reservationId);
            assertEquals("First Event", reservationModel.eventName);
            assertEquals("Street", reservationModel.eventLocation);
            assertEquals("05/01/2021", reservationModel.eventStartDate);
            assertEquals("12", reservationModel.eventStartHour);
            assertEquals(11.6, reservationModel.ticketPrice, 0.01);
            assertEquals(1, reservationModel.reservedSeatsNumber);
            assertNotNull(reservationModel.eventPhoto);

        });

        verify(regularUserRepository).getDocument(eq(pathToDocument), eq(RegularUserModelDTO.class), regularUserModelConsumerArgumentCaptor.capture());

        Consumer<RegularUserModelDTO> value = regularUserModelConsumerArgumentCaptor.getValue();
        value.accept(regularUserModelDTO);


        verify(eventService, times(1)).getEventAllDetails(eq(eventid), eventModelConsumerArgumentCaptor.capture());

        Consumer<EventModel> eventModelConsumer = eventModelConsumerArgumentCaptor.getValue();
        eventModelConsumer.accept(eventModel);

        assertTrue(methodCalled.get());
    }

    @Test
    public void getRegularUserProfileById_profileExists_hasMultipleReservationsWithEventImage() {
        String userId = "uid";
        String pathToDocument = "users/" + userId;
        String eventid = "5151";
        int reservationsNumber = 5;

        List<EventModel> eventModels = new ArrayList<>();

        List<ReservationModel> reservationModelList = new ArrayList<>();
        for (int i = 0; i < reservationsNumber; i++) {
            Bitmap mockedEventPhoto = Mockito.mock(Bitmap.class);

            EventModel eventModel = new EventModel("eventName" + i, "Description", 120, "", "", "", "", "", "", 20.1, "", "", new ArrayList<>(), mockedEventPhoto);
            eventModels.add(eventModel);
            eventModel.eventId = "eventId" + i;

            reservationModelList.add(new ReservationModel("eventId" + i, userId, i, "name" + i, "Street" + i, i + "/" + "/2021", "1" + i, (i + 1) * 5.1, (i + 1) * 2, mockedEventPhoto));
        }

        RegularUserModelDTO regularUserModelDTO = new RegularUserModelDTO();
        regularUserModelDTO.id = "877";
        regularUserModelDTO.email = "test@gmail.com";
        regularUserModelDTO.name = "My Test Name";
        regularUserModelDTO.userType = "Regular User";
        regularUserModelDTO.reservationsNumber = reservationsNumber;
        regularUserModelDTO.reservations = new ArrayList<>();

        for (int i = 0; i < reservationsNumber; i++) {
            ReservationModel reservationModel = reservationModelList.get(i);
            regularUserModelDTO.reservations.add(new ReservationModelDTO(reservationModel.eventId, reservationModel.userId, reservationModel.reservationId,
                    reservationModel.eventName, reservationModel.eventLocation, reservationModel.eventStartDate, reservationModel.eventStartHour,
                    reservationModel.ticketPrice, reservationModel.reservedSeatsNumber));
        }


        AtomicBoolean methodCalled = new AtomicBoolean(false);

        regularUserProfileService.getRegularUserProfileById(userId, regularUserModel -> {
            methodCalled.set(true);

            assertEquals("877", regularUserModel.id);
            assertEquals("test@gmail.com", regularUserModel.email);
            assertEquals("My Test Name", regularUserModel.name);
            assertEquals("Regular User", regularUserModel.userType);
            assertEquals(reservationsNumber, regularUserModel.reservationsNumber);
            assertEquals(reservationsNumber, regularUserModel.reservations.size());


            for (int i = 0; i < regularUserModel.reservations.size(); i++) {
                ReservationModel actualReservationModel = reservationModelList.get(i);
                ReservationModel reservationModel = regularUserModel.reservations.get(i);

                assertEquals(actualReservationModel.eventId, reservationModel.eventId);
                assertEquals(actualReservationModel.userId, reservationModel.userId);
                assertEquals(actualReservationModel.reservationId, reservationModel.reservationId);
                assertEquals(actualReservationModel.eventName, reservationModel.eventName);
                assertEquals(actualReservationModel.eventLocation, reservationModel.eventLocation);
                assertEquals(actualReservationModel.eventStartDate, reservationModel.eventStartDate);
                assertEquals(actualReservationModel.eventStartHour, reservationModel.eventStartHour);
                assertEquals(actualReservationModel.ticketPrice, reservationModel.ticketPrice, 0.01);
                assertEquals(actualReservationModel.reservedSeatsNumber, reservationModel.reservedSeatsNumber);
                assertEquals(actualReservationModel.eventPhoto, reservationModel.eventPhoto);
            }
        });

        verify(regularUserRepository).getDocument(eq(pathToDocument), eq(RegularUserModelDTO.class), regularUserModelConsumerArgumentCaptor.capture());


        Consumer<RegularUserModelDTO> value = regularUserModelConsumerArgumentCaptor.getValue();
        value.accept(regularUserModelDTO);


        for (int i = 0; i < eventModels.size(); i++) {
            EventModel eventModel = eventModels.get(i);
            verify(eventService, times(1)).getEventAllDetails(eq(eventModel.eventId), eventModelConsumerArgumentCaptor.capture());

            Consumer<EventModel> eventModelConsumer = eventModelConsumerArgumentCaptor.getValue();

            eventModelConsumer.accept(eventModel);
        }

        assertTrue(methodCalled.get());
    }

    @Test
    public void updateRegularUserProfileById_profileExistsAndHasNoReservations() {
        String userId = "rtyu";
        String pathToDocument = "users/" + userId;

        UpdatableRegularUserModelDTO updatableRegularUserModelDTO = new UpdatableRegularUserModelDTO();
        updatableRegularUserModelDTO.reservations = new ArrayList<>();
        updatableRegularUserModelDTO.reservationsNumber = 0;

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        RegularUserModel regularUserModel = new RegularUserModel(userId, "John", "Regular User", "john@gmail.com", 0, new ArrayList<>());

        regularUserProfileService.updateRegularUserProfileById(userId, regularUserModel, status -> {
            methodCalled.set(true);

            assertTrue(status);
        });

        verify(updatableRegularUserModelDTOFirebaseRepository).updateDocument(eq(pathToDocument), argThat(argument ->
                        updatableRegularUserModelDTO.reservationsNumber == argument.reservationsNumber && updatableRegularUserModelDTO.reservations.size() == argument.reservations.size()),
                booleanConsumerArgumentCaptor.capture());

        Consumer<Boolean> value = booleanConsumerArgumentCaptor.getValue();
        value.accept(true);

        assertTrue(methodCalled.get());
    }

    @Test
    public void updateRegularUserProfileById_profileDoesNotExist() {
        String userId = "rtyu";
        String pathToDocument = "users/" + userId;

        UpdatableRegularUserModelDTO updatableRegularUserModelDTO = new UpdatableRegularUserModelDTO();
        updatableRegularUserModelDTO.reservations = new ArrayList<>();
        updatableRegularUserModelDTO.reservationsNumber = 0;

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        RegularUserModel regularUserModel = new RegularUserModel(userId, "Mary", "Regular User", "mary_regularuser@gmail.com", 0, new ArrayList<>());

        regularUserProfileService.updateRegularUserProfileById(userId, regularUserModel, status -> {
            methodCalled.set(true);

            assertTrue(status);
        });

        verify(updatableRegularUserModelDTOFirebaseRepository).updateDocument(eq(pathToDocument), argThat(argument ->
                        updatableRegularUserModelDTO.reservationsNumber == argument.reservationsNumber && updatableRegularUserModelDTO.reservations.size() == argument.reservations.size()),
                booleanConsumerArgumentCaptor.capture());

        Consumer<Boolean> value = booleanConsumerArgumentCaptor.getValue();
        value.accept(true);

        assertTrue(methodCalled.get());
    }

    @Test
    public void updateRegularUserProfileById_profileRepositoryFails() {
        String userId = "z124b1gh";
        String pathToDocument = "users/" + userId;

        UpdatableRegularUserModelDTO updatableRegularUserModelDTO = new UpdatableRegularUserModelDTO();
        updatableRegularUserModelDTO.reservations = new ArrayList<>();
        updatableRegularUserModelDTO.reservationsNumber = 0;

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        RegularUserModel regularUserModel = new RegularUserModel(userId, "Zain", "Regular User", "Zainegularuser@gmail.com", 0, new ArrayList<>());

        regularUserProfileService.updateRegularUserProfileById(userId, regularUserModel, status -> {
            methodCalled.set(true);

            assertFalse(status);
        });

        verify(updatableRegularUserModelDTOFirebaseRepository).updateDocument(eq(pathToDocument), any(UpdatableRegularUserModelDTO.class), booleanConsumerArgumentCaptor.capture());

        Consumer<Boolean> value = booleanConsumerArgumentCaptor.getValue();
        value.accept(false);

        assertTrue(methodCalled.get());
    }

    @Test
    public void updateRegularUserProfileById_profileExistsAndHasOneReservation() {
        String userId = "xxzy12";
        String pathToDocument = "users/" + userId;

        UpdatableRegularUserModelDTO updatableRegularUserModelDTO = new UpdatableRegularUserModelDTO();
        updatableRegularUserModelDTO.reservationsNumber = 1;
        updatableRegularUserModelDTO.reservations = new ArrayList<>();
        updatableRegularUserModelDTO.reservations.add(new ReservationModelDTO("eventid", userId, 0, "First Event", "Street", "05/02/2021", "12", 15.2, 20));

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        ArrayList<ReservationModel> reservations = new ArrayList<>();
        reservations.add(new ReservationModel("eventid", userId, 0, "First Event", "Street", "05/02/2021", "12", 15.2, 20, null));

        RegularUserModel regularUserModel = new RegularUserModel(userId, "John", "Regular User", "john@gmail.com", 1, reservations);

        regularUserProfileService.updateRegularUserProfileById(userId, regularUserModel, status -> {
            methodCalled.set(true);

            assertTrue(status);
        });

        verify(updatableRegularUserModelDTOFirebaseRepository).updateDocument(eq(pathToDocument), argThat(argument ->
        {
            if (updatableRegularUserModelDTO.reservationsNumber != argument.reservationsNumber || updatableRegularUserModelDTO.reservations.size() != argument.reservations.size()) {
                return false;
            }
            ReservationModelDTO reservation = argument.reservations.get(0);
            return reservation.eventName.equals("First Event") && reservation.eventId.equals("eventid") && reservation.userId.equals(userId) && reservation.eventLocation.equals("Street") &&
                    reservation.reservationId == 0 && reservation.eventStartDate.equals("05/02/2021") && reservation.eventStartHour.equals("12") && reservation.reservedSeatsNumber == 20 &&
                    Math.abs(reservation.ticketPrice - 15.2) < 0.01;
        }), booleanConsumerArgumentCaptor.capture());

        Consumer<Boolean> value = booleanConsumerArgumentCaptor.getValue();
        value.accept(true);

        assertTrue(methodCalled.get());
    }

    @Test
    public void updateRegularUserProfileById_profileIsNull() {
        String userId = "z124b1gh";

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        regularUserProfileService.updateRegularUserProfileById(userId, null, status -> {
            methodCalled.set(true);

            assertFalse(status);
        });

        verify(updatableRegularUserModelDTOFirebaseRepository, times(0)).updateDocument(any(String.class), any(UpdatableRegularUserModelDTO.class), any(Consumer.class));

        assertTrue(methodCalled.get());
    }
}