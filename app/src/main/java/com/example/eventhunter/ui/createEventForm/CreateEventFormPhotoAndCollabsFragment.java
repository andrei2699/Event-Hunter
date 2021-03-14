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

public class CreateEventFormPhotoAndCollabsFragment extends Fragment {

    private BasicEventFormViewModel mViewModel;

    public static CreateEventFormPhotoAndCollabsFragment newInstance() {
        return new CreateEventFormPhotoAndCollabsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_create_event_form_phot_and_collabs, container, false);

        String eventType = "";
        if (getArguments() != null) {
            eventType = CreateEventFormPhotoAndCollabsFragmentArgs.fromBundle(getArguments()).getEventType();
        }
        String finalEventType = eventType;

        root.findViewById(R.id.eventPhotoAndCollabPreviousButton).setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigateBackToBasicInfo);
        });

        root.findViewById(R.id.eventPhotoAndCollabNextButton).setOnClickListener(view -> {
            String[] stringArray = getResources().getStringArray(R.array.event_types_array);
            if (stringArray[0].equals(finalEventType)) {
                Navigation.findNavController(view).navigate(R.id.navigateToOneTimeEvent);
            } else if (stringArray[1].equals(finalEventType)) {
                Navigation.findNavController(view).navigate(R.id.navigateToRepeatableEvent);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BasicEventFormViewModel.class);
        // TODO: Use the ViewModel
    }
}