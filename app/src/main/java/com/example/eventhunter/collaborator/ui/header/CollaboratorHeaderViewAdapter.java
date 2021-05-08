package com.example.eventhunter.collaborator.ui.header;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventhunter.R;
import com.example.eventhunter.profile.service.ProfileService;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

public class CollaboratorHeaderViewAdapter extends RecyclerView.Adapter<CollaboratorHeaderViewAdapter.ViewHolder> {

    private final ProfileService profileService;

    private List<CollaboratorHeader> collaborators = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final de.hdodenhof.circleimageview.CircleImageView imageView;

        public ViewHolder(View view) {
            super(view);

            nameTextView = view.findViewById(R.id.collaboratorNameViewHeader);
            imageView = view.findViewById(R.id.collaboratorImageCollaboratorCard);
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public CollaboratorHeaderViewAdapter(ProfileService profileService) {
        this.profileService = profileService;
    }

    public void setCollaborators(List<CollaboratorHeader> collaborators) {
        this.collaborators = collaborators;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CollaboratorHeaderViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.collaborator_view_header, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        CollaboratorHeader collaborator = collaborators.get(position);

        viewHolder.getNameTextView().setText(collaborator.getCollaboratorName());

        Drawable image = AppCompatResources.getDrawable(viewHolder.itemView.getContext(), R.drawable.photo_unavailable);
        viewHolder.getImageView().setImageDrawable(image);

        profileService.getProfilePhoto(collaborator.getCollaboratorId(), bitmap -> {
            if (bitmap != null) {
                viewHolder.getImageView().setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return collaborators.size();
    }
}

