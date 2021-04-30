package com.example.eventhunter.profile.collaborator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.databinding.FragmentCollaboratorPastEventsBinding;
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

public class CollaboratorPastEventsFragment extends Fragment {

    private FragmentCollaboratorPastEventsBinding binding;

    @Injectable
    private EventService eventService;

    public CollaboratorPastEventsFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static CollaboratorPastEventsFragment newInstance() {
        return new CollaboratorPastEventsFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCollaboratorPastEventsBinding.inflate(inflater, container, false);
        CollaboratorProfileViewModel viewModel = new ViewModelProvider(requireActivity()).get(CollaboratorProfileViewModel.class);

        RecyclerView pastEventsRecyclerView = binding.pastEventsRecyclerView;
        EventCardAdapter eventCardAdapter = new EventCardAdapter(this);

        pastEventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
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