package com.example.eventhunter.ui.profile.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.R;
import com.example.eventhunter.databinding.OrganizerProfileFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class OrganizerProfileFragment extends Fragment {

    private OrganizerProfileViewModel mViewModel;
    private OrganizerProfileFragmentBinding binding;

    public static OrganizerProfileFragment newInstance() {
        return new OrganizerProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(OrganizerProfileViewModel.class);
        binding = OrganizerProfileFragmentBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true);

        binding.createEventFabButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(OrganizerProfileFragmentDirections.navigateToCreateEventFormFragment());
        });

        mViewModel.getOrganizerName().observe(getViewLifecycleOwner(), binding.organizerNameTextView::setText);
        mViewModel.getOrganizerAddress().observe(getViewLifecycleOwner(), binding.organizerAddressTextView::setText);
        mViewModel.getOrganizerPhoneNumber().observe(getViewLifecycleOwner(), binding.organizerPhoneNumberTextView::setText);
        mViewModel.getOrganizerNumberOfOrganizedEvents().observe(getViewLifecycleOwner(), binding.numberOfOrganizedEventsTextView::setText);
        mViewModel.getOrganizerType().observe(getViewLifecycleOwner(), binding.organizerTypeTextView::setText);
        mViewModel.getOrganizerEmail().observe(getViewLifecycleOwner(), binding.organizerEmailTextView::setText);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.profile_edit_action) {
            Navigation.findNavController(binding.cardView).navigate(OrganizerProfileFragmentDirections.navigateToEditOrganizerProfileFragment());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}