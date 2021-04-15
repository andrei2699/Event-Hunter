package com.example.eventhunter.ui.mainPage.organizers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventhunter.databinding.FragmentHomeOrganizersBinding;
import com.example.eventhunter.ui.mainPage.organizers.organizerCard.OrganizerCard;
import com.example.eventhunter.ui.mainPage.organizers.organizerCard.OrganizerCardAdapter;

public class MainPageOrganizersFragment extends Fragment {

    private MainPageOrganizersViewModel mainPageOrganizersViewModel;
    private FragmentHomeOrganizersBinding binding;

    public static MainPageOrganizersFragment newInstance() {
        return new MainPageOrganizersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeOrganizersBinding.inflate(inflater, container, false);
        mainPageOrganizersViewModel = new ViewModelProvider(requireActivity()).get(MainPageOrganizersViewModel.class);

        RecyclerView organizersRecycleView = binding.homeOrganizersRecyclerView;
        OrganizerCard[] organizers = {new OrganizerCard("Name1", "name1@example.com","Music Events"), new OrganizerCard("Name2", "name2@example.com", "Literature Events"), new OrganizerCard("Name3", "name3@example.com", "Sportive Events")};

        organizersRecycleView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        organizersRecycleView.setAdapter(new OrganizerCardAdapter(organizers));

        View view = binding.getRoot();
        return view;
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