package com.example.eventhunter.ui.createEventForm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.eventhunter.R;
import com.example.eventhunter.databinding.CreateEventFormBasicInfoFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class CreateEventFormBasicInfoFragment extends Fragment {

    private EventFormViewModel mViewModel;
    private CreateEventFormBasicInfoFragmentBinding binding;

    public static CreateEventFormBasicInfoFragment newInstance() {
        return new CreateEventFormBasicInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(EventFormViewModel.class);

        binding = CreateEventFormBasicInfoFragmentBinding.inflate(inflater, container, false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.event_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerEventType.setAdapter(adapter);

        mViewModel.getEventName().observe(getViewLifecycleOwner(), binding.editTextEventName::setText);
        mViewModel.getEventDescription().observe(getViewLifecycleOwner(), binding.editTextEventDescription::setText);
        mViewModel.getEventSeatNumber().observe(getViewLifecycleOwner(), binding.editTextEventSeatNumber::setText);
        mViewModel.getEventLocation().observe(getViewLifecycleOwner(), binding.editTextEventLocation::setText);

        mViewModel.getEventType().observe(getViewLifecycleOwner(), s -> {
            int index = -1;

            String[] stringArray = getResources().getStringArray(R.array.event_types_array);
            for (int i = 0; i < stringArray.length; i++) {
                if (stringArray[i].equals(s)) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                binding.spinnerEventType.setSelection(index);
            }
        });

        binding.createEventFormBasicInfoNextButton.setOnClickListener(view -> {

            mViewModel.setEventName(binding.editTextEventName.getText().toString());
            mViewModel.setEventDescription(binding.editTextEventDescription.getText().toString());
            mViewModel.setEventSeatNumber(binding.editTextEventSeatNumber.getText().toString());
            mViewModel.setEventLocation(binding.editTextEventLocation.getText().toString());
            mViewModel.setEventType(binding.spinnerEventType.getSelectedItem().toString());

            Navigation.findNavController(view).navigate(CreateEventFormBasicInfoFragmentDirections.navigateToPhotoAndCollabs());
        });

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