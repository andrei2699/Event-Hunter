package com.example.eventhunter.profile.organizer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.EventDay;
import com.example.eventhunter.R;
import com.example.eventhunter.databinding.FragmentOrganizerInfoBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.events.models.EventModel;
import com.example.eventhunter.events.service.EventService;
import com.example.eventhunter.profile.organizer.eventDetailsPopup.EventDetailsDialogFragment;
import com.example.eventhunter.utils.DateVerifier;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class OrganizerInfoFragment extends Fragment {
    private static final int EVENT_DETAILS_DIALOG_REQUEST_CODE = 120;

    @Injectable
    private EventService eventService;

    private OrganizerProfileViewModel viewModel;
    private FragmentOrganizerInfoBinding binding;

    public OrganizerInfoFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static OrganizerInfoFragment newInstance() {
        return new OrganizerInfoFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrganizerInfoBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(OrganizerProfileViewModel.class);

        viewModel.getOrganizerAddress().observe(getViewLifecycleOwner(), binding.organizerAddressTextView::setText);
        viewModel.getOrganizerPhoneNumber().observe(getViewLifecycleOwner(), binding.organizerPhoneNumberTextView::setText);
        viewModel.getOrganizerNumberOfOrganizedEvents().observe(getViewLifecycleOwner(), binding.numberOfOrganizedEventsTextView::setText);
        viewModel.getOrganizerType().observe(getViewLifecycleOwner(), binding.organizerTypeTextView::setText);
        viewModel.getOrganizerEmail().observe(getViewLifecycleOwner(), binding.organizerEmailTextView::setText);

        List<EventModel> eventModels = new ArrayList<>();
        List<EventDay> events = new ArrayList<>();

        viewModel.getOrganizerId().observe(getViewLifecycleOwner(), organizerId ->
                eventService.getAllFutureEventCardsForUser(organizerId, eventModel -> {
                    eventModels.add(eventModel);

                    try {
                        Date startDate = DateVerifier.getDateFromString(eventModel.eventStartDate);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(startDate);

                        events.add(new EventDay(calendar, R.drawable.ic_baseline_date_range_24_teal, Color.parseColor("#00796B")));
                        binding.calendarViewOrganizerSchedule.setEvents(events);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }));

        binding.calendarViewOrganizerSchedule.setOnDayClickListener(eventDay -> {

            Date eventDate = eventDay.getCalendar().getTime();
            EventModel model = eventModels.stream()
                    .filter(eventModel -> DateVerifier.compareStringDateAndDate(eventModel.eventStartDate, eventDate))
                    .findFirst().orElse(null);

            if (model == null) {
                return;
            }
            EventDetailsDialogFragment reservationCardDialogFragment = EventDetailsDialogFragment.newInstance(model);
            reservationCardDialogFragment.setTargetFragment(this, EVENT_DETAILS_DIALOG_REQUEST_CODE);
            reservationCardDialogFragment.show(getParentFragmentManager(), "event_reservation_dialog");
            System.out.println(eventDay);
        });

        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
