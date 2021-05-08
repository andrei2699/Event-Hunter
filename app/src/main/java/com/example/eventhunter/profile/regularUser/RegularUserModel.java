package com.example.eventhunter.profile.regularUser;

import com.example.eventhunter.reservation.ReservationModel;

import java.util.List;

public class RegularUserModel {

    public String id;
    public String name;
    public String userType;
    public String email;
    public int reservationsNumber;
    public List<ReservationModel> reservations;

    public RegularUserModel(String id, String name, String userType, String email, int reservationsNumber, List<ReservationModel> reservations) {
        this.id = id;
        this.name = name;
        this.userType = userType;
        this.email = email;
        this.reservationsNumber = reservationsNumber;
        this.reservations = reservations;
    }
}
