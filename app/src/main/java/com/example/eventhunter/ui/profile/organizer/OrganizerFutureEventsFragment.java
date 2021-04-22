package com.example.eventhunter.ui.profile.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.databinding.FragmentOrganizerFutureEventsBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.events.service.EventService;
import com.example.eventhunter.ui.mainPage.events.card.EventCard;
import com.example.eventhunter.ui.mainPage.events.card.EventCardAdapter;
import com.example.eventhunter.ui.reservationDetailsCard.reservationCardPopup.ReservationCardDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrganizerFutureEventsFragment extends Fragment {
    private static final int SHOW_RESERVATION_DIALOG_REQUEST_CODE = 100;

    @Injectable
    private EventService eventService;

    private FragmentOrganizerFutureEventsBinding binding;

    public OrganizerFutureEventsFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static OrganizerFutureEventsFragment newInstance() {
        return new OrganizerFutureEventsFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrganizerFutureEventsBinding.inflate(inflater, container, false);
        OrganizerProfileViewModel viewModel = new ViewModelProvider(requireActivity()).get(OrganizerProfileViewModel.class);

        RecyclerView futureEventsRecyclerView = binding.organizerFutureEventsRecyclerView;

        futureEventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        EventCardAdapter eventCardAdapter = new EventCardAdapter();
        eventCardAdapter.setOnReserveButtonClick(eventCard -> {
            ReservationCardDialogFragment reservationCardDialogFragment = ReservationCardDialogFragment.newInstance(eventCard, new ArrayList<>(), reservationCardDialogModel -> {
            });
            reservationCardDialogFragment.setTargetFragment(this, SHOW_RESERVATION_DIALOG_REQUEST_CODE);
            reservationCardDialogFragment.show(getParentFragmentManager(), "reservation_card_dialog");
        });
        futureEventsRecyclerView.setAdapter(eventCardAdapter);

        eventService.getAllFutureEventsForUser("TODO", eventCardDTOS -> {
            List<EventCard> eventCards = eventCardDTOS.stream()
                    .map(eventCardDTO ->
                            new EventCard(eventCardDTO.getEventId(), eventCardDTO.getEventName(),
                                    eventCardDTO.getOrganizerName(), eventCardDTO.getEventDate(),
                                    eventCardDTO.getEventLocation(), eventCardDTO.getTicketPrice(),
                                    eventCardDTO.getEventSeatNumber(), eventCardDTO.getEventImage()))
                    .collect(Collectors.toList());

            eventCardAdapter.updateDataSource(eventCards);
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
