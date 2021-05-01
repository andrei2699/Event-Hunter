package com.example.eventhunter.authentication;

import android.graphics.Bitmap;

import com.example.eventhunter.profile.service.ProfileService;
import com.example.eventhunter.profile.service.dto.ProfileModelDTO;
import com.example.eventhunter.repository.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.function.Consumer;

import androidx.lifecycle.Observer;

public class FirebaseAuthenticationService implements AuthenticationService {
    private static final String USER_DATA_COLLECTION_PATH = "users";

    private final FirebaseAuth firebaseAuth;
    private final FirebaseRepository<ProfileModelDTO> profileModelDTOFirebaseRepository;
    private final ProfileService profileService;

    private LoggedUserData loggedUserData;

    public FirebaseAuthenticationService(FirebaseRepository<ProfileModelDTO> profileModelDTOFirebaseRepository, ProfileService profileService) {
        this.profileModelDTOFirebaseRepository = profileModelDTOFirebaseRepository;
        this.profileService = profileService;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void getLoggedUserData(Consumer<LoggedUserData> userDataConsumer) {
        if (isLoggedIn() && loggedUserData == null) {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            String path = USER_DATA_COLLECTION_PATH + "/" + currentUser.getUid();
            profileModelDTOFirebaseRepository.getDocument(path, ProfileModelDTO.class, profileModelDTO -> {
                setLoggedUserData(profileModelDTO);
                userDataConsumer.accept(loggedUserData);
            });
        } else {
            userDataConsumer.accept(loggedUserData);
        }
    }

    @Override
    public void getProfilePhoto(Consumer<Bitmap> bitmapConsumer) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            bitmapConsumer.accept(null);
        } else {
            profileService.getProfilePhoto(currentUser.getUid(), bitmapConsumer);
        }
    }

    @Override
    public boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public void login(String email, String password, Observer<String> observer) {
        if (email.isEmpty() || password.isEmpty()) {
            observer.onChanged("Some Fields Are Empty");
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                String path = USER_DATA_COLLECTION_PATH + "/" + currentUser.getUid();
                profileModelDTOFirebaseRepository.getDocument(path, ProfileModelDTO.class, this::setLoggedUserData);
                observer.onChanged(null);
            } else {
                observer.onChanged(task.getException().getMessage());
            }
        });
    }

    @Override
    public void logout() {
        loggedUserData = null;
        firebaseAuth.signOut();
    }

    @Override
    public void register(String email, String password, String name, String userType, Observer<String> observer) {

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || userType.isEmpty()) {
            observer.onChanged("Some Fields Are Empty");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                observer.onChanged(task.getException().getMessage());
            }
        })
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        return currentUser.updateProfile(profileChangeRequest);
                    }
                    return null;
                })
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {


                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                        ProfileModelDTO profileModelDTO = new ProfileModelDTO();

                        profileModelDTO.id = currentUser.getUid();
                        profileModelDTO.email = currentUser.getEmail();
                        profileModelDTO.name = name;
                        profileModelDTO.userType = userType;

                        setLoggedUserData(profileModelDTO);

                        String path = USER_DATA_COLLECTION_PATH + "/" + currentUser.getUid();
                        profileModelDTOFirebaseRepository.updateDocument(path, profileModelDTO, status -> {
                            if (status)
                                observer.onChanged(null);
                            else {
                                observer.onChanged("Updating Profile Error");
                            }
                        });
                    }

                    return null;
                });
    }

    private void setLoggedUserData(ProfileModelDTO profileModelDTO) {
        if (loggedUserData == null) {
            loggedUserData = new LoggedUserData();
        }
        loggedUserData.id = profileModelDTO.id;
        loggedUserData.email = profileModelDTO.email;
        loggedUserData.name = profileModelDTO.name;
        loggedUserData.userType = profileModelDTO.userType;
    }
}
