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
public class CollaboratorProfileServiceTest {
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
    private ArgumentCaptor<Consumer<Bitmap>> bitmapConsumerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Consumer<CollaboratorModelDTO>> collaboratorModelDTOConsumerArgumentCaptor;

    private CollaboratorProfileService collaboratorProfileService;


    @Before
    public void setUp() {
        collaboratorProfileService = new FirebaseProfileService(photoRepository, eventService, collaboratorRepository,
                organizerRepository, regularUserRepository, updatableOrganizerModelDTOFirebaseRepository,
                updatableCollaboratorModelDTOFirebaseRepository, updatableRegularUserModelDTOFirebaseRepository);
    }

    @After
    public void tearDown() {
        collaboratorProfileService = null;
    }

    @Test
    public void getCollaboratorProfileById_ProfileDoesNotExist() {
        String id = "213";
        String pathToDocument = "users/" + id;
        String photoPathToDocument = "profiles/" + id;

        CollaboratorModelDTO emptyDto = new CollaboratorModelDTO();
        emptyDto.userType = "";
        emptyDto.address = "";
        emptyDto.email = "";
        emptyDto.id = "";
        emptyDto.name = "";
        emptyDto.phoneNumber = "";

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        collaboratorProfileService.getCollaboratorProfileById(id, collaboratorModel -> {
            methodCalled.set(true);
            assertNotNull(collaboratorModel);

            assertEquals("", collaboratorModel.address);
            assertEquals("", collaboratorModel.email);
            assertEquals("", collaboratorModel.id);
            assertEquals("", collaboratorModel.name);
            assertEquals("", collaboratorModel.phoneNumber);
            assertEquals("", collaboratorModel.userType);
            assertNull(collaboratorModel.profilePhoto);
        });

        verify(collaboratorRepository).getDocument(eq(pathToDocument), eq(CollaboratorModelDTO.class), collaboratorModelDTOConsumerArgumentCaptor.capture());
        Consumer<CollaboratorModelDTO> collaboratorModelDTOConsumer = collaboratorModelDTOConsumerArgumentCaptor.getValue();
        collaboratorModelDTOConsumer.accept(emptyDto);

        verify(photoRepository).getPhoto(eq(photoPathToDocument), bitmapConsumerArgumentCaptor.capture());
        Consumer<Bitmap> bitmapConsumer = bitmapConsumerArgumentCaptor.getValue();
        bitmapConsumer.accept(null);

        assertTrue(methodCalled.get());
    }

    @Test
    public void getCollaboratorProfileById_ProfileDoesExistButHasNoImage() {
        String id = "213";
        String pathToDocument = "users/" + id;
        String photoPathToDocument = "profiles/" + id;

        CollaboratorModelDTO expectedDto = new CollaboratorModelDTO();
        expectedDto.userType = "Collaborator";
        expectedDto.address = "Sun street";
        expectedDto.email = "collab@gmail.com";
        expectedDto.id = id;
        expectedDto.name = "CollabName";
        expectedDto.phoneNumber = "068691";

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        collaboratorProfileService.getCollaboratorProfileById(id, collaboratorModel -> {
            methodCalled.set(true);
            assertNotNull(collaboratorModel);

            assertEquals(expectedDto.address, collaboratorModel.address);
            assertEquals(expectedDto.email, collaboratorModel.email);
            assertEquals(expectedDto.id, collaboratorModel.id);
            assertEquals(expectedDto.name, collaboratorModel.name);
            assertEquals(expectedDto.phoneNumber, collaboratorModel.phoneNumber);
            assertEquals(expectedDto.userType, collaboratorModel.userType);
            assertNull(collaboratorModel.profilePhoto);
        });

        verify(collaboratorRepository).getDocument(eq(pathToDocument), eq(CollaboratorModelDTO.class), collaboratorModelDTOConsumerArgumentCaptor.capture());
        Consumer<CollaboratorModelDTO> collaboratorModelDTOConsumer = collaboratorModelDTOConsumerArgumentCaptor.getValue();
        collaboratorModelDTOConsumer.accept(expectedDto);

        verify(photoRepository).getPhoto(eq(photoPathToDocument), bitmapConsumerArgumentCaptor.capture());
        Consumer<Bitmap> bitmapConsumer = bitmapConsumerArgumentCaptor.getValue();
        bitmapConsumer.accept(null);

        assertTrue(methodCalled.get());
    }

    @Test
    public void getCollaboratorProfileById_ProfileDoesExistAndHasImage() {
        String id = "213";
        String pathToDocument = "users/" + id;
        String photoPathToDocument = "profiles/" + id;

        Bitmap photo = Mockito.mock(Bitmap.class);

        CollaboratorModelDTO expectedDto = new CollaboratorModelDTO();
        expectedDto.userType = "Collaborator";
        expectedDto.address = "Martian street";
        expectedDto.email = "collab12@gmail.com";
        expectedDto.id = id;
        expectedDto.name = "_nume_ collab";
        expectedDto.phoneNumber = "";

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        collaboratorProfileService.getCollaboratorProfileById(id, collaboratorModel -> {
            methodCalled.set(true);
            assertNotNull(collaboratorModel);

            assertEquals(expectedDto.address, collaboratorModel.address);
            assertEquals(expectedDto.email, collaboratorModel.email);
            assertEquals(expectedDto.id, collaboratorModel.id);
            assertEquals(expectedDto.name, collaboratorModel.name);
            assertEquals(expectedDto.phoneNumber, collaboratorModel.phoneNumber);
            assertEquals(expectedDto.userType, collaboratorModel.userType);
            assertEquals(photo, collaboratorModel.profilePhoto);
        });

        verify(photoRepository).getPhoto(eq(photoPathToDocument), bitmapConsumerArgumentCaptor.capture());
        Consumer<Bitmap> bitmapConsumer = bitmapConsumerArgumentCaptor.getValue();
        bitmapConsumer.accept(photo);

        verify(collaboratorRepository).getDocument(eq(pathToDocument), eq(CollaboratorModelDTO.class), collaboratorModelDTOConsumerArgumentCaptor.capture());
        Consumer<CollaboratorModelDTO> collaboratorModelDTOConsumer = collaboratorModelDTOConsumerArgumentCaptor.getValue();
        collaboratorModelDTOConsumer.accept(expectedDto);

        assertTrue(methodCalled.get());
    }
}