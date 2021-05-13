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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ProfileServiceTest {

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
    private ArgumentCaptor<Consumer<Boolean>> booleanArgumentCaptor;


    private ProfileService profileService;


    @Before
    public void setUp() {
        profileService = new FirebaseProfileService(photoRepository, eventService, collaboratorRepository,
                organizerRepository, regularUserRepository, updatableOrganizerModelDTOFirebaseRepository,
                updatableCollaboratorModelDTOFirebaseRepository, updatableRegularUserModelDTOFirebaseRepository);
    }

    @After
    public void tearDown() {
        profileService = null;
    }


    @Test
    public void getProfilePhoto_profileIsNotStored() {
        String id = "25125";

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        profileService.getProfilePhoto(id, bitmap -> {
            methodCalled.set(true);
            assertNull(bitmap);
        });

        verify(photoRepository).getPhoto(eq("profiles/" + id), bitmapConsumerArgumentCaptor.capture());
        Consumer<Bitmap> bitmapConsumer = bitmapConsumerArgumentCaptor.getValue();
        bitmapConsumer.accept(null);

        assertTrue(methodCalled.get());
    }

    @Test
    public void getProfilePhoto_profileIsAvailable() {
        String id = "gamlkmlgs";

        AtomicBoolean methodCalled = new AtomicBoolean(false);
        Bitmap bitmap = Mockito.mock(Bitmap.class);

        profileService.getProfilePhoto(id, b -> {
            methodCalled.set(true);
            assertEquals(bitmap, b);
        });

        verify(photoRepository).getPhoto(eq("profiles/" + id), bitmapConsumerArgumentCaptor.capture());
        Consumer<Bitmap> bitmapConsumer = bitmapConsumerArgumentCaptor.getValue();
        bitmapConsumer.accept(bitmap);

        assertTrue(methodCalled.get());
    }

    @Test
    public void updateProfilePhoto_photoIsNull() {
        String id = "azmlkmgvoinm";

        AtomicBoolean methodCalled = new AtomicBoolean(false);

        profileService.uploadProfilePhoto(id, null, b -> {
            methodCalled.set(true);
            assertTrue(b);
        });

        verify(photoRepository, times(0)).updatePhoto(eq("profiles/" + id), nullable(Bitmap.class), booleanArgumentCaptor.capture());

        assertTrue(methodCalled.get());
    }

    @Test
    public void updateProfilePhoto_photoIsNotNull() {
        String id = "azmlkmgvoinm";

        AtomicBoolean methodCalled = new AtomicBoolean(false);
        Bitmap bitmap = Mockito.mock(Bitmap.class);

        profileService.uploadProfilePhoto(id, bitmap, b -> {
            methodCalled.set(true);
            assertTrue(b);
        });

        verify(photoRepository, times(1)).updatePhoto(eq("profiles/" + id), eq(bitmap), booleanArgumentCaptor.capture());
        Consumer<Boolean> bitmapConsumer = booleanArgumentCaptor.getValue();
        bitmapConsumer.accept(true);

        assertTrue(methodCalled.get());
    }

    @Test
    public void updateProfilePhoto_photoIsNotNull_butUploadFails() {
        String id = "lkmlkamsclma";

        AtomicBoolean methodCalled = new AtomicBoolean(false);
        Bitmap bitmap = Mockito.mock(Bitmap.class);

        profileService.uploadProfilePhoto(id, bitmap, b -> {
            methodCalled.set(true);
            assertFalse(b);
        });

        verify(photoRepository, times(1)).updatePhoto(eq("profiles/" + id), eq(bitmap), booleanArgumentCaptor.capture());
        Consumer<Boolean> bitmapConsumer = booleanArgumentCaptor.getValue();
        bitmapConsumer.accept(false);

        assertTrue(methodCalled.get());
    }
}