package com.example.eventhunter.profile.collaborator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.databinding.FragmentCollaboratorFutureEventsBinding;
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

public class CollaboratorFutureEventsFragment extends Fragment {
    @Injectable
    private EventService eventService;

    private FragmentCollaboratorFutureEventsBinding binding;

    public CollaboratorFutureEventsFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static CollaboratorFutureEventsFragment newInstance() {
        return new CollaboratorFutureEventsFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCollaboratorFutureEventsBinding.inflate(inflater, container, false);
        CollaboratorProfileViewModel viewModel = new ViewModelProvider(requireActivity()).get(CollaboratorProfileViewModel.class);

        RecyclerView futureEventsRecyclerView = binding.futureEventsRecyclerView;

        futureEventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        EventCardAdapter eventCardAdapter = new EventCardAdapter(this);

        futureEventsRecyclerView.setAdapter(eventCardAdapter);

        eventService.getAllFutureEventCardsForUser("TODO", eventModel -> eventCardAdapter.updateDataSource(eventModel.getEventCard()));

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