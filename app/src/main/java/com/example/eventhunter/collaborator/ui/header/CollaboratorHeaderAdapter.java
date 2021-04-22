package com.example.eventhunter.collaborator.ui.header;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventhunter.R;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

public class CollaboratorHeaderAdapter extends RecyclerView.Adapter<CollaboratorHeaderAdapter.ViewHolder> {

    private List<CollaboratorHeader> collaborators;
    private final Consumer<CollaboratorHeader> onCollaboratorHeaderRemoveButtonClick;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nameTextView;
        public final ImageView imageView;
        public final ImageButton removeHeaderImageButton;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.collaboratorNameTextView);
            imageView = view.findViewById(R.id.collaboratorImageView);
            removeHeaderImageButton = view.findViewById(R.id.removeHeaderImageButton);
        }
    }

    public CollaboratorHeaderAdapter(Consumer<CollaboratorHeader> onCollaboratorHeaderRemoveButtonClick) {
        this.onCollaboratorHeaderRemoveButtonClick = onCollaboratorHeaderRemoveButtonClick;
        collaborators = new ArrayList<>();
    }

    public void updateDataSet(List<CollaboratorHeader> collaboratorHeaders) {
        collaborators = collaboratorHeaders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CollaboratorHeaderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.collaborator_header, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollaboratorHeaderAdapter.ViewHolder viewHolder, int position) {
        CollaboratorHeader collaboratorHeader = collaborators.get(position);

        viewHolder.nameTextView.setText(collaboratorHeader.getCollaboratorName());

        Bitmap collaboratorBitmap = collaboratorHeader.getCollaboratorBitmap();
        if (collaboratorBitmap != null) {
            viewHolder.imageView.setImageBitmap(collaboratorBitmap);
        } else {
            Drawable image = AppCompatResources.getDrawable(viewHolder.itemView.getContext(), R.drawable.photo_unavailable);
            viewHolder.imageView.setImageDrawable(image);
        }

        viewHolder.removeHeaderImageButton.setOnClickListener(view -> onCollaboratorHeaderRemoveButtonClick.accept(collaboratorHeader));
    }

    @Override
    public int getItemCount() {
        return collaborators.size();
    }
}