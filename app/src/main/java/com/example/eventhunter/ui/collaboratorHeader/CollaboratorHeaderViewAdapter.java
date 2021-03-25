package com.example.eventhunter.ui.collaboratorHeader;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventhunter.R;

public class CollaboratorHeaderViewAdapter extends RecyclerView.Adapter<CollaboratorHeaderViewAdapter.ViewHolder> {

    private final CollaboratorHeader[] collaborators;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            nameTextView = view.findViewById(R.id.collaboratorNameViewHeader);
            imageView = view.findViewById(R.id.collaboratorPhotoViewHeader);
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public CollaboratorHeaderViewAdapter(CollaboratorHeader[] dataSet) {
        collaborators = dataSet;
    }

    @NonNull
    @Override
    public CollaboratorHeaderViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.collaborator_view_header, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
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

