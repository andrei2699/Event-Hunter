package com.example.eventhunter.profile.service;

import android.graphics.Bitmap;

import com.example.eventhunter.profile.collaborator.CollaboratorModel;
import com.example.eventhunter.profile.service.dto.CollaboratorModelDTO;
import com.example.eventhunter.profile.service.dto.OrganizerModelDTO;
import com.example.eventhunter.profile.service.dto.RegularUserModelDTO;
import com.example.eventhunter.repository.EventOccurrenceTransmitter;
import com.example.eventhunter.repository.FirebaseRepository;
import com.example.eventhunter.repository.PhotoRepository;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FirebaseProfileService implements OrganizerProfileService, CollaboratorProfileService, RegularUserProfileService {
    private static final String USERS_COLLECTION_PATH = "users";
    private static final String PROFILES_STORAGE_FOLDER_PATH = "profiles";

    private final PhotoRepository photoRepository;
    private final FirebaseRepository<CollaboratorModelDTO> collaboratorRepository;
    private final FirebaseRepository<OrganizerModelDTO> organizerRepository;
    private final FirebaseRepository<RegularUserModelDTO> regularUserRepository;

    public FirebaseProfileService( PhotoRepository photoRepository, FirebaseRepository<CollaboratorModelDTO> collaboratorRepository, FirebaseRepository<OrganizerModelDTO> organizerRepository, FirebaseRepository<RegularUserModelDTO> regularUserRepository) {
        this.collaboratorRepository = collaboratorRepository;
        this.photoRepository = photoRepository;
        this.organizerRepository = organizerRepository;
        this.regularUserRepository = regularUserRepository;
    }

    @Override
    public void getCollaboratorProfileById(String id, Consumer<CollaboratorModel> collaboratorModelConsumer) {

        String completeDocumentPath = USERS_COLLECTION_PATH + "/" + id;
        String completePhotoPath = PROFILES_STORAGE_FOLDER_PATH + "/" + id;
        CollaboratorModel collaboratorModel = new CollaboratorModel();

        Consumer<Bitmap> photoConsumer = photo -> {
            collaboratorModel.profilePhoto = photo;
        };
        Consumer<CollaboratorModelDTO> collaboratorModelDTOConsumer = collaboratorModelDTO -> {
            collaboratorModel.id = collaboratorModelDTO.id;
            collaboratorModel.address = collaboratorModelDTO.address;
            collaboratorModel.email = collaboratorModelDTO.email;
            collaboratorModel.name = collaboratorModelDTO.name;
            collaboratorModel.phoneNumber = collaboratorModelDTO.phoneNumber;
            collaboratorModel.userType = collaboratorModelDTO.userType;
        };

        EventOccurrenceTransmitter<Bitmap, CollaboratorModelDTO> transmitter = new EventOccurrenceTransmitter<>(photoConsumer, collaboratorModelDTOConsumer);

        transmitter.waitAsyncEvents(() -> collaboratorModelConsumer.accept(collaboratorModel));

        this.photoRepository.getPhoto(completePhotoPath, transmitter.firstEventConsumer);
        this.collaboratorRepository.getDocument(completeDocumentPath, CollaboratorModelDTO.class, transmitter.secondEventConsumer);
    }

    @Override
    public void updateCollaboratorProfile(String id, CollaboratorModel collaboratorModel, Consumer<Boolean> updateConsumer) {

        String completeDocumentPath = USERS_COLLECTION_PATH + "/" + id;
        String completePhotoPath = PROFILES_STORAGE_FOLDER_PATH + "/" + id;

        CollaboratorModelDTO collaboratorModelDTO = new CollaboratorModelDTO();
        collaboratorModelDTO.address = collaboratorModel.address;
        collaboratorModelDTO.phoneNumber = collaboratorModel.phoneNumber;

        Consumer<Boolean> photoConsumer = e -> {
        };
        Consumer<Boolean> dataConsumer = e -> {
        };

        EventOccurrenceTransmitter<Boolean, Boolean> transmitter = new EventOccurrenceTransmitter<>(photoConsumer, dataConsumer);

        transmitter.waitAsyncEvents(() -> updateConsumer.accept(true));

        this.photoRepository.updatePhoto(completePhotoPath, collaboratorModel.profilePhoto, photoConsumer);
        this.collaboratorRepository.updateDocument(completeDocumentPath, collaboratorModelDTO, dataConsumer);
    }

    @Override
    public void getOrganizerProfileById(String id) {

    }

    @Override
    public void updateOrganizerProfile(String id) {

    }

    @Override
    public void getProfilePhoto(String id) {

    }

    @Override
    public void uploadProfilePhoto(String id) {

    }

    @Override
    public void getAllReservations(String id) {

    }
}
