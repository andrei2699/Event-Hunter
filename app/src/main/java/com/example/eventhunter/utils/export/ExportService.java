package com.example.eventhunter.utils.export;

import com.example.eventhunter.reservation.ReservationDetailsCard;

public interface ExportService {

    void exportPDF(ReservationDetailsCard reservationDetailsCard);
}
