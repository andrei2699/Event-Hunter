package com.example.eventhunter.profile.organizer;

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
import com.example.eventhunter.profile.service.OrganizerProfileService;
import com.example.eventhunter.events.createEventForm.EventFormViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

public class OrganizerProfileFragment extends Fragment {

    private OrganizerProfileViewModel mViewModel;
    private TabLayout tabLayout;
    private String organizerId;

    @Injectable
    private OrganizerProfileService organizerProfileService;

    @Injectable
    private AuthenticationService authenticationService;

    public OrganizerProfileFragment() {
        ServiceLocator.getInstance().inject(this);
    }


    public static OrganizerProfileFragment newInstance() {
        return new OrganizerProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_organizer_profile, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(OrganizerProfileViewModel.class);

        organizerId = getArguments() != null ? getArguments().getString("organizerId") : null;
        if (organizerId != null && !organizerId.isEmpty()) {
            organizerProfileService.getOrganizerProfileById(organizerId, organizerModel -> {
                mViewModel.setOrganizerAddress(organizerModel.address);
                mViewModel.setOrganizerId(organizerId);
                mViewModel.setOrganizerEmail(organizerModel.email);
                mViewModel.setOrganizerName(organizerModel.name);
                mViewModel.setOrganizerPhoneNumber(organizerModel.phoneNumber);
                mViewModel.setOrganizerNumberOfOrganizedEvents(organizerModel.organizedEvents+"");
                mViewModel.setOrganizerType(organizerModel.eventType);
                mViewModel.setOrganizerPhoto(organizerModel.profilePhoto);
            });
        }

        authenticationService.getLoggedUserData(loggedUserData -> {
            setHasOptionsMenu(loggedUserData.id.equals(organizerId));
        });

        TextView organizerNameTextView = view.findViewById(R.id.organizerNameTextView);
        ImageView organizerProfileImage = view.findViewById(R.id.organizerProfileImage);

        mViewModel.getOrganizerName().observe(getViewLifecycleOwner(), organizerNameTextView::setText);
        mViewModel.getOrganizerPhoto().observe(getViewLifecycleOwner(), bitmap -> {
            if (bitmap != null) {
                organizerProfileImage.setImageBitmap(bitmap);
            } else {
                Drawable image = AppCompatResources.getDrawable(requireContext(), R.drawable.photo_unavailable);
                organizerProfileImage.setImageDrawable(image);
            }
        });

        tabLayout = view.findViewById(R.id.tabOrganizerLayout);
        TabItem infoTab = view.findViewById(R.id.infoTab);
        TabItem pastEventsTab = view.findViewById(R.id.pastEventsTab);
        TabItem futureEventsTab = view.findViewById(R.id.futureEventsTab);
        final ViewPager viewPagerOrganizerProfile = view.findViewById(R.id.viewPagerOrganizerProfile);

        FloatingActionButton createEventFabButton = view.findViewById(R.id.createEventFabButton);

        createEventFabButton.setOnClickListener(v -> {
            new ViewModelProvider(requireActivity()).get(EventFormViewModel.class).removeValues();
            Navigation.findNavController(v).navigate(OrganizerProfileFragmentDirections.navigateToCreateEventFormFragment());
        });

        PagerAdapterOrganizer pagerAdapter = new PagerAdapterOrganizer(requireActivity().getSupportFragmentManager(), tabLayout.getTabCount());

        viewPagerOrganizerProfile.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerOrganizerProfile.setCurrentItem(tab.getPosition());
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

            Navigation.findNavController(tabLayout).navigate(OrganizerProfileFragmentDirections.navigateToEditOrganizerProfileFragment());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}