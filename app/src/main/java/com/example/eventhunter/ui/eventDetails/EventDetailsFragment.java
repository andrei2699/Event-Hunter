package com.example.eventhunter.ui.eventDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.databinding.EventDetailsFragmentBinding;
import com.example.eventhunter.ui.collaboratorHeader.CollaboratorHeader;
import com.example.eventhunter.ui.collaboratorHeader.CollaboratorHeaderViewAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventDetailsFragment extends Fragment {

    private EventDetailsViewModel mViewModel;
    private EventDetailsFragmentBinding binding;

    public static EventDetailsFragment newInstance() {
        return new EventDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = EventDetailsFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(EventDetailsViewModel.class);

        RecyclerView collaboratorsRecyclerView = binding.collaboratorsRecycleViewEventDetails;
        CollaboratorHeader[] collaborators = {new CollaboratorHeader("Name1"), new CollaboratorHeader("Name 2")};

        collaboratorsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        collaboratorsRecyclerView.setAdapter(new CollaboratorHeaderViewAdapter(collaborators));

        mViewModel.getEventName().observe(getViewLifecycleOwner(), name -> {
            binding.eventNameEventDetailsPage.setText(name);
        });
        mViewModel.getEventOrganizerName().observe(getViewLifecycleOwner(), organizerName -> {
            binding.organizerNameEventDetails.setText(organizerName);
        });
        mViewModel.getEventLocation().observe(getViewLifecycleOwner(), location -> {
            binding.eventLocationEventDetails.setText(location);
        });
        mViewModel.getEventDate().observe(getViewLifecycleOwner(), date -> {
            binding.eventDateEventDetails.setText(date);
        });
        mViewModel.getEventStartHour().observe(getViewLifecycleOwner(), startHour -> {
            binding.eventStartHourEventDetailsPage.setText(startHour);
        });
        mViewModel.getEventEndHour().observe(getViewLifecycleOwner(), endHour -> {
            binding.eventEndHourEventDetailsPage.setText(endHour);
        });
        mViewModel.getEventSeatNumber().observe(getViewLifecycleOwner(), seatNumber -> {
            binding.eventSeatNumberEventDetailsPage.setText(seatNumber);
        });
        mViewModel.getEventDescription().observe(getViewLifecycleOwner(), description -> {
            binding.eventDescriptionEventDetailsPage.setText(description);
        });
        mViewModel.getEventType().observe(getViewLifecycleOwner(), type -> {
            binding.eventTypeEventDetailsPage.setText(type);
        });

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