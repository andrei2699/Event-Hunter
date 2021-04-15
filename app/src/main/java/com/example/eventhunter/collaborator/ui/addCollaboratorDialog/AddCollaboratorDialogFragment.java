package com.example.eventhunter.collaborator.ui.addCollaboratorDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.eventhunter.R;
import com.example.eventhunter.collaborator.service.CollaboratorService;
import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;

import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddCollaboratorDialogFragment extends DialogFragment {

    @Injectable()
    private CollaboratorService collaboratorService;

    private Consumer<CollaboratorHeader> onCollaboratorHeaderSelected;

    public AddCollaboratorDialogFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static AddCollaboratorDialogFragment newInstance(Consumer<CollaboratorHeader> onCollaboratorHeaderSelected) {
        AddCollaboratorDialogFragment addCollaboratorDialogFragment = new AddCollaboratorDialogFragment();
        addCollaboratorDialogFragment.onCollaboratorHeaderSelected = onCollaboratorHeaderSelected;

        return addCollaboratorDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_collaborator_dialog, null);

        AddCollaboratorDialogAdapter addCollaboratorDialogAdapter = new AddCollaboratorDialogAdapter(collaboratorHeader -> {
            onCollaboratorHeaderSelected.accept(collaboratorHeader);
            dismiss();
        });

        collaboratorService.getAllCollaborators(addCollaboratorDialogAdapter::updateDataSource);

        TextView searchTextView = view.findViewById(R.id.searchCollaboratorAddCollaboratorDialogEditText);
        searchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addCollaboratorDialogAdapter.getFilter().filter(editable);
            }
        });


        RecyclerView collaboratorsAddCollaboratorDialogRecyclerView = view.findViewById(R.id.collaboratorsAddCollaboratorDialogRecyclerView);
        collaboratorsAddCollaboratorDialogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        collaboratorsAddCollaboratorDialogRecyclerView.setAdapter(addCollaboratorDialogAdapter);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Add Collaborator")
                .setView(view)
                .setNegativeButton("Close", (dialogInterface, i) -> dismiss())
                .create();
    }
}
