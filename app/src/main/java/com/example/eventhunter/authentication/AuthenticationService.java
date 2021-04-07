package com.example.eventhunter.authentication;

import androidx.lifecycle.Observer;

public interface AuthenticationService {
    boolean isLoggedIn();

    void login(String email, String password, Observer<String> observer);

    void logout();

    void register(String email, String password, String name, String userType, Observer<String> observer);
}
