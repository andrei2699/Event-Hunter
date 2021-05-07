package com.example.eventhunter.ui.mainPage.collaborators.collaboratorCard;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventhunter.R;
import com.example.eventhunter.profile.collaborator.CollaboratorModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class CollaboratorCardAdapter extends RecyclerView.Adapter<CollaboratorCardAdapter.ViewHolder> {
    private final List<CollaboratorCard> collaboratorCards;
    private final Fragment fragment;

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

    public CollaboratorCardAdapter(Fragment fragment) {
        this.fragment = fragment;
        collaboratorCards = new ArrayList<>();
    }

    public void updateDataSource(CollaboratorModel collaboratorModel) {
        this.collaboratorCards.add(new CollaboratorCard(collaboratorModel.id, collaboratorModel.name, collaboratorModel.email, collaboratorModel.profilePhoto));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CollaboratorCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.collaborator_card, viewGroup, false);

        return new CollaboratorCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollaboratorCardAdapter.ViewHolder viewHolder, int position) {
        CollaboratorCard collaboratorCard = collaboratorCards.get(position);

        viewHolder.getNameTextView().setText(collaboratorCard.collaboratorName);
        viewHolder.getEmailTextView().setText(collaboratorCard.collaboratorEmail);

        if (collaboratorCard.collaboratorImage != null) {
            viewHolder.getImageView().setImageBitmap(collaboratorCard.collaboratorImage);
        } else {
            Drawable image = AppCompatResources.getDrawable(fragment.requireContext(), R.drawable.photo_unavailable);
            viewHolder.getImageView().setImageDrawable(image);
        }

        viewHolder.getViewProfileButton().setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("collaboratorId", collaboratorCard.collaboratorId);
            Navigation.findNavController(fragment.getView()).navigate(R.id.nav_collaborator_profile_fragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return collaboratorCards.size();
    }
}