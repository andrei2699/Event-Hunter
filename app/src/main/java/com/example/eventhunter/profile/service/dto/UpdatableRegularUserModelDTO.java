package com.example.eventhunter.profile.service.dto;

import com.example.eventhunter.reservation.dto.ReservationModelDTO;

import java.util.List;

public class UpdatableRegularUserModelDTO {

    public int reservationsNumber;
    public List<ReservationModelDTO> reservations;
}
