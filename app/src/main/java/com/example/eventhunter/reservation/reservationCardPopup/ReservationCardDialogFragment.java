package com.example.eventhunter.reservation.reservationCardPopup;

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

import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ReservationCardDialogFragment extends DialogFragment {

    private Consumer<ReservationCardDialogModel> onReservationButtonClick;
    private ReservationCardDialogModel reservationCardDialogModel;

    public ReservationCardDialogFragment() {
    }

    public static ReservationCardDialogFragment newInstance(String eventId, int availableSeatsNumber, Double ticketPrice, Consumer<ReservationCardDialogModel> onReservationButtonClick) {
        ReservationCardDialogFragment reservationCardDialogFragment = new ReservationCardDialogFragment();
        reservationCardDialogFragment.onReservationButtonClick = onReservationButtonClick;
        reservationCardDialogFragment.reservationCardDialogModel = new ReservationCardDialogModel(eventId, availableSeatsNumber, ticketPrice);

        return reservationCardDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.reservation_card_dialog, null);

        TextView totalPriceTextView = view.findViewById(R.id.totalPriceReservationCardDialog);
        totalPriceTextView.setText(reservationCardDialogModel.calculateTotalPrice() + "");

        TextView selectedSeatNumberTextView = view.findViewById(R.id.selectedSeatNumberTextViewReservationDialog);
        selectedSeatNumberTextView.setText("1");

        SeekBar seatNumberSeekBar = view.findViewById(R.id.seatNumberSeekBarReservationDialog);
        seatNumberSeekBar.setMax(reservationCardDialogModel.getAvailableSeatsNumber());

        seatNumberSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedSeatNumberTextView.setText(String.valueOf(progress));
                reservationCardDialogModel.setChosenSeatsNumber(progress);
                totalPriceTextView.setText(reservationCardDialogModel.calculateTotalPrice() + "");
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
