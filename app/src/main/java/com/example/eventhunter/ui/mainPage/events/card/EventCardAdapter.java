package com.example.eventhunter.ui.mainPage.events.card;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Consumer;

public class EventCardAdapter extends RecyclerView.Adapter<EventCardAdapter.ViewHolder> {
    private final EventCard[] eventCards;
    private Consumer<EventCard> onReserveButtonClick;
    private Consumer<EventCard> onSeeDetailsButtonClick;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView eventNameTextView;
        public final TextView organizerNameTextView;
        public final TextView eventDateTextView;
        public final TextView eventLocationTextView;
        public final TextView eventSeatNumberTextView;
        public final TextView ticketPriceTextView;
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
            ticketPriceTextView = view.findViewById(R.id.realTicketPriceEventDetailsCard);
            eventImageView = view.findViewById(R.id.eventImageEventDetailsCard);
            reserveButton = view.findViewById(R.id.reserveButtonEventDetailsCard);
            detailsButton = view.findViewById(R.id.eventDetailsButtonEventDetailsCard);
        }
    }

    public EventCardAdapter(EventCard[] dataSet) {
        eventCards = dataSet;
    }

    public void setOnReserveButtonClick(Consumer<EventCard> onReserveButtonClick) {
        this.onReserveButtonClick = onReserveButtonClick;
    }

    public void setOnSeeDetailsButtonClick(Consumer<EventCard> onSeeDetailsButtonClick) {
        this.onSeeDetailsButtonClick = onSeeDetailsButtonClick;
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
        viewHolder.ticketPriceTextView.setText(eventCards[position].ticketPrice + "");
        viewHolder.eventSeatNumberTextView.setText(eventCards[position].availableSeatsNumber + "");

        Drawable image = AppCompatResources.getDrawable(viewHolder.itemView.getContext(), R.drawable.image_unavailable);
        if (eventCards[position].eventImage != null) {
            image = eventCards[position].eventImage;
        }
        viewHolder.eventImageView.setImageDrawable(image);


        if (checkIfPastEvent(eventCards[position].eventDate)) {
            viewHolder.reserveButton.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.reserveButton.setOnClickListener(view -> {
                if (onReserveButtonClick != null) {
                    this.onReserveButtonClick.accept(eventCards[position]);
                }
            });
        }

        viewHolder.detailsButton.setOnClickListener(view -> {
            if (onSeeDetailsButtonClick != null) {
                this.onReserveButtonClick.accept(eventCards[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventCards.length;
    }

    public boolean checkIfPastEvent(String eventDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        String[] partsEventDate = eventDate.split("/");
        String[] partsCurrentDate = currentDate.split("/");

        int eventYear = Integer.parseInt(partsEventDate[2]);
        int currentYear = Integer.parseInt(partsCurrentDate[2]);
        int eventMonth = Integer.parseInt(partsEventDate[1]);
        int currentMonth = Integer.parseInt(partsCurrentDate[1]);
        int eventDay = Integer.parseInt(partsEventDate[0]);
        int currentDay = Integer.parseInt(partsCurrentDate[0]);

        if (eventYear < currentYear)
            return true;
        if ((eventYear == currentYear) && (eventMonth < currentMonth))
            return true;
        return (eventYear == currentYear) && (eventMonth == currentMonth) && (eventDay < currentDay);
    }
}