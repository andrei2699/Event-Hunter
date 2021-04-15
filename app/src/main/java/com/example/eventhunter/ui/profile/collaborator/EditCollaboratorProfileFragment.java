package com.example.eventhunter.ui.profile.collaborator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.eventhunter.databinding.EditCollaboratorProfileFragmentBinding;

public class EditCollaboratorProfileFragment extends Fragment {

    private CollaboratorProfileViewModel mViewModel;
    private EditCollaboratorProfileFragmentBinding binding;

    public static EditCollaboratorProfileFragment newInstance() {
        return new EditCollaboratorProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(CollaboratorProfileViewModel.class);

        binding = EditCollaboratorProfileFragmentBinding.inflate(inflater, container, false);

        binding.saveCollaboratorProfileButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(EditCollaboratorProfileFragmentDirections.navigateToCollaboratorProfileFragment());
        });

        binding.uploadCollaboratorProfileImageFab.setOnClickListener(view -> {

        });

        mViewModel.getCollaboratorName().observe(getViewLifecycleOwner(), binding.collaboratorNameTextView::setText);
        mViewModel.getCollaboratorPhoneNumber().observe(getViewLifecycleOwner(), binding.editTextCollaboratorPhoneNumber::setText);
        mViewModel.getCollaboratorAddress().observe(getViewLifecycleOwner(), binding.editTextTextCollaboratorAddress::setText);

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
