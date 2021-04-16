package com.example.eventhunter.utils.pickDateDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;

import com.example.eventhunter.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PickDateDialogFragment extends DialogFragment {

    private Consumer<String> onDateSelected;
    private String selectedDate = "";

    public static PickDateDialogFragment newInstance(Consumer<String> onDateSelected) {
        PickDateDialogFragment pickDateDialogFragment = new PickDateDialogFragment();
        pickDateDialogFragment.onDateSelected = onDateSelected;

        return pickDateDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.pick_date_dialog, null);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDate = sdf.format(new Date());

        CalendarView calendar = view.findViewById(R.id.pickDateDialogCalendarView);
        calendar.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.getDefault(), "%d/%d/%d", dayOfMonth, month + 1, year);
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle("Pick Date")
                .setView(view)
                .setPositiveButton("Pick", (dialogInterface, i) -> {
                    onDateSelected.accept(selectedDate);
                    dismiss();
                })
                .setNegativeButton("Close", (dialogInterface, i) -> dismiss())
                .create();
    }
}
