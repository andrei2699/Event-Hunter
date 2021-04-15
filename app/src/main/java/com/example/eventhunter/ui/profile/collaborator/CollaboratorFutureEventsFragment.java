package com.example.eventhunter.ui.profile.collaborator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventhunter.databinding.FragmentCollaboratorFutureEventsBinding;
import com.example.eventhunter.ui.mainPage.events.eventCard.EventCard;
import com.example.eventhunter.ui.mainPage.events.eventCard.EventCardAdapter;

public class CollaboratorFutureEventsFragment extends Fragment {

    private CollaboratorProfileViewModel viewModel;
    private FragmentCollaboratorFutureEventsBinding binding;

    public CollaboratorFutureEventsFragment() {
    }

    public static CollaboratorFutureEventsFragment newInstance() {
        return new CollaboratorFutureEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCollaboratorFutureEventsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(CollaboratorProfileViewModel.class);

        RecyclerView futureEventsRecyclerView = binding.futureEventsRecyclerView;
        EventCard[] events = {new EventCard("Event1", "Organizer1", "12/03/2021", "Location1", 14), new EventCard("Event2", "Organizer2", "17/05/2021", "Location2", 57), new EventCard("Event3", "Organizer3", "31/07/2021", "Location3", 100)};
        futureEventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        futureEventsRecyclerView.setAdapter(new EventCardAdapter(events));

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