package com.example.eventhunter.profile.organizer;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.eventhunter.R;
import com.example.eventhunter.databinding.EditOrganizerProfileFragmentBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.profile.service.OrganizerProfileService;
import com.example.eventhunter.utils.photoUpload.PhotoUploadService;
import com.google.android.material.snackbar.Snackbar;

public class EditOrganizerProfileFragment extends Fragment {

    @Injectable
    private OrganizerProfileService organizerProfileService;

    @Injectable
    private PhotoUploadService photoUploadService;

    private OrganizerProfileViewModel mViewModel;
    private EditOrganizerProfileFragmentBinding binding;

    public EditOrganizerProfileFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static EditOrganizerProfileFragment newInstance() {
        return new EditOrganizerProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(OrganizerProfileViewModel.class);

        binding = EditOrganizerProfileFragmentBinding.inflate(inflater, container, false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.organizer_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerOrganizerType.setAdapter(adapter);

        mViewModel.getOrganizerType().observe(getViewLifecycleOwner(), s -> {
            int index = -1;

            String[] stringArray = getResources().getStringArray(R.array.organizer_types_array);
            for (int i = 0; i < stringArray.length; i++) {
                if (stringArray[i].equals(s)) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                binding.spinnerOrganizerType.setSelection(index);
            }
        });

        mViewModel.getOrganizerAddress().observe(getViewLifecycleOwner(), binding.editTextTextOrganizerAddress::setText);
        mViewModel.getOrganizerName().observe(getViewLifecycleOwner(), binding.organizerNameTextView::setText);
        mViewModel.getOrganizerPhoneNumber().observe(getViewLifecycleOwner(), binding.editTextOrganizerPhoneNumber::setText);
        mViewModel.getOrganizerPhoto().observe(getViewLifecycleOwner(), bitmap -> {
            if (bitmap != null) {
                binding.organizerProfileImage.setImageBitmap(bitmap);
            } else {
                Drawable image = AppCompatResources.getDrawable(requireContext(), R.drawable.photo_unavailable);
                binding.organizerProfileImage.setImageDrawable(image);
            }
        });
        binding.saveOrganizerProfileButton.setOnClickListener(view -> {

            OrganizerModel organizerModel = new OrganizerModel();

            organizerModel.address = binding.editTextTextOrganizerAddress.getText().toString();
            organizerModel.phoneNumber = binding.editTextOrganizerPhoneNumber.getText().toString();
            organizerModel.eventType = binding.spinnerOrganizerType.getSelectedItem().toString();
            organizerModel.profilePhoto = mViewModel.getOrganizerPhoto().getValue();

            mViewModel.setOrganizerType(organizerModel.eventType);
            mViewModel.setOrganizerPhoneNumber(organizerModel.phoneNumber);
            mViewModel.setOrganizerAddress(organizerModel.address);

            String organizerId = mViewModel.getOrganizerId().getValue();

            organizerProfileService.updateOrganizerProfile(organizerId, organizerModel, updateStatus -> {
                if (updateStatus) {
                    Snackbar.make(view, "Profile updated successfully!", Snackbar.LENGTH_SHORT)
                            .show();

                    Navigation.findNavController(view).navigate(EditOrganizerProfileFragmentDirections.navigateToOrganizerProfile());
                } else {
                    Snackbar.make(view, "Unsuccessful update!", Snackbar.LENGTH_SHORT)
                            .show();
                }
            });

        });

        binding.uploadOrganizerProfileImageFab.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.inflate(R.menu.upload_photo_popup_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {

                if (menuItem.getItemId() == R.id.uploadFromCameraMenuItem) {
                    photoUploadService.launchCamera(bitmap -> {
                        if (bitmap != null) {
                            mViewModel.setOrganizerPhoto(bitmap);
                        }
                    });
                    return true;
                }

                if (menuItem.getItemId() == R.id.uploadFromGalleryMenuItem) {
                    photoUploadService.openGallery(bitmap -> {
                        if (bitmap != null) {
                            mViewModel.setOrganizerPhoto(bitmap);
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