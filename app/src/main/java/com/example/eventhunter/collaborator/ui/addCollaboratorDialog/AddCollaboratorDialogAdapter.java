package com.example.eventhunter.collaborator.ui.addCollaboratorDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.eventhunter.R;
import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddCollaboratorDialogAdapter extends RecyclerView.Adapter<AddCollaboratorDialogAdapter.ViewHolder> implements Filterable {

    private List<CollaboratorHeader> collaboratorNames;
    private List<CollaboratorHeader> collaboratorNamesFiltered;
    private final Consumer<CollaboratorHeader> onCollaboratorNameSelected;

    public AddCollaboratorDialogAdapter(Consumer<CollaboratorHeader> onCollaboratorNameSelected) {
        this.collaboratorNames = new ArrayList<>();
        collaboratorNamesFiltered = collaboratorNames;
        this.onCollaboratorNameSelected = onCollaboratorNameSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.add_collaborator_dialog_recycler_view_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.collaboratorNameTextView.setText(collaboratorNamesFiltered.get(position).collaboratorName);
        holder.itemView.setOnClickListener(view ->
                onCollaboratorNameSelected.accept(collaboratorNamesFiltered.get(position)));
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
                            .filter(collaboratorHeader -> collaboratorHeader.collaboratorName.toLowerCase().contains(filter.toLowerCase()))
                            .collect(Collectors.toList());
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = collaboratorNamesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                collaboratorNamesFiltered = (List<CollaboratorHeader>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void updateDataSource(List<CollaboratorHeader> collaboratorHeaders) {
        this.collaboratorNames = collaboratorHeaders;
        this.collaboratorNamesFiltered = collaboratorNames;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView collaboratorNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            collaboratorNameTextView = itemView.findViewById(R.id.addCollaboratorDialogRecyclerViewRowTextView);
        }
    }
}
