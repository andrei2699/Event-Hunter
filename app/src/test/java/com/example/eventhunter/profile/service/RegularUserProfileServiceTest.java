package com.example.eventhunter.profile.service;

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
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
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
    private ArgumentCaptor<Consumer<Boolean>> consumerArgumentCaptor;

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
    public void getRegularUserProfileById_profileExists_andHasReservations() {
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
                consumerArgumentCaptor.capture());

        Consumer<Boolean> value = consumerArgumentCaptor.getValue();
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
                consumerArgumentCaptor.capture());

        Consumer<Boolean> value = consumerArgumentCaptor.getValue();
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

        verify(updatableRegularUserModelDTOFirebaseRepository).updateDocument(eq(pathToDocument), any(UpdatableRegularUserModelDTO.class), consumerArgumentCaptor.capture());

        Consumer<Boolean> value = consumerArgumentCaptor.getValue();
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
        }), consumerArgumentCaptor.capture());

        Consumer<Boolean> value = consumerArgumentCaptor.getValue();
        value.accept(true);

        assertTrue(methodCalled.get());
    }

    @Test
    public void updateRegularUserProfileById_profileIsNull() {
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

        verify(updatableRegularUserModelDTOFirebaseRepository).updateDocument(eq(pathToDocument), any(UpdatableRegularUserModelDTO.class), consumerArgumentCaptor.capture());

        Consumer<Boolean> value = consumerArgumentCaptor.getValue();
        value.accept(false);

        assertTrue(methodCalled.get());
    }
}