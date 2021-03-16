package com.example.eventhunter.ui.collaboratorHeader;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventhunter.R;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

public class CollaboratorHeaderAdapter extends RecyclerView.Adapter<CollaboratorHeaderAdapter.ViewHolder> {

    private final CollaboratorHeader[] collaborators;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final ImageView imageView;
        private final ImageButton removeHeaderImageButton;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nameTextView = view.findViewById(R.id.collaboratorNameEditText);
            imageView = view.findViewById(R.id.collaboratorImageView);
            removeHeaderImageButton = view.findViewById(R.id.removeHeaderImageButton);
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public ImageButton getRemoveHeaderImageButton() {
            return removeHeaderImageButton;
        }
    }

    public CollaboratorHeaderAdapter(CollaboratorHeader[] dataSet) {
        collaborators = dataSet;
    }

    @NonNull
    @Override
    public CollaboratorHeaderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.collaborator_header, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollaboratorHeaderAdapter.ViewHolder viewHolder, int position) {
        viewHolder.getNameTextView().setText(collaborators[position].collaboratorName);
        Drawable image = AppCompatResources.getDrawable(viewHolder.itemView.getContext(), R.drawable.photo_unavailable);
        if (collaborators[position].collaboratorImage != null) {
            image = collaborators[position].collaboratorImage;
        }
        viewHolder.getImageView().setImageDrawable(image);
    }

    @Override
    public int getItemCount() {
        return collaborators.length;
    }
}