package com.example.eventhunter.profile.regularUser;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.eventhunter.reservation.service.ReservationService;
import com.example.eventhunter.utils.DateVerifier;

import java.util.stream.Collectors;

public class RegularUserFragment extends Fragment {

    private RegularUserViewModel mViewModel;
    private RegularUserFragmentBinding binding;

    @Injectable
    private RegularUserProfileService regularUserProfileService;

    @Injectable
    private ReservationService reservationService;

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
                mViewModel.setReservations(regularUserModel.reservations.stream()
                        .filter(reservationModel -> DateVerifier.dateInTheFuture(reservationModel.eventStartDate))
                        .map(reservationModel -> new ReservationDetailsCard(reservationModel.reservationId, reservationModel.eventId, reservationModel.userId, reservationModel.eventName,
                                reservationModel.eventPhoto, reservationModel.eventLocation, reservationModel.eventStartDate, reservationModel.eventStartHour,
                                reservationModel.reservedSeatsNumber, reservationModel.ticketPrice))
                        .collect(Collectors.toList()));
            });
        }

        ReservationDetailsCardAdapter reservationDetailsCardAdapter = new ReservationDetailsCardAdapter(reservationDetailsCard -> {
            showConfirmationDialog(() -> {
                this.reservationService.cancelReservation(reservationDetailsCard.reservationId, reservationDetailsCard.eventId, reservationDetailsCard.userId, reservationDetailsCard.reservedSeats, consumer -> {
                    mViewModel.removeReservation(reservationDetailsCard);
                });
            });
        });

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

    private void showConfirmationDialog(Runnable runnable) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        runnable.run();
                        dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Delete reservation?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

}