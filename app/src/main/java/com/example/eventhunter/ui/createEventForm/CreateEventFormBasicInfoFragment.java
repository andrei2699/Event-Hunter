package com.example.eventhunter.ui.createEventForm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.eventhunter.R;
import com.example.eventhunter.ui.gallery.GalleryViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class CreateEventFormBasicInfoFragment extends Fragment {

    private EventFormViewModel mViewModel;

    public static CreateEventFormBasicInfoFragment newInstance() {
        return new CreateEventFormBasicInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(EventFormViewModel.class);

        View root = inflater.inflate(R.layout.create_event_form_basic_info_fragment, container, false);

        Spinner spinner = root.findViewById(R.id.spinnerEventType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.event_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final EditText eventNameEditText = root.findViewById(R.id.editTextEventName);
        mViewModel.getEventName().observe(getViewLifecycleOwner(), eventNameEditText::setText);

        final EditText eventDescriptionEditText = root.findViewById(R.id.editTextEventDescription);
        mViewModel.getEventDescription().observe(getViewLifecycleOwner(), eventDescriptionEditText::setText);

        final EditText eventSeatNumberEditText = root.findViewById(R.id.editTextEventSeatNumber);
        mViewModel.getEventSeatNumber().observe(getViewLifecycleOwner(), eventSeatNumberEditText::setText);

        final EditText eventLocationEditText = root.findViewById(R.id.editTextEventLocation);
        mViewModel.getEventLocation().observe(getViewLifecycleOwner(), eventLocationEditText::setText);

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
                spinner.setSelection(index);
            }
        });

        root.findViewById(R.id.createEventFormBasicInfoNextButton).setOnClickListener(view -> {

            mViewModel.setEventName(eventNameEditText.getText().toString());
            mViewModel.setEventDescription(eventDescriptionEditText.getText().toString());
            mViewModel.setEventSeatNumber(eventSeatNumberEditText.getText().toString());
            mViewModel.setEventLocation(eventLocationEditText.getText().toString());
            mViewModel.setEventType(spinner.getSelectedItem().toString());

            Navigation.findNavController(view).navigate(CreateEventFormBasicInfoFragmentDirections.navigateToPhotoAndCollabs());
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}