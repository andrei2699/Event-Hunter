package com.example.eventhunter.ui.createEventForm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.eventhunter.R;
import com.example.eventhunter.databinding.FragmentCreateEventFormOneTimeEventBinding;
import com.example.eventhunter.databinding.FragmentCreateEventFormRepeatableEventBinding;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class CreateEventFormRepeatableEventFragment extends Fragment {

    private EventFormViewModel mViewModel;
    private FragmentCreateEventFormRepeatableEventBinding binding;

    public static CreateEventFormRepeatableEventFragment newInstance() {
        return new CreateEventFormRepeatableEventFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(EventFormViewModel.class);
        binding = FragmentCreateEventFormRepeatableEventBinding.inflate(inflater, container, false);


        binding.repeatableEventPreviousButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigateBackToPhotoAndCollabsFromRepeatableEvent);
        });

        mViewModel.getEventStartDate().observe(getViewLifecycleOwner(), binding.editTextRepeatableEventStartDate::setText);
        mViewModel.getEventRepetitions().observe(getViewLifecycleOwner(), binding.editTextRepeatableEventRepetitions::setText);
        mViewModel.getEventStartHour().observe(getViewLifecycleOwner(), binding.editTextRepeatableEventStartHour::setText);

        mViewModel.getEventEndHour().observe(getViewLifecycleOwner(), binding.editTextRepeatableEventEndHour::setText);

        binding.createRepeatableEventButton.setOnClickListener(view -> {

            mViewModel.setEventStartDate(binding.editTextRepeatableEventStartDate.getText().toString());
            mViewModel.setEventRepetitions(binding.editTextRepeatableEventRepetitions.getText().toString());
            mViewModel.setEventStartHour(binding.editTextRepeatableEventStartHour.getText().toString());
            mViewModel.setEventEndHour(binding.editTextRepeatableEventEndHour.getText().toString());

            // TODO save to DB
            mViewModel.removeValues();

            Navigation.findNavController(view).navigate(R.id.nav_home);
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}