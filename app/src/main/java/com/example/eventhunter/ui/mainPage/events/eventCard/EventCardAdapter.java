package com.example.eventhunter.ui.mainPage.events.eventCard;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventhunter.R;

import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

public class EventCardAdapter extends RecyclerView.Adapter<EventCardAdapter.ViewHolder> {
    private final EventCard[] eventCards;
    private Consumer<EventCard> onReserveButtonClick;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView eventNameTextView;
        public final TextView organizerNameTextView;
        public final TextView eventDateTextView;
        public final TextView eventLocationTextView;
        public final TextView eventSeatNumberTextView;
        public final ImageView eventImageView;
        public final Button detailsButton;
        public final Button reserveButton;

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
    }

    public EventCardAdapter(EventCard[] dataSet, Consumer<EventCard> onReserveButtonClick) {
        this.onReserveButtonClick = onReserveButtonClick;
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
        viewHolder.eventNameTextView.setText(eventCards[position].eventName);
        viewHolder.organizerNameTextView.setText(eventCards[position].organizerName);
        viewHolder.eventDateTextView.setText(eventCards[position].eventDate);
        viewHolder.eventLocationTextView.setText(eventCards[position].eventLocation);
        viewHolder.eventSeatNumberTextView.setText(eventCards[position].availableSeatsNumber + "");

        Drawable image = AppCompatResources.getDrawable(viewHolder.itemView.getContext(), R.drawable.image_unavailable);
        if (eventCards[position].eventImage != null) {
            image = eventCards[position].eventImage;
        }
        viewHolder.eventImageView.setImageDrawable(image);

        viewHolder.reserveButton.setOnClickListener(view -> {
            if (onReserveButtonClick != null) {
                this.onReserveButtonClick.accept(eventCards[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventCards.length;
    }
}