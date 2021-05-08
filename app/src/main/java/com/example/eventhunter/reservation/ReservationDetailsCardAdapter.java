package com.example.eventhunter.reservation;

import android.graphics.Bitmap;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ReservationDetailsCardAdapter extends RecyclerView.Adapter<ReservationDetailsCardAdapter.ViewHolder> {

    private List<ReservationDetailsCard> reservations = new ArrayList<>();

    private final Consumer<ReservationDetailsCard> onCancelReservationButtonClick;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView eventNameTextView;
        public final ImageView eventImageView;
        public final TextView locationTextView;
        public final TextView dateTextView;
        public final TextView startHourTextView;
        public final TextView seatNumberTextView;
        public final TextView ticketPriceTextView;
        public final TextView totalPriceTextView;
        public final Button downloadReservationButton;
        public final Button cancelReservationButton;

        public ViewHolder(View view) {
            super(view);

            eventNameTextView = view.findViewById(R.id.eventNameReservationCard);
            eventImageView = view.findViewById(R.id.eventImageReservationCard);
            locationTextView = view.findViewById(R.id.realLocationReservationCard);
            dateTextView = view.findViewById(R.id.realDateReservationCard);
            startHourTextView = view.findViewById(R.id.realStartHourReservationcard);
            seatNumberTextView = view.findViewById(R.id.realNumberOfSeatsReservationCard);
            ticketPriceTextView = view.findViewById(R.id.realTicketPriceReservationCard);
            totalPriceTextView = view.findViewById(R.id.realTotalPriceReservationCard);
            downloadReservationButton = view.findViewById(R.id.downloadButton);
            cancelReservationButton = view.findViewById(R.id.cancelReservationButton);
        }
    }

    public ReservationDetailsCardAdapter(Consumer<ReservationDetailsCard> onCancelReservationButtonClick) {
        this.onCancelReservationButtonClick = onCancelReservationButtonClick;
    }

    @NonNull
    @Override
    public ReservationDetailsCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reservation_card, viewGroup, false);

        return new ReservationDetailsCardAdapter.ViewHolder(view);
    }

    public void updateDataSource(List<ReservationDetailsCard> reservationDetailsCards) {
        reservations = reservationDetailsCards;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        ReservationDetailsCard reservationDetailsCard = reservations.get(position);

        viewHolder.eventNameTextView.setText(reservationDetailsCard.eventName);
        viewHolder.locationTextView.setText(reservationDetailsCard.eventLocation);
        viewHolder.dateTextView.setText(reservationDetailsCard.eventDate);
        viewHolder.startHourTextView.setText(reservationDetailsCard.eventStartHour);
        viewHolder.seatNumberTextView.setText(reservationDetailsCard.reservedSeats + "");
        viewHolder.totalPriceTextView.setText(reservationDetailsCard.ticketPrice * reservationDetailsCard.reservedSeats + "");
        viewHolder.ticketPriceTextView.setText(reservationDetailsCard.ticketPrice + "");

        Bitmap eventBitmap = reservationDetailsCard.eventImage;
        if (eventBitmap != null) {
            viewHolder.eventImageView.setImageBitmap(eventBitmap);
        } else {
            Drawable image = AppCompatResources.getDrawable(viewHolder.itemView.getContext(), R.drawable.photo_unavailable);
            viewHolder.eventImageView.setImageDrawable(image);
        }

        viewHolder.cancelReservationButton.setOnClickListener(view -> {
            onCancelReservationButtonClick.accept(reservationDetailsCard);
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }
}