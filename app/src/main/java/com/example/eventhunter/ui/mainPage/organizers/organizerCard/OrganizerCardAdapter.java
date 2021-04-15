package com.example.eventhunter.ui.mainPage.organizers.organizerCard;

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

public class OrganizerCardAdapter extends RecyclerView.Adapter<OrganizerCardAdapter.ViewHolder> {
    private final OrganizerCard[] organizerCards;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView emailTextView;
        private final TextView eventTypeTextView;
        private final ImageView imageView;
        private final Button viewProfileButton;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.organizerNameOrganizerCard);
            emailTextView = view.findViewById(R.id.organizerEmailOrganizerCard);
            eventTypeTextView = view.findViewById(R.id.organizerTypeOrganizerCard);
            imageView = view.findViewById(R.id.organizerImageOrganizerCard);
            viewProfileButton = view.findViewById(R.id.viewProfileButtonOrganizerCard);
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public TextView getEmailTextView() {
            return emailTextView;
        }

        public TextView getEventTypeTextView() {
            return eventTypeTextView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public Button getViewProfileButton() {
            return viewProfileButton;
        }
    }

    public OrganizerCardAdapter(OrganizerCard[] dataSet) {
        organizerCards = dataSet;
    }

    @NonNull
    @Override
    public OrganizerCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.organizer_card, viewGroup, false);

        return new OrganizerCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizerCardAdapter.ViewHolder viewHolder, int position) {
        viewHolder.getNameTextView().setText(organizerCards[position].organizerName);
        viewHolder.getEmailTextView().setText(organizerCards[position].organizerEmail);
        viewHolder.getEventTypeTextView().setText(organizerCards[position].organizerEventsType);
        Drawable image = AppCompatResources.getDrawable(viewHolder.itemView.getContext(), R.drawable.ic_baseline_account_darker_gray_circle_24);
        if (organizerCards[position].organizerImage != null) {
            image = organizerCards[position].organizerImage;
        }
        viewHolder.getImageView().setImageDrawable(image);
    }

    @Override
    public int getItemCount() {
        return organizerCards.length;
    }
}
