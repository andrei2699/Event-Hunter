package com.example.eventhunter.ui.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<String> emailAddress;
    private MutableLiveData<String> password;
    private MutableLiveData<String> name;
    private MutableLiveData<String> userType;

    public RegisterViewModel () {
        emailAddress = new MutableLiveData<>();
        password = new MutableLiveData<>();
        name = new MutableLiveData<>();
        userType = new MutableLiveData<>();
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

    public MutableLiveData<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public MutableLiveData<String> getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType.setValue(userType);
    }
}