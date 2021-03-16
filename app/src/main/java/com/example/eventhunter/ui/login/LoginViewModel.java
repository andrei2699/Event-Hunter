package com.example.eventhunter.ui.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> emailAddress;
    private MutableLiveData<String> password;

    public LoginViewModel () {
        emailAddress = new MutableLiveData<>();
        password = new MutableLiveData<>();
    }

    public MutableLiveData<String> getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress.setValue(emailAddress);
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password.setValue(password);
    }
}