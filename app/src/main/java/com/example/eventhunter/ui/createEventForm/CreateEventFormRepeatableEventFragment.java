package com.example.eventhunter.ui.createEventForm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.eventhunter.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class CreateEventFormRepeatableEventFragment extends Fragment {

    private EventFormViewModel mViewModel;

    public static CreateEventFormRepeatableEventFragment newInstance() {
        return new CreateEventFormRepeatableEventFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(EventFormViewModel.class);

        View root = inflater.inflate(R.layout.fragment_create_event_form_repeatable_event, container, false);

        root.findViewById(R.id.repeatableEventPreviousButton).setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigateBackToPhotoAndCollabsFromRepeatableEvent);
        });


        final EditText eventStartDateEditText = root.findViewById(R.id.editTextRepeatableEventStartDate);
        mViewModel.getEventStartDate().observe(getViewLifecycleOwner(), eventStartDateEditText::setText);

        final EditText eventRepetitionsEditText = root.findViewById(R.id.editTextRepeatableEventRepetitions);
        mViewModel.getEventRepetitions().observe(getViewLifecycleOwner(), eventRepetitionsEditText::setText);

        final EditText eventStartHourEditText = root.findViewById(R.id.editTextRepeatableEventStartHour);
        mViewModel.getEventStartHour().observe(getViewLifecycleOwner(), eventStartHourEditText::setText);

        final EditText eventEndHourEditText = root.findViewById(R.id.editTextRepeatableEventEndHour);
        mViewModel.getEventEndHour().observe(getViewLifecycleOwner(), eventEndHourEditText::setText);

        root.findViewById(R.id.createRepeatableEventButton).setOnClickListener(view -> {

            mViewModel.setEventStartDate(eventStartDateEditText.getText().toString());
            mViewModel.setEventRepetitions(eventRepetitionsEditText.getText().toString());
            mViewModel.setEventStartHour(eventStartHourEditText.getText().toString());
            mViewModel.setEventEndHour(eventEndHourEditText.getText().toString());

            // TODO save to DB
            mViewModel.removeValues();

            Navigation.findNavController(view).navigate(R.id.nav_home);
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}