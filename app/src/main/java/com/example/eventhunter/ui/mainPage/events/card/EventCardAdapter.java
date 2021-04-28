package com.example.eventhunter.ui.mainPage.events.card;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventhunter.R;
import com.example.eventhunter.events.models.EventCard;
import com.example.eventhunter.ui.reservationDetailsCard.reservationCardPopup.ReservationCardDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class EventCardAdapter extends RecyclerView.Adapter<EventCardAdapter.ViewHolder> {

    private static final int EVENT_RESERVATION_DIALOG_REQUEST_CODE = 100;

    private final List<EventCard> eventCards = new ArrayList<>();

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

    public EventCardAdapter(Fragment fragment) {

        this.onReserveButtonClick = eventCard -> {
            ReservationCardDialogFragment reservationCardDialogFragment = ReservationCardDialogFragment.newInstance(eventCard, reservationCardDialogModel -> {
                // TODO save reservation to DB

                eventCard.removeAvailableSeats(reservationCardDialogModel.getChosenSeatsNumber());
                notifyDataSetChanged();
            });
            reservationCardDialogFragment.setTargetFragment(fragment, EVENT_RESERVATION_DIALOG_REQUEST_CODE);
            reservationCardDialogFragment.show(fragment.getParentFragmentManager(), "event_reservation_dialog");
        };

        this.onSeeDetailsButtonClick = eventCard -> {

        };
    }

    public void updateDataSource(EventCard eventCard) {
        this.eventCards.add(eventCard);
        notifyDataSetChanged();
    }

//    public void setOnReserveButtonClick(Consumer<EventCard> onReserveButtonClick) {
//        this.onReserveButtonClick = onReserveButtonClick;
//    }
//
//    public void setOnSeeDetailsButtonClick(Consumer<EventCard> onSeeDetailsButtonClick) {
//        this.onSeeDetailsButtonClick = onSeeDetailsButtonClick;
//    }

    @NonNull
    @Override
    public EventCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_details_card, viewGroup, false);

        return new EventCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventCardAdapter.ViewHolder viewHolder, int position) {
        EventCard eventCard = eventCards.get(position);

        viewHolder.eventNameTextView.setText(eventCard.getEventName());
        viewHolder.organizerNameTextView.setText(eventCard.getOrganizerName());
        viewHolder.eventDateTextView.setText(eventCard.getEventDate());
        viewHolder.eventLocationTextView.setText(eventCard.getEventLocation());
        viewHolder.ticketPriceTextView.setText(eventCard.getTicketPrice() + "");
        viewHolder.eventSeatNumberTextView.setText(eventCard.getAvailableSeatsNumber() + "");

        Bitmap eventImageBitmap = eventCard.getEventImage();
        if (eventImageBitmap != null) {
            viewHolder.eventImageView.setImageBitmap(eventImageBitmap);
        } else {
            Drawable image = AppCompatResources.getDrawable(viewHolder.itemView.getContext(), R.drawable.image_unavailable);
            viewHolder.eventImageView.setImageDrawable(image);
        }


        if (checkIfPastEvent(eventCard.getEventDate())) {
            viewHolder.reserveButton.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.reserveButton.setOnClickListener(view -> {
                if (onReserveButtonClick != null) {
                    this.onReserveButtonClick.accept(eventCard);
                }
            });
        }

        viewHolder.detailsButton.setOnClickListener(view -> {
            if (onSeeDetailsButtonClick != null) {
                this.onSeeDetailsButtonClick.accept(eventCard);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventCards.size();
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