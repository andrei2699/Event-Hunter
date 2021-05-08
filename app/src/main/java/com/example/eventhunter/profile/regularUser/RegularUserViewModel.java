package com.example.eventhunter.profile.regularUser;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventhunter.reservation.ReservationDetailsCard;

import java.util.List;

public class RegularUserViewModel extends ViewModel {
    private MutableLiveData<String> regularUserName;
    private MutableLiveData<String> regularUserEmail;
    private MutableLiveData<List<ReservationDetailsCard>> reservations;

    public RegularUserViewModel() {
        regularUserName = new MutableLiveData<>();
        regularUserEmail = new MutableLiveData<>();
        reservations = new MutableLiveData<>();
    }

    public void setRegularUserName(String regularUserName) {
        this.regularUserName.setValue(regularUserName);
    }

    public void setRegularUserEmail(String regularUserEmail) {
        this.regularUserEmail.setValue(regularUserEmail);
    }

    public void setReservations(List<ReservationDetailsCard> reservations) {
        this.reservations.setValue(reservations);
    }

    public MutableLiveData<String> getRegularUserName() {
        return regularUserName;
    }

    public MutableLiveData<String> getRegularUserEmail() {
        return regularUserEmail;
    }

    public MutableLiveData<List<ReservationDetailsCard>> getReservations() {
        return reservations;
    }
}