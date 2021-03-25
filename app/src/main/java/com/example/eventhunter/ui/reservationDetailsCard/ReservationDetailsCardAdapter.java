package com.example.eventhunter.ui.reservationDetailsCard;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventhunter.R;

public class ReservationDetailsCardAdapter extends RecyclerView.Adapter<ReservationDetailsCardAdapter.ViewHolder> {

    private final ReservationDetailsCard[] reservations;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventNameTextView;
        private final ImageView eventImageView;
        private final TextView seatNumberTextView;
        private final TextView totalPriceTextView;
        private final Button downloadReservationButton;

        public ViewHolder(View view) {
            super(view);

            eventNameTextView = view.findViewById(R.id.eventNameReservationCard);
            eventImageView = view.findViewById(R.id.eventImageReservationCard);
            seatNumberTextView = view.findViewById(R.id.seatNumberReservationTextView);
            totalPriceTextView = view.findViewById(R.id.totalPriceReservationTextView);
            downloadReservationButton = view.findViewById(R.id.downloadButtonReservationCard);
        }

        public TextView getEventNameTextView() {
            return eventNameTextView;
        }

        public ImageView getEventImageView() {
            return eventImageView;
        }

        public Button getDownloadReservationButton() {
            return downloadReservationButton;
        }

        public TextView getSeatNumberTextView() {
            return seatNumberTextView;
        }

        public TextView getTotalPriceTextView() {
            return totalPriceTextView;
        }
    }

    public ReservationDetailsCardAdapter(ReservationDetailsCard[] dataSet) {
        reservations = dataSet;
    }

    @NonNull
    @Override
    public ReservationDetailsCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reservation_card, viewGroup, false);

        return new ReservationDetailsCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.getEventNameTextView().setText(reservations[position].eventName);
        viewHolder.getSeatNumberTextView().setText(reservations[position].reservedSeatsNumber + "");
        viewHolder.getTotalPriceTextView().setText(reservations[position].totalPrice + "");
        Drawable image = AppCompatResources.getDrawable(viewHolder.itemView.getContext(), R.drawable.image_unavailable);
        if (reservations[position].eventImage != null) {
            image = reservations[position].eventImage;
        }
        viewHolder.getEventImageView().setImageDrawable(image);
    }

    @Override
    public int getItemCount() {
        return reservations.length;
    }
}