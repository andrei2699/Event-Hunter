package com.example.eventhunter.ui.createEventForm;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.eventhunter.R;

public class CreateEventFormBasicInfoFragment extends Fragment {

    private BasicEventFormViewModel mViewModel;

    public static CreateEventFormBasicInfoFragment newInstance() {
        return new CreateEventFormBasicInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.create_event_form_basic_info_fragment, container, false);

        Spinner spinner = root.findViewById(R.id.spinnerEventType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.event_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        root.findViewById(R.id.createEventFormBasicInfoNextButton).setOnClickListener(view -> {
            NavDirections action = CreateEventFormBasicInfoFragmentDirections.navigateToPhotoAndCollabs(spinner.getSelectedItem().toString());
            Navigation.findNavController(view).navigate(action);
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