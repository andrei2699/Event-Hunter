package com.example.eventhunter.ui.mainPage.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;
import com.example.eventhunter.databinding.FragmentHomeEventsBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.events.service.EventService;
import com.example.eventhunter.ui.mainPage.events.card.EventCardAdapter;
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

    @Injectable
    private EventService eventService;

    private static final int EVENT_RESERVATION_DIALOG_REQUEST_CODE = 100;

    private FragmentHomeEventsBinding binding;

    public MainPageEventsFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static MainPageEventsFragment newInstance() {
        return new MainPageEventsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeEventsBinding.inflate(inflater, container, false);
        MainPageEventsViewModel mainPageEventsViewModel = new ViewModelProvider(requireActivity()).get(MainPageEventsViewModel.class);

        RecyclerView eventsRecyclerView = binding.homeEventsRecyclerView;

        EventCardAdapter eventCardAdapter = new EventCardAdapter();
        eventCardAdapter.setOnReserveButtonClick(eventCard -> {
            ReservationCardDialogFragment reservationCardDialogFragment = ReservationCardDialogFragment.newInstance(eventCard, reservationCardDialogModel -> {
                // TODO save reservation to DB

                eventCard.removeAvailableSeats(reservationCardDialogModel.getChosenSeatsNumber());
                eventCardAdapter.notifyDataSetChanged();
            });
            reservationCardDialogFragment.setTargetFragment(this, EVENT_RESERVATION_DIALOG_REQUEST_CODE);
            reservationCardDialogFragment.show(getParentFragmentManager(), "event_reservation_dialog");
        });
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        eventsRecyclerView.setAdapter(eventCardAdapter);

        eventService.getAllEventCards(eventCardAdapter::updateDataSource);

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