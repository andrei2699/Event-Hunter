package com.example.eventhunter.ui.reservationDetailsCard.reservationCardPopup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.eventhunter.R;
import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;
import com.example.eventhunter.events.models.EventCard;

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
                new ReservationCardDialogModel(eventCard.getEventId(), eventCard.getEventName(), eventCard.getOrganizerName(),
                        eventCard.getEventDate(), eventCard.getEventLocation(), eventCard.getAvailableSeatsNumber(), eventCard.getTicketPrice(),
                        collaboratorHeaders);

        return reservationCardDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.reservation_card_dialog, null);

        TextView totalPriceTextView = view.findViewById(R.id.totalPriceReservationCardDialog);

        TextView selectedSeatNumberTextView = view.findViewById(R.id.selectedSeatNumberTextViewReservationDialog);
        selectedSeatNumberTextView.setText("1");

        SeekBar seatNumberSeekBar = view.findViewById(R.id.seatNumberSeekBarReservationDialog);
        seatNumberSeekBar.setMax(reservationCardDialogModel.availableSeatsNumber);

        seatNumberSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedSeatNumberTextView.setText(String.valueOf(progress));
                reservationCardDialogModel.chosenSeatsNumber = progress;
                totalPriceTextView.setText(reservationCardDialogModel.chosenSeatsNumber * reservationCardDialogModel.ticketPrice + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        selectedSeatNumberTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!TextUtils.isEmpty(text)) {
                    seatNumberSeekBar.setProgress(Integer.parseInt(text));
                }
            }
        });

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
