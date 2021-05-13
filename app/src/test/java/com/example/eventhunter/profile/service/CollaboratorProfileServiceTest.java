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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
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
        collaboratorModelDTOConsumer.accept(null);

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

    @Test
    public void getAllCollaboratorProfiles_hasNoProfilesInCollection() {
        String pathToCollection = "users";

        collaboratorProfileService.getAllCollaboratorProfiles(collaboratorModel -> fail());

        verify(collaboratorRepository, times(1)).getAllDocuments(eq(pathToCollection), eq(CollaboratorModelDTO.class), any(Consumer.class));
    }

    @Test
    public void getAllCollaboratorProfiles_hasNoProfilesCollaborators_justOrganizer() {
        String pathToCollection = "users";

        int profileCount = 5;
        List<CollaboratorModelDTO> expectedDtos = new ArrayList<>();
        for (int i = 0; i < profileCount; i++) {

            CollaboratorModelDTO dto = new CollaboratorModelDTO();
            dto.userType = "Organizer";
            dto.address = "Martian street" + i;
            dto.email = "collab12" + i + "@gmail.com";
            dto.id = "id" + i;
            dto.name = "Organizer " + i;
            dto.phoneNumber = "061" + i + "156";

            expectedDtos.add(dto);
        }

        collaboratorProfileService.getAllCollaboratorProfiles(collaboratorModel -> fail());

        verify(collaboratorRepository, times(1)).getAllDocuments(eq(pathToCollection), eq(CollaboratorModelDTO.class), collaboratorModelDTOConsumerArgumentCaptor.capture());

        Consumer<CollaboratorModelDTO> collaboratorModelDTOConsumer = collaboratorModelDTOConsumerArgumentCaptor.getValue();

        for (int i = 0; i < profileCount; i++) {
            collaboratorModelDTOConsumer.accept(expectedDtos.get(i));
        }
    }

    @Test
    public void getAllCollaboratorProfiles_hasOnlyOneCollaboratorsProfile_theRestAreOrganizers() {
        String pathToCollection = "users";

        int profileCount = 5;
        int collaboratorPosition = profileCount / 2;
        List<CollaboratorModelDTO> expectedDtos = new ArrayList<>();
        for (int i = 0; i < profileCount; i++) {

            CollaboratorModelDTO dto = new CollaboratorModelDTO();
            if (i == collaboratorPosition) {
                dto.userType = "Collaborator";
                dto.name = "Collaborator " + i;
            } else {
                dto.userType = "Organizer";
                dto.name = "Organizer " + i;
            }

            dto.address = "Martian street" + i;
            dto.email = "collab12" + i + "@gmail.com";
            dto.id = "id" + i;
            dto.phoneNumber = "061156" + i;

            expectedDtos.add(dto);
        }

        AtomicInteger count = new AtomicInteger();

        collaboratorProfileService.getAllCollaboratorProfiles(collaboratorModel -> {
            count.getAndIncrement();

            assertEquals("Collaborator", collaboratorModel.userType);
            assertEquals("Collaborator " + collaboratorPosition, collaboratorModel.name);
            assertEquals("Martian street" + collaboratorPosition, collaboratorModel.address);
            assertEquals("collab12" + collaboratorPosition + "@gmail.com", collaboratorModel.email);
            assertEquals("id" + collaboratorPosition, collaboratorModel.id);
            assertEquals("061156" + collaboratorPosition, collaboratorModel.phoneNumber);
            assertNotNull(collaboratorModel.profilePhoto);
        });

        verify(collaboratorRepository, times(1)).getAllDocuments(eq(pathToCollection), eq(CollaboratorModelDTO.class), collaboratorModelDTOConsumerArgumentCaptor.capture());

        Consumer<CollaboratorModelDTO> collaboratorModelDTOConsumer = collaboratorModelDTOConsumerArgumentCaptor.getValue();

        for (int i = 0; i < profileCount; i++) {
            collaboratorModelDTOConsumer.accept(expectedDtos.get(i));
        }

        verify(photoRepository).getPhoto(eq("profiles/id" + collaboratorPosition), bitmapConsumerArgumentCaptor.capture());
        Consumer<Bitmap> bitmapConsumer = bitmapConsumerArgumentCaptor.getValue();
        bitmapConsumer.accept(Mockito.mock(Bitmap.class));

        assertEquals(1, count.get());
    }

    @Test
    public void getAllCollaboratorProfiles_hasOnlyCollaboratorsProfile() {
        String pathToCollection = "users";

        int profileCount = 5;
        List<CollaboratorModelDTO> expectedDtos = new ArrayList<>();
        for (int i = 0; i < profileCount; i++) {

            CollaboratorModelDTO dto = new CollaboratorModelDTO();
            dto.userType = "Collaborator";
            dto.name = "Collaborator " + i;
            dto.address = "Collaborator street" + i;
            dto.email = "Collaborator" + i + "@gmail.com";
            dto.id = "collab" + i;
            dto.phoneNumber = "061156" + i;

            expectedDtos.add(dto);
        }

        AtomicInteger count = new AtomicInteger();

        collaboratorProfileService.getAllCollaboratorProfiles(collaboratorModel -> {
            count.getAndIncrement();

            assertEquals("Collaborator", collaboratorModel.userType);
            assertTrue(collaboratorModel.name.contains("Collaborator"));
            assertTrue(collaboratorModel.address.contains("Collaborator"));
            assertTrue(collaboratorModel.email.contains("Collaborator"));
            assertTrue(collaboratorModel.id.contains("collab"));
            assertTrue(collaboratorModel.phoneNumber.contains("1156"));
            assertNotNull(collaboratorModel.profilePhoto);
        });

        verify(collaboratorRepository, times(1)).getAllDocuments(eq(pathToCollection), eq(CollaboratorModelDTO.class), collaboratorModelDTOConsumerArgumentCaptor.capture());

        Consumer<CollaboratorModelDTO> collaboratorModelDTOConsumer = collaboratorModelDTOConsumerArgumentCaptor.getValue();

        for (int i = 0; i < profileCount; i++) {
            collaboratorModelDTOConsumer.accept(expectedDtos.get(i));
            verify(photoRepository).getPhoto(eq("profiles/collab" + i), bitmapConsumerArgumentCaptor.capture());
            Consumer<Bitmap> bitmapConsumer = bitmapConsumerArgumentCaptor.getValue();
            bitmapConsumer.accept(Mockito.mock(Bitmap.class));
        }

        assertEquals(profileCount, count.get());
    }

    @Test
    public void getAllCollaboratorProfiles_hasCollaboratorsAndOrganizersProfile() {
        String pathToCollection = "users";

        int profileCount = 5;
        int collaboratorsCount = 0;

        List<CollaboratorModelDTO> expectedDtos = new ArrayList<>();
        for (int i = 0; i < profileCount; i++) {

            CollaboratorModelDTO dto = new CollaboratorModelDTO();
            if (i % 2 == 0) {
                collaboratorsCount++;
                dto.userType = "Collaborator";
                dto.name = "Collaborator " + i;
                dto.address = "Collaborator street" + i;
                dto.email = "Collaborator" + i + "@gmail.com";
                dto.id = "collab" + i;
            } else {
                dto.userType = "Organizer";
                dto.name = "Organizer " + i;
                dto.address = "Organizer street" + i;
                dto.email = "Organizer" + i + "@gmail.com";
                dto.id = "Organizer" + i;
            }
            dto.phoneNumber = "061156" + i;

            expectedDtos.add(dto);
        }

        AtomicInteger count = new AtomicInteger();

        collaboratorProfileService.getAllCollaboratorProfiles(collaboratorModel -> {
            count.getAndIncrement();

            assertEquals("Collaborator", collaboratorModel.userType);
            assertTrue(collaboratorModel.name.contains("Collaborator"));
            assertTrue(collaboratorModel.address.contains("Collaborator"));
            assertTrue(collaboratorModel.email.contains("Collaborator"));
            assertTrue(collaboratorModel.id.contains("collab"));
            assertTrue(collaboratorModel.phoneNumber.contains("1156"));
            assertNotNull(collaboratorModel.profilePhoto);
        });

        verify(collaboratorRepository, times(1)).getAllDocuments(eq(pathToCollection), eq(CollaboratorModelDTO.class), collaboratorModelDTOConsumerArgumentCaptor.capture());

        Consumer<CollaboratorModelDTO> collaboratorModelDTOConsumer = collaboratorModelDTOConsumerArgumentCaptor.getValue();

        for (int i = 0; i < profileCount; i++) {
            collaboratorModelDTOConsumer.accept(expectedDtos.get(i));
            if (i % 2 == 0) {
                verify(photoRepository).getPhoto(eq("profiles/collab" + i), bitmapConsumerArgumentCaptor.capture());
                Consumer<Bitmap> bitmapConsumer = bitmapConsumerArgumentCaptor.getValue();
                bitmapConsumer.accept(Mockito.mock(Bitmap.class));
            }
        }

        assertEquals(collaboratorsCount, count.get());
    }
}