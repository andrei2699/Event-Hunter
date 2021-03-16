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

public class CreateEventFormOneTimeEventFragment extends Fragment {

    private EventFormViewModel mViewModel;

    public static CreateEventFormOneTimeEventFragment newInstance() {
        return new CreateEventFormOneTimeEventFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(EventFormViewModel.class);
        View root = inflater.inflate(R.layout.fragment_create_event_form_one_time_event, container, false);

        root.findViewById(R.id.oneTimeEventPreviousButton).setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigateBackToPhotoAndCollabsFromOneTimeEvent);
        });

        final EditText eventStartDateEditText = root.findViewById(R.id.editTextOneTimeEventStartDate);
        mViewModel.getEventStartDate().observe(getViewLifecycleOwner(), eventStartDateEditText::setText);

        final EditText eventEndDateEditText = root.findViewById(R.id.editTextOneTimeEventEndDate);
        mViewModel.getEventEndDate().observe(getViewLifecycleOwner(), eventEndDateEditText::setText);

        final EditText eventStartHourEditText = root.findViewById(R.id.editTextOneTimeEventStartHour);
        mViewModel.getEventStartHour().observe(getViewLifecycleOwner(), eventStartHourEditText::setText);

        final EditText eventEndHourEditText = root.findViewById(R.id.editTextOneTimeEventEndHour);
        mViewModel.getEventEndHour().observe(getViewLifecycleOwner(), eventEndHourEditText::setText);

        root.findViewById(R.id.createOneTimeEventButton).setOnClickListener(view -> {

            mViewModel.setEventStartDate(eventStartDateEditText.getText().toString());
            mViewModel.setEventEndDate(eventEndDateEditText.getText().toString());
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