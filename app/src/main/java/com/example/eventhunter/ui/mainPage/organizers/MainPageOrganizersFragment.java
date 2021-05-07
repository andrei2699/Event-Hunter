package com.example.eventhunter.ui.mainPage.organizers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.databinding.FragmentHomeOrganizersBinding;
import com.example.eventhunter.ui.mainPage.organizers.organizerCard.OrganizerCard;
import com.example.eventhunter.ui.mainPage.organizers.organizerCard.OrganizerCardAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainPageOrganizersFragment extends Fragment {

    private MainPageOrganizersViewModel mainPageOrganizersViewModel;
    private FragmentHomeOrganizersBinding binding;

    public static MainPageOrganizersFragment newInstance() {
        return new MainPageOrganizersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeOrganizersBinding.inflate(inflater, container, false);
        mainPageOrganizersViewModel = new ViewModelProvider(requireActivity()).get(MainPageOrganizersViewModel.class);

        RecyclerView organizersRecycleView = binding.homeOrganizersRecyclerView;
        List<OrganizerCard> organizers = new ArrayList<>();
        organizers.add(new OrganizerCard("Id1", "Name1", "name1@example.com", "Music Events"));
        organizers.add(new OrganizerCard("Id2", "Name2", "name2@example.com", "Literature Events"));
        organizers.add(new OrganizerCard("Id3", "Name3", "name3@example.com", "Sportive Events"));


        organizersRecycleView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        organizersRecycleView.setAdapter(new OrganizerCardAdapter(this, organizers));

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