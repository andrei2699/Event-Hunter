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
        viewHolder.getLocationTextView().setText(reservations[position].eventLocation);
        viewHolder.getDateTextView().setText(reservations[position].eventDate);
        viewHolder.getStartHourTextView().setText(reservations[position].eventStartHour);
        viewHolder.getSeatNumberTextView().setText(reservations[position].reservedSeats + "");
        viewHolder.getTotalPriceTextView().setText(reservations[position].ticketPrice * reservations[position].reservedSeats + "");
        Drawable image = AppCompatResources.getDrawable(viewHolder.itemView.getContext(), R.drawable.ic_baseline_account_circle_24);
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