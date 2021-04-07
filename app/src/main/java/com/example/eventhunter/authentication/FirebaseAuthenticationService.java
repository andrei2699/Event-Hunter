package com.example.eventhunter.authentication;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.Observer;

public class FirebaseAuthenticationService implements AuthenticationService {

    private final FirebaseAuth firebaseAuth;
    private final Activity activity;
    private FirebaseFirestore firestore;

    private static final String USER_DATA_COLLECTION_PATH = "users";

    public FirebaseAuthenticationService(Activity activity) {
        this.activity = activity;
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
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

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                observer.onChanged(null);
            } else {
                observer.onChanged(task.getException().getMessage());
            }
        });
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
    }

    @Override
    public void register(String email, String password, String name, String userType, Observer<String> observer) {

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || userType.isEmpty()) {
            observer.onChanged("Some Fields Are Empty");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> {
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
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", name);
                        user.put("userType", userType);

                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        return firestore.collection(USER_DATA_COLLECTION_PATH).document(currentUser.getUid()).set(user);
                    }

                    return null;
                })
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        observer.onChanged(null);
                    }
                    return null;
                });
    }

}
