package com.example.eventhunter.events.createEventForm;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.example.eventhunter.R;
import com.example.eventhunter.collaborator.ui.addCollaboratorDialog.AddCollaboratorDialogFragment;
import com.example.eventhunter.collaborator.ui.header.CollaboratorHeaderAdapter;
import com.example.eventhunter.databinding.FragmentCreateEventFormPhotoAndCollabsBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.profile.service.ProfileService;
import com.example.eventhunter.utils.photoUpload.PhotoUploadService;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CreateEventFormPhotoAndCollabsFragment extends Fragment {

    private static final int ADD_COLLABORATOR_DIALOG_REQUEST_CODE = 300;
    private EventFormViewModel mViewModel;
    private FragmentCreateEventFormPhotoAndCollabsBinding binding;

    @Injectable
    private ProfileService profileService;

    @Injectable
    private PhotoUploadService photoUploadService;

    public CreateEventFormPhotoAndCollabsFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static CreateEventFormPhotoAndCollabsFragment newInstance() {
        return new CreateEventFormPhotoAndCollabsFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(EventFormViewModel.class);
        binding = FragmentCreateEventFormPhotoAndCollabsBinding.inflate(inflater, container, false);

        RecyclerView collaboratorsRecyclerView = binding.collaboratorsRecyclerView;
        CollaboratorHeaderAdapter collaboratorHeaderAdapter = new CollaboratorHeaderAdapter(profileService, collaboratorHeader ->
                mViewModel.removeCollaborator(collaboratorHeader));

        collaboratorsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        collaboratorsRecyclerView.setAdapter(collaboratorHeaderAdapter);

        AtomicReference<String> eventType = new AtomicReference<>("");

        mViewModel.getCollaborators().observe(getViewLifecycleOwner(), collaboratorHeaderAdapter::updateDataSet);
        mViewModel.getEventPhoto().observe(getViewLifecycleOwner(), bitmap -> {
            if (bitmap != null) {
                binding.eventImageView.setImageBitmap(bitmap);
            } else {
                Drawable image = AppCompatResources.getDrawable(requireContext(), R.drawable.image_unavailable);
                binding.eventImageView.setImageDrawable(image);
            }
        });

        mViewModel.getEventType().observe(getViewLifecycleOwner(), s -> {

            String[] stringArray = getResources().getStringArray(R.array.event_types_array);
            for (String value : stringArray) {
                if (value.equals(s)) {
                    eventType.set(value);
                    break;
                }
            }
        });

        binding.uploadImageButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.inflate(R.menu.upload_photo_popup_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {

                if (menuItem.getItemId() == R.id.uploadFromCameraMenuItem) {
                    photoUploadService.launchCamera(bitmap -> {
                        if (bitmap != null) {
                            mViewModel.setEventPhoto(bitmap);
                        }
                    });
                    return true;
                }

                if (menuItem.getItemId() == R.id.uploadFromGalleryMenuItem) {
                    photoUploadService.openGallery(bitmap -> {
                        if (bitmap != null) {
                            mViewModel.setEventPhoto(bitmap);
                        }
                    });
                    return true;
                }

                return false;
            });
            popupMenu.show();

        });

        binding.addCollaboratorButton.setOnClickListener(view -> {
            AddCollaboratorDialogFragment addCollaboratorDialogFragment = AddCollaboratorDialogFragment.newInstance(collaboratorHeader -> mViewModel.addCollaborator(collaboratorHeader));

            addCollaboratorDialogFragment.setTargetFragment(this, ADD_COLLABORATOR_DIALOG_REQUEST_CODE);
            addCollaboratorDialogFragment.show(getParentFragmentManager(), "add_collaborator_dialog");
        });

        binding.eventPhotoAndCollabPreviousButton.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.navigateBackToBasicInfo));

        binding.eventPhotoAndCollabNextButton.setOnClickListener(view -> {
            String[] stringArray = getResources().getStringArray(R.array.event_types_array);
            if (stringArray[0].equals(eventType.get())) {
                Navigation.findNavController(view).navigate(R.id.navigateToOneTimeEvent);
            } else if (stringArray[1].equals(eventType.get())) {
                Navigation.findNavController(view).navigate(R.id.navigateToRepeatableEvent);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}