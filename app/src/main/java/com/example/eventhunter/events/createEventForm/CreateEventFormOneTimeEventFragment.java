package com.example.eventhunter.events.createEventForm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.R;
import com.example.eventhunter.authentication.AuthenticationService;
import com.example.eventhunter.databinding.FragmentCreateEventFormOneTimeEventBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.events.models.EventModel;
import com.example.eventhunter.events.service.EventService;
import com.example.eventhunter.profile.service.OrganizerProfileService;
import com.example.eventhunter.utils.pickDateDialog.PickDateDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class CreateEventFormOneTimeEventFragment extends Fragment {

    private static final int PICK_DATE_DIALOG_REQUEST_CODE = 200;

    @Injectable
    private AuthenticationService authenticationService;

    @Injectable
    private OrganizerProfileService organizerProfileService;

    @Injectable
    private EventService eventService;

    private EventFormViewModel mViewModel;
    private FragmentCreateEventFormOneTimeEventBinding binding;

    public CreateEventFormOneTimeEventFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static CreateEventFormOneTimeEventFragment newInstance() {
        return new CreateEventFormOneTimeEventFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(EventFormViewModel.class);
        binding = FragmentCreateEventFormOneTimeEventBinding.inflate(inflater, container, false);

        binding.oneTimeEventPreviousButton.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.navigateBackToPhotoAndCollabsFromOneTimeEvent));

        mViewModel.getEventStartDate().observe(getViewLifecycleOwner(), binding.editTextOneTimeEventStartDate::setText);
        mViewModel.getEventEndDate().observe(getViewLifecycleOwner(), binding.editTextOneTimeEventEndDate::setText);
        mViewModel.getEventStartHour().observe(getViewLifecycleOwner(), binding.editTextOneTimeEventStartHour::setText);
        mViewModel.getEventEndHour().observe(getViewLifecycleOwner(), binding.editTextOneTimeEventEndHour::setText);

        binding.openStartDatePickerButton.setOnClickListener(view -> showPickDateDialog(selectedDate -> mViewModel.setEventStartDate(selectedDate)));

        binding.openEndDatePickerButton.setOnClickListener(view -> showPickDateDialog(selectedDate -> mViewModel.setEventEndDate(selectedDate)));

        binding.createOneTimeEventButton.setOnClickListener(view -> {

            mViewModel.setEventStartDate(binding.editTextOneTimeEventStartDate.getText().toString());
            mViewModel.setEventEndDate(binding.editTextOneTimeEventEndDate.getText().toString());
            mViewModel.setEventStartHour(binding.editTextOneTimeEventStartHour.getText().toString());
            mViewModel.setEventEndHour(binding.editTextOneTimeEventEndHour.getText().toString());


            if (!validateFields()) {
                Snackbar.make(view, "Some Fields Are Empty", Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                authenticationService.getLoggedUserData(loggedUserData -> {

                    String seatNumberString = mViewModel.getEventSeatNumber().getValue();
                    int seatNumber = 0;
                    if (seatNumberString != null && !seatNumberString.isEmpty()) {
                        seatNumber = Integer.parseInt(seatNumberString);
                    }

                    double ticketPrice = 0.0;
                    String ticketPriceString = mViewModel.getEventSeatNumber().getValue();
                    if (ticketPriceString != null && !ticketPriceString.isEmpty()) {
                        ticketPrice = Double.parseDouble(ticketPriceString);
                    }

                    EventModel eventModel = new EventModel(
                            mViewModel.getEventName().getValue(), mViewModel.getEventDescription().getValue(),
                            seatNumber, mViewModel.getEventLocation().getValue(),
                            mViewModel.getEventType().getValue(), mViewModel.getEventStartDate().getValue(),
                            mViewModel.getEventEndDate().getValue(), mViewModel.getEventStartHour().getValue(),
                            mViewModel.getEventEndHour().getValue(), ticketPrice,
                            loggedUserData.id, loggedUserData.name, mViewModel.getCollaboratorsDTO(),
                            mViewModel.getEventPhoto().getValue()
                    );

                    eventService.createOneTimeEvent(eventModel, success -> {
                        if (success) {

                            organizerProfileService.updateOrganizerEventCount(loggedUserData.id, 1, sc -> {
                                if (sc) {
                                    mViewModel.removeValues();

                                    Snackbar.make(view, "Event Created!", Snackbar.LENGTH_SHORT)
                                            .show();

                                    Navigation.findNavController(view).navigate(R.id.nav_home_events);

                                } else {
                                    Snackbar.make(view, "Could not Create Event", Snackbar.LENGTH_SHORT)
                                            .show();
                                }
                            });
                        }
                    });
                });
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

    private void showPickDateDialog(Consumer<String> onDateSelected) {
        PickDateDialogFragment pickDateDialogFragment = PickDateDialogFragment.newInstance(onDateSelected);
        pickDateDialogFragment.setTargetFragment(this, PICK_DATE_DIALOG_REQUEST_CODE);
        pickDateDialogFragment.show(getParentFragmentManager(), "pick_date_dialog");
    }

    private boolean validateFields() {
        if (binding.editTextOneTimeEventStartDate.getText().toString().isEmpty()) {
            return false;
        }

        if (binding.editTextOneTimeEventEndDate.getText().toString().isEmpty()) {
            return false;
        }

        if (binding.editTextOneTimeEventStartHour.getText().toString().isEmpty()) {
            return false;
        }

        return !binding.editTextOneTimeEventEndHour.getText().toString().isEmpty();
    }
}