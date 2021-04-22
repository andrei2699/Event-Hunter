package com.example.eventhunter.ui.profile.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventhunter.databinding.FragmentOrganizerPastEventsBinding;
import com.example.eventhunter.ui.mainPage.events.card.EventCard;
import com.example.eventhunter.ui.mainPage.events.card.EventCardAdapter;
import com.example.eventhunter.ui.reservationDetailsCard.reservationCardPopup.ReservationCardDialogFragment;

import java.util.ArrayList;

public class OrganizerPastEventsFragment extends Fragment {
    private static final int SHOW_RESERVATION_DIALOG_REQUEST_CODE = 100;

    private OrganizerProfileViewModel viewModel;
    private FragmentOrganizerPastEventsBinding binding;

    public OrganizerPastEventsFragment() {
    }

    public static OrganizerPastEventsFragment newInstance() {
        return new OrganizerPastEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOrganizerPastEventsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(OrganizerProfileViewModel.class);

        RecyclerView pastEventsRecyclerView = binding.organizerPastEventsRecyclerView;
        EventCard[] events = {
                new EventCard("ID1", "Event1", "Organizer1", "12/03/2021", "Location1", 14, 20),
                new EventCard("ID2", "Event2", "Organizer2", "17/05/2021", "Location2", 57, 30),
                new EventCard("ID3", "Event3", "Organizer3", "31/07/2021", "Location3", 100, 50)};
        pastEventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        EventCardAdapter eventCardAdapter = new EventCardAdapter(events);
        eventCardAdapter.setOnReserveButtonClick(eventCard -> {
            ReservationCardDialogFragment reservationCardDialogFragment = ReservationCardDialogFragment.newInstance(eventCard, new ArrayList<>(), reservationCardDialogModel -> {
            });
            reservationCardDialogFragment.setTargetFragment(this, SHOW_RESERVATION_DIALOG_REQUEST_CODE);
            reservationCardDialogFragment.show(getParentFragmentManager(), "reservation_card_dialog");
        });
        pastEventsRecyclerView.setAdapter(eventCardAdapter);

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
