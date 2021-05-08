package com.example.eventhunter.profile.service;

import android.graphics.Bitmap;
import android.media.audiofx.PresetReverb;

import com.example.eventhunter.events.service.EventService;
import com.example.eventhunter.profile.collaborator.CollaboratorModel;
import com.example.eventhunter.profile.organizer.OrganizerModel;
import com.example.eventhunter.profile.regularUser.RegularUserModel;
import com.example.eventhunter.profile.service.dto.CollaboratorModelDTO;
import com.example.eventhunter.profile.service.dto.OrganizerModelDTO;
import com.example.eventhunter.profile.service.dto.RegularUserModelDTO;
import com.example.eventhunter.profile.service.dto.UpdatableCollaboratorModelDTO;
import com.example.eventhunter.profile.service.dto.UpdatableOrganizerModelDTO;
import com.example.eventhunter.profile.service.dto.UpdatableRegularUserModelDTO;
import com.example.eventhunter.repository.EventOccurrenceTransmitter;
import com.example.eventhunter.repository.FirebaseRepository;
import com.example.eventhunter.repository.PhotoRepository;
import com.example.eventhunter.reservation.ReservationModel;
import com.example.eventhunter.reservation.dto.ReservationModelDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FirebaseProfileService implements OrganizerProfileService, CollaboratorProfileService, RegularUserProfileService {
    private static final String USERS_COLLECTION_PATH = "users";
    private static final String PROFILES_STORAGE_FOLDER_PATH = "profiles";

    private final PhotoRepository photoRepository;
    private final EventService eventService;
    private final FirebaseRepository<CollaboratorModelDTO> collaboratorRepository;
    private final FirebaseRepository<OrganizerModelDTO> organizerRepository;
    private final FirebaseRepository<RegularUserModelDTO> regularUserRepository;
    private final FirebaseRepository<UpdatableOrganizerModelDTO> updatableOrganizerModelDTOFirebaseRepository;
    private final FirebaseRepository<UpdatableCollaboratorModelDTO> updatableCollaboratorModelDTOFirebaseRepository;
    private final FirebaseRepository<UpdatableRegularUserModelDTO> updatableRegularUserModelDTOFirebaseRepository;

    public FirebaseProfileService(PhotoRepository photoRepository,
                                  EventService eventService, FirebaseRepository<CollaboratorModelDTO> collaboratorRepository,
                                  FirebaseRepository<OrganizerModelDTO> organizerRepository,
                                  FirebaseRepository<RegularUserModelDTO> regularUserRepository,
                                  FirebaseRepository<UpdatableOrganizerModelDTO> updatableOrganizerModelDTOFirebaseRepository,
                                  FirebaseRepository<UpdatableCollaboratorModelDTO> updatableCollaboratorModelDTOFirebaseRepository,
                                  FirebaseRepository<UpdatableRegularUserModelDTO> updatableRegularUserModelDTOFirebaseRepository) {
        this.eventService = eventService;
        this.collaboratorRepository = collaboratorRepository;
        this.photoRepository = photoRepository;
        this.organizerRepository = organizerRepository;
        this.regularUserRepository = regularUserRepository;
        this.updatableOrganizerModelDTOFirebaseRepository = updatableOrganizerModelDTOFirebaseRepository;
        this.updatableCollaboratorModelDTOFirebaseRepository = updatableCollaboratorModelDTOFirebaseRepository;
        this.updatableRegularUserModelDTOFirebaseRepository = updatableRegularUserModelDTOFirebaseRepository;
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

        UpdatableCollaboratorModelDTO updatableCollaboratorModelDTO = new UpdatableCollaboratorModelDTO();
        updatableCollaboratorModelDTO.address = collaboratorModel.address;
        updatableCollaboratorModelDTO.phoneNumber = collaboratorModel.phoneNumber;

        Consumer<Boolean> photoConsumer = e -> {
        };
        Consumer<Boolean> dataConsumer = e -> {
            System.out.println(e);
        };

        EventOccurrenceTransmitter<Boolean, Boolean> transmitter = new EventOccurrenceTransmitter<>(photoConsumer, dataConsumer);

        transmitter.waitAsyncEvents(() -> updateConsumer.accept(true));

        this.photoRepository.updatePhoto(completePhotoPath, collaboratorModel.profilePhoto, transmitter.firstEventConsumer);
        this.updatableCollaboratorModelDTOFirebaseRepository.updateDocument(completeDocumentPath, updatableCollaboratorModelDTO, transmitter.secondEventConsumer);
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

        UpdatableOrganizerModelDTO updatableOrganizerModelDTO = new UpdatableOrganizerModelDTO();
        updatableOrganizerModelDTO.address = organizerModel.address;
        updatableOrganizerModelDTO.phoneNumber = organizerModel.phoneNumber;
        updatableOrganizerModelDTO.eventType = organizerModel.eventType;
        updatableOrganizerModelDTO.organizedEvents = organizerModel.organizedEvents;

        Consumer<Boolean> photoConsumer = e -> {
        };
        Consumer<Boolean> dataConsumer = e -> {
        };

        EventOccurrenceTransmitter<Boolean, Boolean> transmitter = new EventOccurrenceTransmitter<>(photoConsumer, dataConsumer);

        transmitter.waitAsyncEvents(() -> updateConsumer.accept(true));

        this.photoRepository.updatePhoto(completePhotoPath, organizerModel.profilePhoto, transmitter.firstEventConsumer);
        this.updatableOrganizerModelDTOFirebaseRepository.updateDocument(completeDocumentPath, updatableOrganizerModelDTO, transmitter.secondEventConsumer);
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
    public void getRegularUserProfileById(String id, Consumer<RegularUserModel> regularUserModelConsumer) {
        String completeDocumentPath = USERS_COLLECTION_PATH + "/" + id;

        regularUserRepository.getDocument(completeDocumentPath, RegularUserModelDTO.class, regularUserModelDTO -> {
            List<ReservationModel> reservationModelList = new ArrayList<>();

            if(regularUserModelDTO.reservations == null || regularUserModelDTO.reservations.size() == 0) {
                RegularUserModel regularUserModel = new RegularUserModel(regularUserModelDTO.id, regularUserModelDTO.name, regularUserModelDTO.userType,
                        regularUserModelDTO.email, regularUserModelDTO.reservationsNumber, reservationModelList);

                regularUserModelConsumer.accept(regularUserModel);
                return;
            }

            for(ReservationModelDTO reservation: regularUserModelDTO.reservations) {

                this.eventService.getEvent(reservation.eventId,eventModel -> {

                    ReservationModel reservationModel = new ReservationModel(reservation.eventId, reservation.userId, reservation.reservationId,
                            reservation.eventName, reservation.eventLocation, reservation.eventStartDate, reservation.eventStartHour, reservation.ticketPrice,
                            reservation.reservedSeatsNumber, eventModel.eventPhoto);

                    reservationModelList.add(reservationModel);

                    if(reservationModelList.size() == regularUserModelDTO.reservations.size()) {
                        RegularUserModel regularUserModel = new RegularUserModel(regularUserModelDTO.id, regularUserModelDTO.name, regularUserModelDTO.userType,
                                regularUserModelDTO.email, regularUserModelDTO.reservationsNumber, reservationModelList);

                        regularUserModelConsumer.accept(regularUserModel);
                    }
                });
            }
        });
    }

    @Override
    public void updateRegularUserProfileById(String id, RegularUserModel regularUserModel, Consumer<Boolean> updateConsumer) {
        String completeDocumentPath = USERS_COLLECTION_PATH + "/" + id;

        UpdatableRegularUserModelDTO updatableRegularUserModelDTO = new UpdatableRegularUserModelDTO();
        updatableRegularUserModelDTO.reservations = regularUserModel.reservations.stream().map(reservation -> {
            return new ReservationModelDTO(reservation.eventId, reservation.userId, reservation.reservationId, reservation.eventName,
                    reservation.eventLocation, reservation.eventStartDate, reservation.eventStartHour, reservation.ticketPrice, reservation.reservedSeatsNumber);
            }).collect(Collectors.toList());
        updatableRegularUserModelDTO.reservationsNumber = regularUserModel.reservationsNumber;

        this.updatableRegularUserModelDTOFirebaseRepository.updateDocument(completeDocumentPath, updatableRegularUserModelDTO, updateConsumer);
    }
}
