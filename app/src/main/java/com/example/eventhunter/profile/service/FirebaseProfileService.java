package com.example.eventhunter.profile.service;

import android.graphics.Bitmap;

import com.example.eventhunter.profile.collaborator.CollaboratorModel;
import com.example.eventhunter.profile.organizer.OrganizerModel;
import com.example.eventhunter.profile.regularUser.RegularUserModel;
import com.example.eventhunter.profile.service.dto.CollaboratorModelDTO;
import com.example.eventhunter.profile.service.dto.OrganizerModelDTO;
import com.example.eventhunter.repository.EventOccurrenceTransmitter;
import com.example.eventhunter.repository.FirebaseRepository;
import com.example.eventhunter.repository.PhotoRepository;

import java.util.function.Consumer;

public class FirebaseProfileService implements OrganizerProfileService, CollaboratorProfileService, RegularUserProfileService {
    private static final String USERS_COLLECTION_PATH = "users";
    private static final String PROFILES_STORAGE_FOLDER_PATH = "profiles";

    private final PhotoRepository photoRepository;
    private final FirebaseRepository<CollaboratorModelDTO> collaboratorRepository;
    private final FirebaseRepository<OrganizerModelDTO> organizerRepository;
    private final FirebaseRepository<RegularUserModel> regularUserRepository;

    public FirebaseProfileService(PhotoRepository photoRepository, FirebaseRepository<CollaboratorModelDTO> collaboratorRepository,
                                  FirebaseRepository<OrganizerModelDTO> organizerRepository, FirebaseRepository<RegularUserModel> regularUserRepository) {
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

        this.photoRepository.updatePhoto(completePhotoPath, collaboratorModel.profilePhoto, transmitter.firstEventConsumer);
        this.collaboratorRepository.updateDocument(completeDocumentPath, collaboratorModelDTO, transmitter.secondEventConsumer);
    }

    @Override
    public void getOrganizerProfileById(String id, Consumer<OrganizerModel> organizerModelConsumer) {
        String completeDocumentPath = USERS_COLLECTION_PATH + "/" + id;
        String completePhotoPath = PROFILES_STORAGE_FOLDER_PATH + "/" + id;
        OrganizerModel organizerModel = new OrganizerModel();

        Consumer<Bitmap> photoConsumer = photo -> {
            organizerModel.profilePhoto = photo;
        };

        Consumer<OrganizerModelDTO> organizerModelDTOConsumer = organizerModelDTO -> {
            organizerModel.id = organizerModelDTO.id;
            organizerModel.address = organizerModelDTO.address;
            organizerModel.email = organizerModelDTO.email;
            organizerModel.name = organizerModelDTO.name;
            organizerModel.phoneNumber = organizerModelDTO.phoneNumber;
            organizerModel.userType = organizerModelDTO.userType;
            organizerModel.organizedEvents = organizerModelDTO.organizedEvents;
            organizerModel.eventType = organizerModelDTO.eventType;
        };

        EventOccurrenceTransmitter<Bitmap, OrganizerModelDTO> transmitter = new EventOccurrenceTransmitter<>(photoConsumer, organizerModelDTOConsumer);

        transmitter.waitAsyncEvents(() -> organizerModelConsumer.accept(organizerModel));

        this.photoRepository.getPhoto(completePhotoPath, transmitter.firstEventConsumer);
        this.organizerRepository.getDocument(completeDocumentPath, OrganizerModelDTO.class, transmitter.secondEventConsumer);
    }

    @Override
    public void updateOrganizerProfile(String id, OrganizerModel organizerModel, Consumer<Boolean> updateConsumer) {

        String completeDocumentPath = USERS_COLLECTION_PATH + "/" + id;
        String completePhotoPath = PROFILES_STORAGE_FOLDER_PATH + "/" + id;

        OrganizerModelDTO organizerModelDTO = new OrganizerModelDTO();
        organizerModelDTO.address = organizerModel.address;
        organizerModelDTO.phoneNumber = organizerModel.phoneNumber;
        organizerModelDTO.eventType = organizerModel.eventType;

        Consumer<Boolean> photoConsumer = e -> {
        };
        Consumer<Boolean> dataConsumer = e -> {
        };

        EventOccurrenceTransmitter<Boolean, Boolean> transmitter = new EventOccurrenceTransmitter<>(photoConsumer, dataConsumer);

        transmitter.waitAsyncEvents(() -> updateConsumer.accept(true));

        this.photoRepository.updatePhoto(completePhotoPath, organizerModel.profilePhoto, transmitter.firstEventConsumer);
        this.organizerRepository.updateDocument(completeDocumentPath, organizerModelDTO, transmitter.secondEventConsumer);
    }

    @Override
    public void getProfilePhoto(String id, Consumer<Bitmap> consumer) {
        String completePhotoPath = PROFILES_STORAGE_FOLDER_PATH + "/" + id;
        photoRepository.getPhoto(completePhotoPath, consumer);
    }

    @Override
    public void uploadProfilePhoto(String id, Bitmap photo, Consumer<Boolean> updateStatus) {
        String completePhotoPath = PROFILES_STORAGE_FOLDER_PATH + "/" + id;
        photoRepository.updatePhoto(completePhotoPath, photo, updateStatus);
    }

    @Override
    public void getAllReservations(String id, Consumer<RegularUserModel> regularUserModelConsumer) {
        String completeDocumentPath = USERS_COLLECTION_PATH + "/" + id;
        regularUserRepository.getDocument(completeDocumentPath, RegularUserModel.class, regularUserModelConsumer);
    }
}
