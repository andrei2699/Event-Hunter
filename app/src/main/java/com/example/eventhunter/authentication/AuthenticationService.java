package com.example.eventhunter.authentication;

import android.graphics.Bitmap;

import java.util.function.Consumer;

import androidx.lifecycle.Observer;

public interface AuthenticationService {
    void getLoggedUserData(Consumer<LoggedUserData> userDataConsumer);

    void getProfilePhoto(Consumer<Bitmap> bitmapConsumer);

    boolean isLoggedIn();

    void login(String email, String password, Observer<String> observer);

    void logout();

    void register(String email, String password, String name, String userType, Observer<String> observer);
}
