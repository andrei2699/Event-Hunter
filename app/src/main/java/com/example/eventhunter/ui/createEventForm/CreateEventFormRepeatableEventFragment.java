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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventFormRepeatableEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventFormRepeatableEventFragment extends Fragment {

    private BasicEventFormViewModel mViewModel;

    public static CreateEventFormRepeatableEventFragment newInstance() {
        return new CreateEventFormRepeatableEventFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_create_event_form_repeatable_event, container, false);

        root.findViewById(R.id.repeatableEventPreviousButton).setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigateBackToPhotoAndCollabsFromRepeatableEvent);
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