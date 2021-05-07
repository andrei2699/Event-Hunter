package com.example.eventhunter.ui.mainPage.collaborators;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.databinding.FragmentHomeCollaboratorsBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.profile.service.CollaboratorProfileService;
import com.example.eventhunter.ui.mainPage.collaborators.collaboratorCard.CollaboratorCard;
import com.example.eventhunter.ui.mainPage.collaborators.collaboratorCard.CollaboratorCardAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainPageCollaboratorsFragment extends Fragment {

    @Injectable
    private CollaboratorProfileService collaboratorProfileService;

    private MainPageCollaboratorsViewModel mainPageCollaboratorsViewModel;
    private FragmentHomeCollaboratorsBinding binding;

    public MainPageCollaboratorsFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static MainPageCollaboratorsFragment newInstance() {
        return new MainPageCollaboratorsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeCollaboratorsBinding.inflate(inflater, container, false);
        mainPageCollaboratorsViewModel = new ViewModelProvider(requireActivity()).get(MainPageCollaboratorsViewModel.class);

        RecyclerView collaboratorsRecycleView = binding.homeCollaboratorsRecycleView;

        CollaboratorCardAdapter collaboratorCardAdapter = new CollaboratorCardAdapter(this);

        collaboratorProfileService.getAllCollaboratorProfiles(collaboratorCardAdapter::updateDataSource);

        collaboratorsRecycleView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        collaboratorsRecycleView.setAdapter(collaboratorCardAdapter);

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