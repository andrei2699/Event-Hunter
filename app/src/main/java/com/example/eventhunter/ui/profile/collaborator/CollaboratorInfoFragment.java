package com.example.eventhunter.ui.profile.collaborator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.R;
import com.example.eventhunter.databinding.FragmentCollaboratorInfoBinding;
import com.example.eventhunter.databinding.FragmentCollaboratorPastEventsBinding;
import com.example.eventhunter.ui.mainPage.events.eventCard.EventCard;
import com.example.eventhunter.ui.mainPage.events.eventCard.EventCardAdapter;

public class CollaboratorInfoFragment extends Fragment {

    private CollaboratorProfileViewModel viewModel;
    private FragmentCollaboratorInfoBinding binding;

    public CollaboratorInfoFragment() {
    }

    public static CollaboratorInfoFragment newInstance() {
        return new CollaboratorInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCollaboratorInfoBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(CollaboratorProfileViewModel.class);

        viewModel.getCollaboratorAddress().observe(getViewLifecycleOwner(), binding.collaboratorAddressTextView::setText);
        viewModel.getCollaboratorEmail().observe(getViewLifecycleOwner(), binding.collaboratorEmailTextView::setText);
        viewModel.getCollaboratorPhoneNumber().observe(getViewLifecycleOwner(), binding.collaboratorPhoneNumberTextView::setText);

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}