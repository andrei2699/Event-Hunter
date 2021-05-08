package com.example.eventhunter.profile.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.databinding.FragmentOrganizerFutureEventsBinding;
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

public class OrganizerFutureEventsFragment extends Fragment {
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
        EventCardAdapter eventCardAdapter = new EventCardAdapter(this);

        futureEventsRecyclerView.setAdapter(eventCardAdapter);

        viewModel.getOrganizerId().observe(getViewLifecycleOwner(), id -> {
            eventService.getAllFutureEventCardsForUser(id, eventCardAdapter::updateDataSource);
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
