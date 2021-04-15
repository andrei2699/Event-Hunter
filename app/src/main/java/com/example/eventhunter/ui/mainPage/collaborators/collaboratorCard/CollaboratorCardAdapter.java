package com.example.eventhunter.ui.mainPage.collaborators.collaboratorCard;

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

public class CollaboratorCardAdapter extends RecyclerView.Adapter<CollaboratorCardAdapter.ViewHolder> {
    private final CollaboratorCard[] collaboratorCards;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView emailTextView;
        private final ImageView imageView;
        private final Button viewProfileButton;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.collaboratorNameCollaboratorCard);
            emailTextView = view.findViewById(R.id.collaboratorEmailCollaboratorCard);
            imageView = view.findViewById(R.id.collaboratorImageCollaboratorCard);
            viewProfileButton = view.findViewById(R.id.viewProfileButtonCollaboratorCard);
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public TextView getEmailTextView() {
            return emailTextView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public Button getViewProfileButton() {
            return viewProfileButton;
        }
    }

    public CollaboratorCardAdapter(CollaboratorCard[] dataSet) {
        collaboratorCards = dataSet;
    }

    @NonNull
    @Override
    public CollaboratorCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.collaborator_card, viewGroup, false);

        return new CollaboratorCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollaboratorCardAdapter.ViewHolder viewHolder, int position) {
        viewHolder.getNameTextView().setText(collaboratorCards[position].collaboratorName);
        viewHolder.getEmailTextView().setText(collaboratorCards[position].collaboratorEmail);
        Drawable image = AppCompatResources.getDrawable(viewHolder.itemView.getContext(), R.drawable.ic_baseline_account_darker_gray_circle_24);
        if (collaboratorCards[position].collaboratorImage != null) {
            image = collaboratorCards[position].collaboratorImage;
        }
        viewHolder.getImageView().setImageDrawable(image);
    }

    @Override
    public int getItemCount() {
        return collaboratorCards.length;
    }
}