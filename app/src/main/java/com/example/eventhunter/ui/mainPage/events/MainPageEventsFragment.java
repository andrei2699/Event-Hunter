package com.example.eventhunter.ui.mainPage.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventhunter.databinding.FragmentHomeEventsBinding;
import com.example.eventhunter.ui.mainPage.events.eventCard.EventCard;
import com.example.eventhunter.ui.mainPage.events.eventCard.EventCardAdapter;

public class MainPageEventsFragment extends Fragment {

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
        EventCard[] events = {new EventCard("Event1","Organizer1","12/03/2021","Location1",14), new EventCard("Event2","Organizer2","17/05/2021","Location2",57), new EventCard("Event3","Organizer3","31/07/2021","Location3",100)};
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        eventsRecyclerView.setAdapter(new EventCardAdapter(events));

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