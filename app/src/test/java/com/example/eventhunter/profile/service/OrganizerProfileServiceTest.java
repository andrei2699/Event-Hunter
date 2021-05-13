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

    @Test
    public void getAllOrganizerProfiles_hasNoProfilesInCollection() {
        String pathToCollection = "users";

        organizerProfileService.getAllOrganizersProfiles(collaboratorModel -> fail());

        verify(organizerRepository, times(1)).getAllDocuments(eq(pathToCollection), eq(OrganizerModelDTO.class), any(Consumer.class));
    }

    @Test
    public void getAllOrganizerProfiles_hasNoProfilesOrganizers_justCollaborators() {
        String pathToCollection = "users";

        int profileCount = 5;
        List<OrganizerModelDTO> expectedDtos = new ArrayList<>();
        for (int i = 0; i < profileCount; i++) {

            OrganizerModelDTO dto = new OrganizerModelDTO();
            dto.userType = "Collaborator";
            dto.address = "Earth street" + i;
            dto.email = "org" + i + "@yahoo.com";
            dto.id = "id" + i;
            dto.name = "Collaborator " + i;
            dto.phoneNumber = "061156" + i;
            dto.organizedEvents = i;

            expectedDtos.add(dto);
        }

        organizerProfileService.getAllOrganizersProfiles(collaboratorModel -> fail());

        verify(organizerRepository, times(1)).getAllDocuments(eq(pathToCollection), eq(OrganizerModelDTO.class), organizerModelDTOConsumerArgumentCaptor.capture());

        Consumer<OrganizerModelDTO> organizerModelDTOConsumer = organizerModelDTOConsumerArgumentCaptor.getValue();

        for (int i = 0; i < profileCount; i++) {
            organizerModelDTOConsumer.accept(expectedDtos.get(i));
        }
    }

    @Test
    public void getAllOrganizerProfiles_hasOnlyOneOrganizerProfile_theRestAreCollaborators() {
        String pathToCollection = "users";

        int profileCount = 5;
        int organizerPosition = profileCount / 2;
        List<OrganizerModelDTO> expectedDtos = new ArrayList<>();
        for (int i = 0; i < profileCount; i++) {

            OrganizerModelDTO dto = new OrganizerModelDTO();
            if (i != organizerPosition) {
                dto.userType = "Collaborator";
                dto.name = "Collaborator " + i;
            } else {
                dto.userType = "Organizer";
                dto.name = "Organizer " + i;
            }

            dto.address = "My street" + i;
            dto.email = "my_mail" + i + "@gmail.com";
            dto.id = "id" + i;
            dto.phoneNumber = "61209" + i;
            dto.organizedEvents = organizerPosition;

            expectedDtos.add(dto);
        }

        AtomicInteger count = new AtomicInteger();

        organizerProfileService.getAllOrganizersProfiles(organizerModel -> {
            count.getAndIncrement();

            assertEquals("Organizer", organizerModel.userType);
            assertEquals("Organizer " + organizerPosition, organizerModel.name);
            assertEquals("My street" + organizerPosition, organizerModel.address);
            assertEquals("my_mail" + organizerPosition + "@gmail.com", organizerModel.email);
            assertEquals("id" + organizerPosition, organizerModel.id);
            assertEquals("61209" + organizerPosition, organizerModel.phoneNumber);
            assertEquals(organizerPosition, organizerModel.organizedEvents);
            assertNotNull(organizerModel.profilePhoto);
        });

        verify(organizerRepository, times(1)).getAllDocuments(eq(pathToCollection), eq(OrganizerModelDTO.class), organizerModelDTOConsumerArgumentCaptor.capture());

        Consumer<OrganizerModelDTO> organizerModelDTOConsumer = organizerModelDTOConsumerArgumentCaptor.getValue();

        for (int i = 0; i < profileCount; i++) {
            organizerModelDTOConsumer.accept(expectedDtos.get(i));
        }

        verify(photoRepository).getPhoto(eq("profiles/id" + organizerPosition), bitmapConsumerArgumentCaptor.capture());
        Consumer<Bitmap> bitmapConsumer = bitmapConsumerArgumentCaptor.getValue();
        bitmapConsumer.accept(Mockito.mock(Bitmap.class));

        assertEquals(1, count.get());
    }

    @Test
    public void getAllOrganizerProfiles_hasOnlyOrganizerProfile() {
        String pathToCollection = "users";

        int profileCount = 5;
        List<OrganizerModelDTO> expectedDtos = new ArrayList<>();
        for (int i = 0; i < profileCount; i++) {

            OrganizerModelDTO dto = new OrganizerModelDTO();
            dto.userType = "Organizer";
            dto.name = "Organizer " + i;
            dto.address = "Organizer street" + i;
            dto.email = "Organizer" + i + "@gmail.com";
            dto.id = "org" + i;
            dto.phoneNumber = "15667" + i;
            dto.organizedEvents = 5;

            expectedDtos.add(dto);
        }

        AtomicInteger count = new AtomicInteger();

        organizerProfileService.getAllOrganizersProfiles(organizerModel -> {
            count.getAndIncrement();

            assertEquals("Organizer", organizerModel.userType);
            assertTrue(organizerModel.name.contains("Organizer"));
            assertTrue(organizerModel.address.contains("Organizer"));
            assertTrue(organizerModel.email.contains("Organizer"));
            assertTrue(organizerModel.id.contains("org"));
            assertTrue(organizerModel.phoneNumber.contains("15667"));
            assertEquals(5, organizerModel.organizedEvents);
            assertNotNull(organizerModel.profilePhoto);
        });

        verify(organizerRepository, times(1)).getAllDocuments(eq(pathToCollection), eq(OrganizerModelDTO.class), organizerModelDTOConsumerArgumentCaptor.capture());

        Consumer<OrganizerModelDTO> organizerModelDTOConsumer = organizerModelDTOConsumerArgumentCaptor.getValue();

        for (int i = 0; i < profileCount; i++) {
            organizerModelDTOConsumer.accept(expectedDtos.get(i));
            verify(photoRepository).getPhoto(eq("profiles/org" + i), bitmapConsumerArgumentCaptor.capture());
            Consumer<Bitmap> bitmapConsumer = bitmapConsumerArgumentCaptor.getValue();
            bitmapConsumer.accept(Mockito.mock(Bitmap.class));
        }

        assertEquals(profileCount, count.get());
    }

    @Test
    public void getAllOrganizerProfiles_hasCollaboratorsAndOrganizersProfile() {
        String pathToCollection = "users";

        int profileCount = 5;
        int organizerCount = 0;

        List<OrganizerModelDTO> expectedDtos = new ArrayList<>();
        for (int i = 0; i < profileCount; i++) {

            OrganizerModelDTO dto = new OrganizerModelDTO();
            if (i % 2 == 0) {
                organizerCount++;
                dto.userType = "Organizer";
                dto.name = "Organizer " + i;
                dto.address = "Organizer street" + i;
                dto.email = "Organizer" + i + "@gmail.com";
                dto.id = "Organizer" + i;
                dto.organizedEvents = 6;
            } else {
                dto.userType = "Collaborator";
                dto.name = "Collaborator " + i;
                dto.address = "Collaborator street" + i;
                dto.email = "Collaborator" + i + "@gmail.com";
                dto.id = "collab" + i;
            }
            dto.phoneNumber = "061156" + i;

            expectedDtos.add(dto);
        }

        AtomicInteger count = new AtomicInteger();

        organizerProfileService.getAllOrganizersProfiles(organizerModel -> {
            count.getAndIncrement();

            assertEquals("Organizer", organizerModel.userType);
            assertTrue(organizerModel.name.contains("Organizer"));
            assertTrue(organizerModel.address.contains("Organizer"));
            assertTrue(organizerModel.email.contains("Organizer"));
            assertTrue(organizerModel.id.contains("Organizer"));
            assertTrue(organizerModel.phoneNumber.contains("061156"));
            assertEquals(6, organizerModel.organizedEvents);
            assertNotNull(organizerModel.profilePhoto);
        });

        verify(organizerRepository, times(1)).getAllDocuments(eq(pathToCollection), eq(OrganizerModelDTO.class), organizerModelDTOConsumerArgumentCaptor.capture());

        Consumer<OrganizerModelDTO> organizerModelDTOConsumer = organizerModelDTOConsumerArgumentCaptor.getValue();

        for (int i = 0; i < profileCount; i++) {
            organizerModelDTOConsumer.accept(expectedDtos.get(i));
            if (i % 2 == 0) {
                verify(photoRepository).getPhoto(eq("profiles/Organizer" + i), bitmapConsumerArgumentCaptor.capture());
                Consumer<Bitmap> bitmapConsumer = bitmapConsumerArgumentCaptor.getValue();
                bitmapConsumer.accept(Mockito.mock(Bitmap.class));
            }
        }

        assertEquals(organizerCount, count.get());
    }
}