package com.example.eventhunter.profile.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.eventhunter.R;
import com.example.eventhunter.databinding.EditOrganizerProfileFragmentBinding;

public class EditOrganizerProfileFragment extends Fragment {

    private OrganizerProfileViewModel mViewModel;
    private EditOrganizerProfileFragmentBinding binding;

    public static EditOrganizerProfileFragment newInstance() {
        return new EditOrganizerProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(OrganizerProfileViewModel.class);

        binding = EditOrganizerProfileFragmentBinding.inflate(inflater, container, false);

        binding.saveOrganizerProfileButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(EditOrganizerProfileFragmentDirections.navigateToOrganizerProfile());
        });

        binding.uploadOrganizerProfileImageFab.setOnClickListener(view -> {

        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.organizer_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerOrganizerType.setAdapter(adapter);

        mViewModel.getOrganizerType().observe(getViewLifecycleOwner(), s -> {
            int index = -1;

            String[] stringArray = getResources().getStringArray(R.array.organizer_types_array);
            for (int i = 0; i < stringArray.length; i++) {
                if (stringArray[i].equals(s)) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                binding.spinnerOrganizerType.setSelection(index);
            }
        });

        mViewModel.getOrganizerName().observe(getViewLifecycleOwner(), binding.organizerNameTextView::setText);
        mViewModel.getOrganizerAddress().observe(getViewLifecycleOwner(), binding.editTextTextOrganizerAddress::setText);
        mViewModel.getOrganizerPhoneNumber().observe(getViewLifecycleOwner(), binding.editTextOrganizerPhoneNumber::setText);

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