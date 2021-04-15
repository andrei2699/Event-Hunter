package com.example.eventhunter.ui.mainpage.collaborators;

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

import com.example.eventhunter.databinding.FragmentHomeCollaboratorsBinding;
import com.example.eventhunter.ui.mainpage.collaborators.collaboratorCard.CollaboratorCard;
import com.example.eventhunter.ui.mainpage.collaborators.collaboratorCard.CollaboratorCardAdapter;

public class MainPageCollaboratorsFragment extends Fragment {

    private MainPageCollaboratorsViewModel mainPageCollaboratorsViewModel;
    private FragmentHomeCollaboratorsBinding binding;

    public static MainPageCollaboratorsFragment newInstance() {
        return new MainPageCollaboratorsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeCollaboratorsBinding.inflate(inflater, container, false);
        mainPageCollaboratorsViewModel = new ViewModelProvider(requireActivity()).get(MainPageCollaboratorsViewModel.class);

        RecyclerView collaboratorsRecycleView = binding.homeCollaboratorsRecycleView;
        CollaboratorCard[] collaborators = {new CollaboratorCard("Name1", "name1@example.com"), new CollaboratorCard("Name2", "name2@example.com"), new CollaboratorCard("Name3", "name3@example.com")};

        collaboratorsRecycleView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        collaboratorsRecycleView.setAdapter(new CollaboratorCardAdapter(collaborators));

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