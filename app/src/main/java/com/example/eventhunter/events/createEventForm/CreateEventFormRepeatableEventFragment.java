package com.example.eventhunter.events.createEventForm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.R;
import com.example.eventhunter.authentication.AuthenticationService;
import com.example.eventhunter.databinding.FragmentCreateEventFormRepeatableEventBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.events.service.EventService;
import com.example.eventhunter.utils.pickDateDialog.PickDateDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class CreateEventFormRepeatableEventFragment extends Fragment {

    private static final int PICK_DATE_DIALOG_REQUEST_CODE = 200;

    @Injectable
    private AuthenticationService authenticationService;

    @Injectable
    private EventService eventService;

    private EventFormViewModel mViewModel;
    private FragmentCreateEventFormRepeatableEventBinding binding;

    public CreateEventFormRepeatableEventFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static CreateEventFormRepeatableEventFragment newInstance() {
        return new CreateEventFormRepeatableEventFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(EventFormViewModel.class);
        binding = FragmentCreateEventFormRepeatableEventBinding.inflate(inflater, container, false);

        binding.repeatableEventPreviousButton.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.navigateBackToPhotoAndCollabsFromRepeatableEvent));

        binding.openStartDatePickerRepeatableEventButton.setOnClickListener(view -> {
            PickDateDialogFragment pickDateDialogFragment = PickDateDialogFragment.newInstance(selectedDate -> mViewModel.setEventStartDate(selectedDate));

            pickDateDialogFragment.setTargetFragment(this, PICK_DATE_DIALOG_REQUEST_CODE);
            pickDateDialogFragment.show(getParentFragmentManager(), "pick_date_dialog");
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

            if (!validateFields()) {
                Snackbar.make(view, "Some Fields Are Empty", Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                authenticationService.getLoggedUserData(loggedUserData ->
                        eventService.createRepeatableEvent(mViewModel, loggedUserData.id, loggedUserData.name, success -> {
                            if (success) {
                                mViewModel.removeValues();

                                Snackbar.make(view, "Event Created!", Snackbar.LENGTH_SHORT)
                                        .show();

                                Navigation.findNavController(view).navigate(R.id.nav_home_events);
                            } else {
                                Snackbar.make(view, "Could not Create Event", Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        }));
            }
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


    private boolean validateFields() {
        if (binding.editTextRepeatableEventStartDate.getText().toString().isEmpty()) {
            return false;
        }

        if (binding.editTextRepeatableEventRepetitions.getText().toString().isEmpty()) {
            return false;
        }

        if (binding.editTextRepeatableEventStartHour.getText().toString().isEmpty()) {
            return false;
        }

        return !binding.editTextRepeatableEventEndHour.getText().toString().isEmpty();
    }
}