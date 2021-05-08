package com.example.eventhunter.profile.collaborator;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventhunter.R;
import com.example.eventhunter.authentication.AuthenticationService;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.profile.service.CollaboratorProfileService;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

public class CollaboratorProfileFragment extends Fragment {

    private CollaboratorProfileViewModel mViewModel;
    private TabLayout tabLayout;

    @Injectable
    private CollaboratorProfileService collaboratorProfileService;

    @Injectable
    private AuthenticationService authenticationService;

    public CollaboratorProfileFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    public static CollaboratorProfileFragment newInstance() {
        return new CollaboratorProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_collaborator_profile, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(CollaboratorProfileViewModel.class);

        String collaboratorId = getArguments() != null ? getArguments().getString("collaboratorId") : null;
        if (collaboratorId != null && !collaboratorId.isEmpty()) {
            collaboratorProfileService.getCollaboratorProfileById(collaboratorId, collaboratorModel -> {
                mViewModel.setCollaboratorId(collaboratorId);
                mViewModel.setCollaboratorAddress(collaboratorModel.address);
                mViewModel.setCollaboratorEmail(collaboratorModel.email);
                mViewModel.setCollaboratorName(collaboratorModel.name);
                mViewModel.setCollaboratorPhoneNumber(collaboratorModel.phoneNumber);
                mViewModel.setCollaboratorPhoto(collaboratorModel.profilePhoto);
            });
        }

        authenticationService.getLoggedUserData(loggedUserData -> {
            setHasOptionsMenu(loggedUserData.id.equals(collaboratorId));
        });

        TextView collaboratorNameTextView = view.findViewById(R.id.collaboratorNameTextView);
        ImageView collaboratorProfileImage = view.findViewById(R.id.collaboratorProfileImage);

        mViewModel.getCollaboratorName().observe(getViewLifecycleOwner(), collaboratorNameTextView::setText);
        mViewModel.getCollaboratorPhoto().observe(getViewLifecycleOwner(), bitmap -> {
            if (bitmap != null) {
                collaboratorProfileImage.setImageBitmap(bitmap);
            } else {
                Drawable image = AppCompatResources.getDrawable(requireContext(), R.drawable.photo_unavailable);
                collaboratorProfileImage.setImageDrawable(image);
            }
        });

        tabLayout = view.findViewById(R.id.tabLayoutCollaborator);
        TabItem infoTab = view.findViewById(R.id.infoTab);
        TabItem pastEventsTab = view.findViewById(R.id.pastEventsTab);
        TabItem futureEventsTab = view.findViewById(R.id.futureEventsTab);
        final ViewPager viewPagerCollaboratorProfile = view.findViewById(R.id.viewPagerCollaboratorProfile);

        PagerAdapterCollaborator pagerAdapter = new PagerAdapterCollaborator(this.getChildFragmentManager(), tabLayout.getTabCount());

        viewPagerCollaboratorProfile.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerCollaboratorProfile.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.profile_edit_action) {

            Navigation.findNavController(tabLayout).navigate(CollaboratorProfileFragmentDirections.navigateToEditCollaboratorProfileFragment());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}