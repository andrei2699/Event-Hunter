package com.example.eventhunter.ui.profile.collaborator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.databinding.FragmentCollaboratorPastEventsBinding;
import com.example.eventhunter.ui.mainPage.events.card.EventCard;
import com.example.eventhunter.ui.mainPage.events.card.EventCardAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CollaboratorPastEventsFragment extends Fragment {

    private CollaboratorProfileViewModel viewModel;
    private FragmentCollaboratorPastEventsBinding binding;

    public CollaboratorPastEventsFragment() {
    }

    public static CollaboratorPastEventsFragment newInstance() {
        return new CollaboratorPastEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCollaboratorPastEventsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(CollaboratorProfileViewModel.class);

        RecyclerView pastEventsRecyclerView = binding.pastEventsRecyclerView;
        EventCard[] events = {
                new EventCard("ID1", "Event1", "Organizer1", "12/03/2021", "Location1", 14),
                new EventCard("ID2", "Event2", "Organizer2", "17/05/2021", "Location2", 57),
                new EventCard("ID3", "Event3", "Organizer3", "31/07/2021", "Location3", 100)};
        pastEventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        pastEventsRecyclerView.setAdapter(new EventCardAdapter(events));

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