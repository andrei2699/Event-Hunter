package com.example.eventhunter.ui.mainPage.organizers.organizerCard;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class OrganizerCardAdapter extends RecyclerView.Adapter<OrganizerCardAdapter.ViewHolder> {
    private final List<OrganizerCard> organizerCards;
    private final Fragment fragment;

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

    public OrganizerCardAdapter(Fragment fragment, List<OrganizerCard> dataSet) {
        this.fragment = fragment;
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
        OrganizerCard organizerCard = organizerCards.get(position);

        viewHolder.getNameTextView().setText(organizerCard.organizerName);
        viewHolder.getEmailTextView().setText(organizerCard.organizerEmail);
        viewHolder.getEventTypeTextView().setText(organizerCard.organizerEventsType);
        Drawable image = AppCompatResources.getDrawable(viewHolder.itemView.getContext(), R.drawable.ic_baseline_account_darker_gray_circle_24);
        if (organizerCard.organizerImage != null) {
            image = organizerCard.organizerImage;
        }
        viewHolder.getImageView().setImageDrawable(image);

        viewHolder.getViewProfileButton().setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("organizerId", organizerCard.organizerId);
            Navigation.findNavController(fragment.getView()).navigate(R.id.nav_organizerProfile, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return organizerCards.size();
    }
}
