package com.example.eventhunter.profile.organizer.eventDetailsPopup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.eventhunter.R;
import com.example.eventhunter.events.models.EventModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EventDetailsDialogFragment extends DialogFragment {

    private Runnable onDetailsClicked;
    private EventModel eventModel;

    public EventDetailsDialogFragment() {
    }

    public static EventDetailsDialogFragment newInstance(EventModel eventModel, Runnable onDetailsClicked) {
        EventDetailsDialogFragment eventDetailsDialogFragment = new EventDetailsDialogFragment();
        eventDetailsDialogFragment.onDetailsClicked = onDetailsClicked;
        eventDetailsDialogFragment.eventModel = eventModel;

        return eventDetailsDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.event_details_dialog, null);

        TextView eventNameTextField = view.findViewById(R.id.eventNameTextField);
        eventNameTextField.setText(eventModel.eventName);

        TextView eventOrganizerNameTextField = view.findViewById(R.id.eventOrganizerNameTextField);
        eventOrganizerNameTextField.setText(eventModel.organizerName);

        TextView realLocationEventTextField = view.findViewById(R.id.realLocationEventTextField);
        realLocationEventTextField.setText(eventModel.eventLocation);

        TextView realDateEventTextField = view.findViewById(R.id.realDateEventTextField);
        realDateEventTextField.setText(eventModel.eventStartDate);

        TextView realSeatNumberTextField = view.findViewById(R.id.realSeatNumberTextField);
        realSeatNumberTextField.setText(String.valueOf(eventModel.eventSeatNumber));


        return new AlertDialog.Builder(getActivity())
                .setTitle("Event Details")
                .setView(view)
                .setNegativeButton("Close", (dialogInterface, i) -> dismiss())
                .setPositiveButton("Details", (dialogInterface, i) -> {
                    onDetailsClicked.run();
                    dismiss();
                })
                .create();
    }
}
