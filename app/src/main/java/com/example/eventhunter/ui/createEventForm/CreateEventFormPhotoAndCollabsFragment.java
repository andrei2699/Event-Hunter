package com.example.eventhunter.ui.createEventForm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.R;

import java.util.concurrent.atomic.AtomicReference;

public class CreateEventFormPhotoAndCollabsFragment extends Fragment {

    private EventFormViewModel mViewModel;

    public static CreateEventFormPhotoAndCollabsFragment newInstance() {
        return new CreateEventFormPhotoAndCollabsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(EventFormViewModel.class);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_create_event_form_phot_and_collabs, container, false);

        AtomicReference<String> eventType = new AtomicReference<>("");

        mViewModel.getEventType().observe(getViewLifecycleOwner(), s -> {

            String[] stringArray = getResources().getStringArray(R.array.event_types_array);
            for (String value : stringArray) {
                if (value.equals(s)) {
                    eventType.set(value);
                    break;
                }
            }
        });


        root.findViewById(R.id.eventPhotoAndCollabPreviousButton).setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigateBackToBasicInfo);
        });

        root.findViewById(R.id.eventPhotoAndCollabNextButton).setOnClickListener(view -> {
            String[] stringArray = getResources().getStringArray(R.array.event_types_array);
            if (stringArray[0].equals(eventType.get())) {
                Navigation.findNavController(view).navigate(R.id.navigateToOneTimeEvent);
            } else if (stringArray[1].equals(eventType.get())) {
                Navigation.findNavController(view).navigate(R.id.navigateToRepeatableEvent);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}