package com.example.eventhunter.ui.reservationDetailsCard;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventhunter.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

public class ReservationDetailsCardAdapter extends RecyclerView.Adapter<ReservationDetailsCardAdapter.ViewHolder> {

    private final List<ReservationDetailsCard> reservations;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventNameTextView;
        private final ImageView eventImageView;
        private final TextView locationTextView;
        private final TextView dateTextView;
        private final TextView startHourTextView;
        private final TextView seatNumberTextView;
        private final TextView ticketPriceTextView;
        private final TextView totalPriceTextView;
        private final Button downloadReservationButton;

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
            downloadReservationButton = view.findViewById(R.id.dowloadButtonReservationCard);
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

        public TextView getLocationTextView() {
            return locationTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }

        public TextView getStartHourTextView() {
            return startHourTextView;
        }

        public TextView getSeatNumberTextView() {
            return seatNumberTextView;
        }

        public TextView getTotalPriceTextView() {
            return totalPriceTextView;
        }

        public TextView getTicketPriceTextView() {
            return ticketPriceTextView;
        }
    }

    public ReservationDetailsCardAdapter(List<ReservationDetailsCard> dataSet) {
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
        ReservationDetailsCard reservation = reservations.get(position);

        viewHolder.getEventNameTextView().setText(reservation.eventName);
        viewHolder.getLocationTextView().setText(reservation.eventLocation);
        viewHolder.getDateTextView().setText(reservation.eventDate);
        viewHolder.getStartHourTextView().setText(reservation.eventStartHour);
        viewHolder.getSeatNumberTextView().setText(reservation.reservedSeats + "");
        viewHolder.getTotalPriceTextView().setText(reservation.ticketPrice * reservation.reservedSeats + "");
        Drawable image = AppCompatResources.getDrawable(viewHolder.itemView.getContext(), R.drawable.ic_baseline_account_circle_24);
        if (reservation.eventImage != null) {
            image = reservation.eventImage;
        }
        viewHolder.getEventImageView().setImageDrawable(image);
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }
}