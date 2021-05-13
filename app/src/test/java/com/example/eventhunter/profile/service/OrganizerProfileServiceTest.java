package com.example.eventhunter.profile.service;

import android.graphics.Bitmap;

import com.example.eventhunter.events.service.FirebaseEventService;
import com.example.eventhunter.profile.service.dto.CollaboratorModelDTO;
import com.example.eventhunter.profile.service.dto.OrganizerModelDTO;
import com.example.eventhunter.profile.service.dto.RegularUserModelDTO;
import com.example.eventhunter.profile.service.dto.UpdatableCollaboratorModelDTO;
import com.example.eventhunter.profile.service.dto.UpdatableOrganizerModelDTO;
import com.example.eventhunter.profile.service.dto.UpdatableRegularUserModelDTO;
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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrganizerProfileServiceTest {
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
    private ArgumentCaptor<Consumer<Boolean>> booleanArgumentCaptor;

    @Captor
    private ArgumentCaptor<Consumer<Bitmap>> bitmapConsumerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Consumer<OrganizerModelDTO>> organizerModelDTOConsumerArgumentCaptor;

    private OrganizerProfileService organizerProfileService;


    @Before
    public void setUp() {
        organizerProfileService = new FirebaseProfileService(photoRepository, eventService, collaboratorRepository,
                organizerRepository, regularUserRepository, updatableOrganizerModelDTOFirebaseRepository,
                updatableCollaboratorModelDTOFirebaseRepository, updatableRegularUserModelDTOFirebaseRepository);
    }

    @After
    public void tearDown() {
        organizerProfileService = null;
    }

    @Test
    public void getOrganizerProfileById_ProfileDoesNotExist() {
        String id = "2zzx13";
        String pathToDocument = "users/" + id;
        String photoPathToDocument = "profiles/" + id;

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        organizerProfileService.getOrganizerProfileById(id, organizerModel -> {
            methodCalled.set(true);
            assertNotNull(organizerModel);

            assertEquals("", organizerModel.address);
            assertEquals("", organizerModel.email);
            assertEquals("", organizerModel.id);
            assertEquals("", organizerModel.name);
            assertEquals("", organizerModel.phoneNumber);
            assertEquals("", organizerModel.userType);
            assertEquals(0, organizerModel.organizedEvents);
            assertNull(organizerModel.profilePhoto);
        });

        verify(organizerRepository).getDocument(eq(pathToDocument), eq(OrganizerModelDTO.class), organizerModelDTOConsumerArgumentCaptor.capture());
        Consumer<OrganizerModelDTO> collaboratorModelDTOConsumer = organizerModelDTOConsumerArgumentCaptor.getValue();
        collaboratorModelDTOConsumer.accept(null);

        verify(photoRepository).getPhoto(eq(photoPathToDocument), bitmapConsumerArgumentCaptor.capture());
        Consumer<Bitmap> bitmapConsumer = bitmapConsumerArgumentCaptor.getValue();
        bitmapConsumer.accept(null);

        assertTrue(methodCalled.get());
    }

    @Test
    public void getOrganizerProfileById_ProfileDoesExistButHasNoImage() {
        String id = "213cz125";
        String pathToDocument = "users/" + id;
        String photoPathToDocument = "profiles/" + id;

        OrganizerModelDTO expectedDto = new OrganizerModelDTO();
        expectedDto.userType = "Organizer";
        expectedDto.address = "Sun street";
        expectedDto.email = "organizer@gmail.com";
        expectedDto.id = id;
        expectedDto.name = "OrganizerName";
        expectedDto.phoneNumber = "075912";

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        organizerProfileService.getOrganizerProfileById(id, organizerModel -> {
            methodCalled.set(true);
            assertNotNull(organizerModel);

            assertEquals(expectedDto.address, organizerModel.address);
            assertEquals(expectedDto.email, organizerModel.email);
            assertEquals(expectedDto.id, organizerModel.id);
            assertEquals(expectedDto.name, organizerModel.name);
            assertEquals(expectedDto.phoneNumber, organizerModel.phoneNumber);
            assertEquals(expectedDto.userType, organizerModel.userType);
            assertNull(organizerModel.profilePhoto);
        });

        verify(organizerRepository).getDocument(eq(pathToDocument), eq(OrganizerModelDTO.class), organizerModelDTOConsumerArgumentCaptor.capture());
        Consumer<OrganizerModelDTO> collaboratorModelDTOConsumer = organizerModelDTOConsumerArgumentCaptor.getValue();
        collaboratorModelDTOConsumer.accept(expectedDto);

        verify(photoRepository).getPhoto(eq(photoPathToDocument), bitmapConsumerArgumentCaptor.capture());
        Consumer<Bitmap> bitmapConsumer = bitmapConsumerArgumentCaptor.getValue();
        bitmapConsumer.accept(null);

        assertTrue(methodCalled.get());
    }

    @Test
    public void getOrganizerProfileById_ProfileDoesExistAndHasImage() {
        String id = "mmznbjf";
        String pathToDocument = "users/" + id;
        String photoPathToDocument = "profiles/" + id;

        Bitmap photo = Mockito.mock(Bitmap.class);

        OrganizerModelDTO expectedDto = new OrganizerModelDTO();
        expectedDto.userType = "Organizer";
        expectedDto.address = "Martian street";
        expectedDto.email = "organizer1245@gmail.com";
        expectedDto.id = id;
        expectedDto.name = "org name";
        expectedDto.phoneNumber = "";

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        organizerProfileService.getOrganizerProfileById(id, organizerModel -> {
            methodCalled.set(true);
            assertNotNull(organizerModel);

            assertEquals(expectedDto.address, organizerModel.address);
            assertEquals(expectedDto.email, organizerModel.email);
            assertEquals(expectedDto.id, organizerModel.id);
            assertEquals(expectedDto.name, organizerModel.name);
            assertEquals(expectedDto.phoneNumber, organizerModel.phoneNumber);
            assertEquals(expectedDto.userType, organizerModel.userType);
            assertEquals(photo, organizerModel.profilePhoto);
        });

        verify(photoRepository).getPhoto(eq(photoPathToDocument), bitmapConsumerArgumentCaptor.capture());
        Consumer<Bitmap> bitmapConsumer = bitmapConsumerArgumentCaptor.getValue();
        bitmapConsumer.accept(photo);

        verify(organizerRepository).getDocument(eq(pathToDocument), eq(OrganizerModelDTO.class), organizerModelDTOConsumerArgumentCaptor.capture());
        Consumer<OrganizerModelDTO> collaboratorModelDTOConsumer = organizerModelDTOConsumerArgumentCaptor.getValue();
        collaboratorModelDTOConsumer.accept(expectedDto);

        assertTrue(methodCalled.get());
    }
}