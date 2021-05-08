package com.example.eventhunter.profile.regularUser;

import android.graphics.Bitmap;
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
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.profile.service.RegularUserProfileService;
import com.example.eventhunter.reservation.ReservationDetailsCard;
import com.example.eventhunter.reservation.ReservationDetailsCardAdapter;

import java.util.List;
import java.util.stream.Collectors;

public class RegularUserFragment extends Fragment {

    private RegularUserViewModel mViewModel;
    private RegularUserFragmentBinding binding;

    @Injectable
    private RegularUserProfileService regularUserProfileService;

    public RegularUserFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static RegularUserFragment newInstance() {
        return new RegularUserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RegularUserFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(RegularUserViewModel.class);

        mViewModel.getRegularUserName().observe(getViewLifecycleOwner(), name -> {
            binding.regularUserNameProfilePage.setText(name);
        });
        mViewModel.getRegularUserEmail().observe(getViewLifecycleOwner(), email -> {
            binding.regularUserEmailProfilePage.setText(email);
        });


        String regularUserId = getArguments() != null ? getArguments().getString("regularUserId") : null;
        if (regularUserId != null && !regularUserId.isEmpty()) {
            this.regularUserProfileService.getRegularUserProfileById(regularUserId, regularUserModel -> {
                mViewModel.setRegularUserName(regularUserModel.name);
                mViewModel.setRegularUserEmail(regularUserModel.email);
                mViewModel.setReservations(regularUserModel.reservations.stream().map(reservationModel ->
                        new ReservationDetailsCard(reservationModel.eventName, reservationModel.eventPhoto, reservationModel.eventLocation,
                                reservationModel.eventStartDate, reservationModel.eventStartHour, reservationModel.reservedSeatsNumber, reservationModel.ticketPrice))
                        .collect(Collectors.toList()));
            });

            // todo get Profile and update model
        }

        ReservationDetailsCardAdapter reservationDetailsCardAdapter = new ReservationDetailsCardAdapter();
        mViewModel.getReservations().observe(getViewLifecycleOwner(), reservationDetailsCardAdapter::updateDataSource);

        RecyclerView reservationsRecycleView = binding.reservationsRecycleViewRegUserProfilePage;
        reservationsRecycleView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        reservationsRecycleView.setAdapter(reservationDetailsCardAdapter);

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