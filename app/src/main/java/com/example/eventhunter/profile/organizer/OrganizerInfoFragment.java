package com.example.eventhunter.profile.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventhunter.databinding.FragmentOrganizerInfoBinding;

public class OrganizerInfoFragment extends Fragment {
    private OrganizerProfileViewModel viewModel;
    private FragmentOrganizerInfoBinding binding;

    public OrganizerInfoFragment() {
    }

    public static OrganizerInfoFragment newInstance() {
        return new OrganizerInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrganizerInfoBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(OrganizerProfileViewModel.class);

        viewModel.getOrganizerAddress().observe(getViewLifecycleOwner(), binding.organizerAddressTextView::setText);
        viewModel.getOrganizerPhoneNumber().observe(getViewLifecycleOwner(), binding.organizerPhoneNumberTextView::setText);
        viewModel.getOrganizerNumberOfOrganizedEvents().observe(getViewLifecycleOwner(), binding.numberOfOrganizedEventsTextView::setText);
        viewModel.getOrganizerType().observe(getViewLifecycleOwner(), binding.organizerTypeTextView::setText);
        viewModel.getOrganizerEmail().observe(getViewLifecycleOwner(), binding.organizerEmailTextView::setText);

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
