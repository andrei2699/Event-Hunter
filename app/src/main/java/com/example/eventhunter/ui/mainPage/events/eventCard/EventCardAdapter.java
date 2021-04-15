package com.example.eventhunter.ui.mainPage.events.eventCard;

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

public class EventCardAdapter extends RecyclerView.Adapter<EventCardAdapter.ViewHolder> {
    private final EventCard[] eventCards;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventNameTextView;
        private final TextView organizerNameTextView;
        private final TextView eventDateTextView;
        private final TextView eventLocationTextView;
        private final TextView eventSeatNumberTextView;
        private final ImageView eventImageView;
        private final Button detailsButton;
        private final Button reserveButton;

        public ViewHolder(View view) {
            super(view);
            eventNameTextView = view.findViewById(R.id.eventNameEventDetailsCard);
            organizerNameTextView = view.findViewById(R.id.eventOrganizerEventDetailsCard);
            eventDateTextView = view.findViewById(R.id.realDateEventDetailsCard);
            eventLocationTextView = view.findViewById(R.id.realLocationEventDetailsCard);
            eventSeatNumberTextView = view.findViewById(R.id.realSeatNumberEventDetailsCard);
            eventImageView = view.findViewById(R.id.eventImageEventDetailsCard);
            reserveButton = view.findViewById(R.id.reserveButtonEventDetailsCard);
            detailsButton = view.findViewById(R.id.eventDetailsButtonEventDetailsCard);
        }

        public TextView getEventNameTextView() {
            return eventNameTextView;
        }

        public TextView getOrganizerNameTextView() {
            return organizerNameTextView;
        }

        public TextView getEventDateTextView() {
            return eventDateTextView;
        }

        public TextView getEventLocationTextView() {
            return eventLocationTextView;
        }

        public TextView getEventSeatNumberTextView() {
            return eventSeatNumberTextView;
        }

        public ImageView getEventImageView() {
            return eventImageView;
        }

        public Button getDetailsButton() {
            return detailsButton;
        }

        public Button getReserveButton() {
            return reserveButton;
        }
    }

    public EventCardAdapter(EventCard[] dataSet) {
        eventCards = dataSet;
    }

    @NonNull
    @Override
    public EventCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_details_card, viewGroup, false);

        return new EventCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventCardAdapter.ViewHolder viewHolder, int position) {
        viewHolder.getEventNameTextView().setText(eventCards[position].eventName);
        viewHolder.getOrganizerNameTextView().setText(eventCards[position].organizerName);
        viewHolder.getEventDateTextView().setText(eventCards[position].eventDate);
        viewHolder.getEventLocationTextView().setText(eventCards[position].eventLocation);
        viewHolder.getEventSeatNumberTextView().setText(eventCards[position].availableSeatsNumber + "");

        Drawable image = AppCompatResources.getDrawable(viewHolder.itemView.getContext(), R.drawable.image_unavailable);
        if (eventCards[position].eventImage != null) {
            image = eventCards[position].eventImage;
        }
        viewHolder.getEventImageView().setImageDrawable(image);
    }

    @Override
    public int getItemCount() {
        return eventCards.length;
    }
}