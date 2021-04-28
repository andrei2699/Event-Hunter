package com.example.eventhunter.events.eventDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.collaborator.ui.header.CollaboratorHeaderViewAdapter;
import com.example.eventhunter.databinding.EventDetailsFragmentBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.events.service.EventService;
import com.example.eventhunter.ui.reservationDetailsCard.reservationCardPopup.ReservationCardDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventDetailsFragment extends Fragment {
    private static final int EVENT_RESERVATION_DIALOG_REQUEST_CODE = 100;

    @Injectable
    private EventService eventService;

    private EventDetailsViewModel mViewModel;
    private EventDetailsFragmentBinding binding;

    public EventDetailsFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static EventDetailsFragment newInstance() {
        return new EventDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = EventDetailsFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(EventDetailsViewModel.class);

        setupViewModelObservers();

        String eventId = getArguments().getString("eventId");
        if (eventId != null && !eventId.isEmpty()) {
            eventService.getEvent(eventId, eventModelDTO -> {
                mViewModel.setEventName(eventModelDTO.getEventName());
                mViewModel.setEventOrganizerName(eventModelDTO.getOrganizerName());
                mViewModel.setEventLocation(eventModelDTO.getEventLocation());
                mViewModel.setEventTicketPrice(eventModelDTO.getTicketPrice() + "");
                mViewModel.setEventDate(eventModelDTO.getEventDate());
                mViewModel.setEventStartHour(eventModelDTO.getEventStartHour());
                mViewModel.setEventEndHour(eventModelDTO.getEventEndHour());
                mViewModel.setEventSeatNumber(eventModelDTO.getEventSeatNumber() + "");
                mViewModel.setEventDescription(eventModelDTO.getEventDescription());
                mViewModel.setEventType(eventModelDTO.getEventType());
                mViewModel.setEventCollaborators(eventModelDTO.getCollaborators());
            });

            eventService.getEventPhoto(eventId, bitmap -> mViewModel.setEventPhoto(bitmap));
        }

        binding.reserveTicketsButtonEventDetailsPage.setOnClickListener(view -> {
            ReservationCardDialogFragment reservationCardDialogFragment = ReservationCardDialogFragment.newInstance(eventId, mViewModel.getEventSeatNumberValue(), mViewModel.getTicketPriceValue(), reservationCardDialogModel -> {
                // TODO save reservation to DB

                int eventSeatNumberValue = mViewModel.getEventSeatNumberValue();
                eventSeatNumberValue -= reservationCardDialogModel.getChosenSeatsNumber();
                mViewModel.setEventSeatNumber(eventSeatNumberValue + "");
            });
            reservationCardDialogFragment.setTargetFragment(this, EVENT_RESERVATION_DIALOG_REQUEST_CODE);
            reservationCardDialogFragment.show(getParentFragmentManager(), "event_reservation_dialog");
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

    private void setupViewModelObservers() {
        RecyclerView collaboratorsRecyclerView = binding.collaboratorsRecycleViewEventDetails;

        CollaboratorHeaderViewAdapter collaboratorHeaderViewAdapter = new CollaboratorHeaderViewAdapter();

        collaboratorsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        collaboratorsRecyclerView.setAdapter(collaboratorHeaderViewAdapter);

        mViewModel.getEventName().observe(getViewLifecycleOwner(), name -> {
            binding.eventNameEventDetailsPage.setText(name);
        });
        mViewModel.getEventOrganizerName().observe(getViewLifecycleOwner(), organizerName -> {
            binding.organizerNameEventDetails.setText(organizerName);
        });
        mViewModel.getEventLocation().observe(getViewLifecycleOwner(), location -> {
            binding.eventLocationEventDetails.setText(location);
        });
        mViewModel.getEventTicketPrice().observe(getViewLifecycleOwner(), ticketPrice -> {
            binding.eventTicketPriceEventDetailsPage.setText(ticketPrice);
        });
        mViewModel.getEventDate().observe(getViewLifecycleOwner(), date -> {
            binding.eventDateEventDetails.setText(date);
        });
        mViewModel.getEventStartHour().observe(getViewLifecycleOwner(), startHour -> {
            binding.eventStartHourEventDetailsPage.setText(startHour);
        });
        mViewModel.getEventEndHour().observe(getViewLifecycleOwner(), endHour -> {
            binding.eventEndHourEventDetailsPage.setText(endHour);
        });
        mViewModel.getEventSeatNumber().observe(getViewLifecycleOwner(), seatNumber -> {
            binding.eventSeatNumberEventDetailsPage.setText(seatNumber);
        });
        mViewModel.getEventDescription().observe(getViewLifecycleOwner(), description -> {
            binding.eventDescriptionEventDetailsPage.setText(description);
        });
        mViewModel.getEventType().observe(getViewLifecycleOwner(), type -> {
            binding.eventTypeEventDetailsPage.setText(type);
        });
        mViewModel.getEventCollaborators().observe(getViewLifecycleOwner(), collaboratorHeaderViewAdapter::setCollaborators);

        mViewModel.getEventPhoto().observe(getViewLifecycleOwner(), bitmap -> binding.eventImageEventDetailsPage.setImageBitmap(bitmap));
    }
}