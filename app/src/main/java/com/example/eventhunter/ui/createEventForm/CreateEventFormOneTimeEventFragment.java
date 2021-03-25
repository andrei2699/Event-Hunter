package com.example.eventhunter.ui.createEventForm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.R;
import com.example.eventhunter.databinding.FragmentCreateEventFormOneTimeEventBinding;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class CreateEventFormOneTimeEventFragment extends Fragment {

    private EventFormViewModel mViewModel;
    private FragmentCreateEventFormOneTimeEventBinding binding;

    public static CreateEventFormOneTimeEventFragment newInstance() {
        return new CreateEventFormOneTimeEventFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(EventFormViewModel.class);
        binding = FragmentCreateEventFormOneTimeEventBinding.inflate(inflater, container, false);

        binding.oneTimeEventPreviousButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigateBackToPhotoAndCollabsFromOneTimeEvent);
        });

        mViewModel.getEventStartDate().observe(getViewLifecycleOwner(), binding.editTextOneTimeEventStartDate::setText);
        mViewModel.getEventEndDate().observe(getViewLifecycleOwner(), binding.editTextOneTimeEventEndDate::setText);
        mViewModel.getEventStartHour().observe(getViewLifecycleOwner(), binding.editTextOneTimeEventStartHour::setText);
        mViewModel.getEventEndHour().observe(getViewLifecycleOwner(), binding.editTextOneTimeEventEndHour::setText);

        binding.createOneTimeEventButton.setOnClickListener(view -> {

            mViewModel.setEventStartDate(binding.editTextOneTimeEventStartDate.getText().toString());
            mViewModel.setEventEndDate(binding.editTextOneTimeEventEndDate.getText().toString());
            mViewModel.setEventStartHour(binding.editTextOneTimeEventStartHour.getText().toString());
            mViewModel.setEventEndHour(binding.editTextOneTimeEventEndHour.getText().toString());

            // TODO save to DB
            mViewModel.removeValues();

            Navigation.findNavController(view).navigate(R.id.nav_home_events);
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