package com.example.eventhunter.ui.mainpage.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.databinding.FragmentHomeEventsBinding;
import com.example.eventhunter.ui.collaboratorHeader.CollaboratorHeader;
import com.example.eventhunter.ui.mainpage.events.card.EventCard;
import com.example.eventhunter.ui.mainpage.events.card.EventCardAdapter;
import com.example.eventhunter.ui.reservationDetailsCard.reservationCardPopup.ReservationCardDialogFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainPageEventsFragment extends Fragment {

    private static final int EVENT_RESERVATION_DIALOG_REQUEST_CODE = 100;

    private MainPageEventsViewModel mainPageEventsViewModel;
    private FragmentHomeEventsBinding binding;

    public static MainPageEventsFragment newInstance() {
        return new MainPageEventsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeEventsBinding.inflate(inflater, container, false);
        mainPageEventsViewModel = new ViewModelProvider(requireActivity()).get(MainPageEventsViewModel.class);

        RecyclerView eventsRecyclerView = binding.homeEventsRecyclerView;
        EventCard[] events = {
                new EventCard("id1", "Event1", "Organizer1", "12/03/2021", "Location1", 14),
                new EventCard("id2", "Event2", "Organizer2", "17/05/2021", "Location2", 57),
                new EventCard("id3", "Event3", "Organizer3", "31/07/2021", "Location3", 100)};
        List<CollaboratorHeader> collaborators = new ArrayList<>();
        collaborators.add(new CollaboratorHeader("Dummy1"));
        collaborators.add(new CollaboratorHeader("Dummy2"));
        collaborators.add(new CollaboratorHeader("Dummy3"));

        EventCardAdapter eventCardAdapter = new EventCardAdapter(events);
        eventCardAdapter.setOnReserveButtonClick(eventCard -> {
            ReservationCardDialogFragment reservationCardDialogFragment = ReservationCardDialogFragment.newInstance(eventCard, collaborators, reservationCardDialogModel -> {
                // TODO save reservation to DB

                eventCard.availableSeatsNumber -= reservationCardDialogModel.chosenSeatsNumber;
                eventCardAdapter.notifyDataSetChanged();
            });
            reservationCardDialogFragment.setTargetFragment(this, EVENT_RESERVATION_DIALOG_REQUEST_CODE);
            reservationCardDialogFragment.show(getParentFragmentManager(), "event_reservation_dialog");
        });

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        eventsRecyclerView.setAdapter(eventCardAdapter);

        View view = binding.getRoot();
        return view;
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