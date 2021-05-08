package com.example.eventhunter.collaborator.ui.addCollaboratorDialog;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventhunter.R;
import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;
import com.example.eventhunter.collaborator.ui.header.CollaboratorHeaderWithImage;
import com.example.eventhunter.profile.collaborator.CollaboratorModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

public class AddCollaboratorDialogAdapter extends RecyclerView.Adapter<AddCollaboratorDialogAdapter.ViewHolder> implements Filterable {

    private List<CollaboratorHeaderWithImage> collaboratorNames;
    private List<CollaboratorHeaderWithImage> collaboratorNamesFiltered;

    private final Consumer<CollaboratorHeader> onCollaboratorNameSelected;

    public AddCollaboratorDialogAdapter(Consumer<CollaboratorHeader> onCollaboratorNameSelected) {
        this.collaboratorNames = new ArrayList<>();
        collaboratorNamesFiltered = collaboratorNames;
        this.onCollaboratorNameSelected = onCollaboratorNameSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.collaborator_view_header, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CollaboratorHeaderWithImage collaboratorHeaderWithImage = collaboratorNamesFiltered.get(position);

        holder.collaboratorNameTextView.setText(collaboratorHeaderWithImage.getCollaboratorName());

        if (collaboratorHeaderWithImage.getCollaboratorImage() != null) {
            holder.collaboratorImageView.setImageBitmap(collaboratorHeaderWithImage.getCollaboratorImage());
        } else {
            Drawable image = AppCompatResources.getDrawable(holder.collaboratorImageView.getContext(), R.drawable.photo_unavailable);
            holder.collaboratorImageView.setImageDrawable(image);
        }

        holder.itemView.setOnClickListener(view ->
                onCollaboratorNameSelected.accept(collaboratorHeaderWithImage));
    }

    @Override
    public int getItemCount() {
        return collaboratorNamesFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filter = charSequence.toString();
                if (filter.isEmpty()) {
                    collaboratorNamesFiltered = collaboratorNames;
                } else {
                    collaboratorNamesFiltered = collaboratorNames.stream()
                            .filter(collaboratorHeader -> collaboratorHeader.getCollaboratorName().toLowerCase().contains(filter.toLowerCase()))
                            .collect(Collectors.toList());
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = collaboratorNamesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                collaboratorNamesFiltered = (List<CollaboratorHeaderWithImage>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void updateDataSource(CollaboratorModel collaboratorModel) {
        this.collaboratorNames.add(new CollaboratorHeaderWithImage(collaboratorModel.id, collaboratorModel.name, collaboratorModel.profilePhoto));
        this.collaboratorNamesFiltered = collaboratorNames;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView collaboratorNameTextView;
        public ImageView collaboratorImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            collaboratorNameTextView = itemView.findViewById(R.id.collaboratorNameViewHeader);
            collaboratorImageView = itemView.findViewById(R.id.collaboratorImageCollaboratorCard);
        }
    }
}
