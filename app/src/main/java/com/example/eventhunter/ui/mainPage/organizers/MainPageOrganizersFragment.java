package com.example.eventhunter.ui.mainPage.organizers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.databinding.FragmentHomeOrganizersBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.profile.service.OrganizerProfileService;
import com.example.eventhunter.ui.mainPage.organizers.organizerCard.OrganizerCardAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainPageOrganizersFragment extends Fragment {
    @Injectable
    private OrganizerProfileService organizerProfileService;

    private MainPageOrganizersViewModel mainPageOrganizersViewModel;
    private FragmentHomeOrganizersBinding binding;

    public MainPageOrganizersFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static MainPageOrganizersFragment newInstance() {
        return new MainPageOrganizersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeOrganizersBinding.inflate(inflater, container, false);
        mainPageOrganizersViewModel = new ViewModelProvider(requireActivity()).get(MainPageOrganizersViewModel.class);

        RecyclerView organizersRecycleView = binding.homeOrganizersRecyclerView;
        OrganizerCardAdapter organizerCardAdapter = new OrganizerCardAdapter(this);

        organizerProfileService.getAllOrganizersProfiles(organizerCardAdapter::updateDataSource);

        organizersRecycleView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        organizersRecycleView.setAdapter(organizerCardAdapter);

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