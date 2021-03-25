package com.example.eventhunter.ui.regularUser;

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

import com.example.eventhunter.databinding.RegularUserFragmentBinding;
import com.example.eventhunter.ui.reservationDetailsCard.ReservationDetailsCard;
import com.example.eventhunter.ui.reservationDetailsCard.ReservationDetailsCardAdapter;

public class RegularUserFragment extends Fragment {

    private RegularUserViewModel mViewModel;
    private RegularUserFragmentBinding binding;

    public static RegularUserFragment newInstance() {
        return new RegularUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RegularUserFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(RegularUserViewModel.class);

        mViewModel.getRegularUserName().observe(getViewLifecycleOwner(), name -> {
            binding.regularUserNameProfilePage.setText(name);
        });
        mViewModel.getRegularUserEmail().observe(getViewLifecycleOwner(), email -> {
            binding.regularUserEmailProfilePage.setText(email);
        });

        RecyclerView reservationsRecycleView = binding.reservationsRecycleViewRegUserProfilePage;
        ReservationDetailsCard[] reservations = {new ReservationDetailsCard("Name1", 12, 32), new ReservationDetailsCard("Name2", 4, 320), new ReservationDetailsCard("Name3", 2, 80)};

        reservationsRecycleView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        reservationsRecycleView.setAdapter(new ReservationDetailsCardAdapter(reservations));

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