package com.example.eventhunter.profile.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.databinding.FragmentOrganizerPastEventsBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.events.service.EventService;
import com.example.eventhunter.ui.mainPage.events.card.EventCardAdapter;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrganizerPastEventsFragment extends Fragment {
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
        EventCardAdapter eventCardAdapter = new EventCardAdapter(this);

        pastEventsRecyclerView.setAdapter(eventCardAdapter);

        viewModel.getOrganizerId().observe(getViewLifecycleOwner(), id -> {
            eventService.getAllPastEventCardsForUser(id, eventCardAdapter::updateDataSource);
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
