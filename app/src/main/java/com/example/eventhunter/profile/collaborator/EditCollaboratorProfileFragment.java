package com.example.eventhunter.profile.collaborator;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.eventhunter.R;
import com.example.eventhunter.databinding.EditCollaboratorProfileFragmentBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.profile.service.CollaboratorProfileService;
import com.example.eventhunter.utils.photoUpload.PhotoUploadService;
import com.google.android.material.snackbar.Snackbar;

public class EditCollaboratorProfileFragment extends Fragment {

    @Injectable
    private CollaboratorProfileService collaboratorProfileService;

    @Injectable
    private PhotoUploadService photoUploadService;

    private CollaboratorProfileViewModel mViewModel;
    private EditCollaboratorProfileFragmentBinding binding;

    public EditCollaboratorProfileFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static EditCollaboratorProfileFragment newInstance() {
        return new EditCollaboratorProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(CollaboratorProfileViewModel.class);

        binding = EditCollaboratorProfileFragmentBinding.inflate(inflater, container, false);

        mViewModel.getCollaboratorName().observe(getViewLifecycleOwner(), binding.collaboratorNameTextView::setText);
        mViewModel.getCollaboratorPhoneNumber().observe(getViewLifecycleOwner(), binding.editTextCollaboratorPhoneNumber::setText);
        mViewModel.getCollaboratorAddress().observe(getViewLifecycleOwner(), binding.editTextTextCollaboratorAddress::setText);
        mViewModel.getCollaboratorPhoto().observe(getViewLifecycleOwner(), bitmap -> {
            if (bitmap != null) {
                binding.collaboratorProfileImage.setImageBitmap(bitmap);
            } else {
                Drawable image = AppCompatResources.getDrawable(requireContext(), R.drawable.photo_unavailable);
                binding.collaboratorProfileImage.setImageDrawable(image);
            }
        });

        binding.saveCollaboratorProfileButton.setOnClickListener(view -> {
            CollaboratorModel collaboratorModel = new CollaboratorModel();

            collaboratorModel.address = binding.editTextTextCollaboratorAddress.getText().toString();
            collaboratorModel.phoneNumber = binding.editTextCollaboratorPhoneNumber.getText().toString();
            collaboratorModel.profilePhoto = mViewModel.getCollaboratorPhoto().getValue();

            mViewModel.setCollaboratorPhoneNumber(collaboratorModel.phoneNumber);
            mViewModel.setCollaboratorAddress(collaboratorModel.address);

            String collaboratorId = mViewModel.getCollaboratorId().getValue();

            collaboratorProfileService.updateCollaboratorProfile(collaboratorId, collaboratorModel, updateStatus -> {
                if (updateStatus) {
                    Snackbar.make(view, "Profile updated successfully!", Snackbar.LENGTH_SHORT)
                            .show();

                    Navigation.findNavController(view).navigate(EditCollaboratorProfileFragmentDirections.navigateToCollaboratorProfileFragment());
                } else {
                    Snackbar.make(view, "Unsuccessful update!", Snackbar.LENGTH_SHORT)
                            .show();
                }
            });

        });

        binding.uploadCollaboratorProfileImageFab.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.inflate(R.menu.upload_photo_popup_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {

                if (menuItem.getItemId() == R.id.uploadFromCameraMenuItem) {
                    photoUploadService.launchCamera(bitmap -> {
                        if (bitmap != null) {
                            mViewModel.setCollaboratorPhoto(bitmap);
                        }
                    });
                    return true;
                }

                if (menuItem.getItemId() == R.id.uploadFromGalleryMenuItem) {
                    photoUploadService.openGallery(bitmap -> {
                        if (bitmap != null) {
                            mViewModel.setCollaboratorPhoto(bitmap);
                        }
                    });
                    return true;
                }

                return false;
            });
            popupMenu.show();

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
