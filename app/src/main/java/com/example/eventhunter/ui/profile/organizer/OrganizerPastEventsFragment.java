package com.example.eventhunter.ui.profile.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.databinding.FragmentOrganizerPastEventsBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.events.service.EventService;
import com.example.eventhunter.ui.mainPage.events.card.EventCardAdapter;
import com.example.eventhunter.ui.reservationDetailsCard.reservationCardPopup.ReservationCardDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrganizerPastEventsFragment extends Fragment {
    private static final int SHOW_RESERVATION_DIALOG_REQUEST_CODE = 100;

    @Injectable
    private EventService eventService;

    private FragmentOrganizerPastEventsBinding binding;

    public OrganizerPastEventsFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static OrganizerPastEventsFragment newInstance() {
        return new OrganizerPastEventsFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrganizerPastEventsBinding.inflate(inflater, container, false);
        OrganizerProfileViewModel viewModel = new ViewModelProvider(requireActivity()).get(OrganizerProfileViewModel.class);

        RecyclerView pastEventsRecyclerView = binding.organizerPastEventsRecyclerView;

        pastEventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        EventCardAdapter eventCardAdapter = new EventCardAdapter();
        eventCardAdapter.setOnReserveButtonClick(eventCard -> {
            ReservationCardDialogFragment reservationCardDialogFragment = ReservationCardDialogFragment.newInstance(eventCard, new ArrayList<>(), reservationCardDialogModel -> {
            });
            reservationCardDialogFragment.setTargetFragment(this, SHOW_RESERVATION_DIALOG_REQUEST_CODE);
            reservationCardDialogFragment.show(getParentFragmentManager(), "reservation_card_dialog");
        });
        pastEventsRecyclerView.setAdapter(eventCardAdapter);

        eventService.getAllPastEventCardsForUser("TODO", eventCardAdapter::updateDataSource);

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
