package com.example.eventhunter.ui.reservationDetailsCard.reservationCardPopup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.eventhunter.R;
import com.example.eventhunter.ui.collaboratorHeader.CollaboratorHeader;
import com.example.eventhunter.ui.mainPage.events.card.EventCard;

import java.util.List;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ReservationCardDialogFragment extends DialogFragment {

    private Consumer<ReservationCardDialogModel> onReservationButtonClick;
    private ReservationCardDialogModel reservationCardDialogModel;

    public ReservationCardDialogFragment() {
    }

    public static ReservationCardDialogFragment newInstance(EventCard eventCard, List<CollaboratorHeader> collaboratorHeaders, Consumer<ReservationCardDialogModel> onReservationButtonClick) {
        ReservationCardDialogFragment reservationCardDialogFragment = new ReservationCardDialogFragment();
        reservationCardDialogFragment.onReservationButtonClick = onReservationButtonClick;
        reservationCardDialogFragment.reservationCardDialogModel =
                new ReservationCardDialogModel(eventCard.eventId, eventCard.eventName, eventCard.organizerName,
                        eventCard.eventDate, eventCard.eventLocation, eventCard.availableSeatsNumber,
                        collaboratorHeaders);

        return reservationCardDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.reservation_card_dialog, null);

//        TextView eventNameTextView = view.findViewById(R.id.eventNameReservationDialog);
//        eventNameTextView.setText(reservationCardDialogModel.eventName);
//
//        TextView organizerNameTextView = view.findViewById(R.id.eventOrganizerReservationDialog);
//        organizerNameTextView.setText(reservationCardDialogModel.organizerName);
//
//        TextView eventDateTextView = view.findViewById(R.id.realDateReservationDialog);
//        eventDateTextView.setText(reservationCardDialogModel.eventDate);
//
//        TextView eventLocationTextView = view.findViewById(R.id.realLocationReservationDialog);
//        eventLocationTextView.setText(reservationCardDialogModel.eventLocation);
//
//        TextView selectedSeatNumberTextView = view.findViewById(R.id.selectedSeatNumberTextViewReservationDialog);
//        selectedSeatNumberTextView.setText("1");
//
//        SeekBar seatNumberSeekBar = view.findViewById(R.id.seatNumberSeekBarReservationDialog);
//        seatNumberSeekBar.setMax(reservationCardDialogModel.availableSeatsNumber);
//
//        seatNumberSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                selectedSeatNumberTextView.setText(String.valueOf(progress));
//                reservationCardDialogModel.chosenSeatsNumber = progress;
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });
//
//        RecyclerView collaboratorsRecyclerView = view.findViewById(R.id.collaboratorsRecyclerViewReservationDialog);

//        collaboratorsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
//        CollaboratorHeader[] collaboratorHeaders = new CollaboratorHeader[reservationCardDialogModel.collaborators.size()];
//        for (int i = 0; i < reservationCardDialogModel.collaborators.size(); i++) {
//            collaboratorHeaders[i] = reservationCardDialogModel.collaborators.get(i);
//        }
//        collaboratorsRecyclerView.setAdapter(new CollaboratorHeaderViewAdapter(collaboratorHeaders));

        return new AlertDialog.Builder(getActivity())
                .setTitle("Make Reservation")
                .setView(view)
                .setPositiveButton("Reserve", (dialogInterface, i) -> {
                    onReservationButtonClick.accept(reservationCardDialogModel);
                    dismiss();
                })
                .setNegativeButton("Close", (dialogInterface, i) -> dismiss())
                .create();
    }
}
