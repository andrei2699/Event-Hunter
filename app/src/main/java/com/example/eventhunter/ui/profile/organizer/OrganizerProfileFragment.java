package com.example.eventhunter.ui.profile.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.eventhunter.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class OrganizerProfileFragment extends Fragment {

    private OrganizerProfileViewModel mViewModel;
    private TabLayout tabLayout;

    public static OrganizerProfileFragment newInstance() {
        return new OrganizerProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_organizer_profile, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(OrganizerProfileViewModel.class);

        TextView organizerNameTextView = view.findViewById(R.id.organizerNameTextView);

        mViewModel.getOrganizerName().observe(getViewLifecycleOwner(), organizerName -> {
            organizerNameTextView.setText(organizerName);
        });

        setHasOptionsMenu(true);

        tabLayout = view.findViewById(R.id.tabOrganizerLayout);
        TabItem infoTab = view.findViewById(R.id.infoTab);
        TabItem pastEventsTab = view.findViewById(R.id.pastEventsTab);
        TabItem futureEventsTab = view.findViewById(R.id.futureEventsTab);
        final ViewPager viewPagerOrganizerProfile = view.findViewById(R.id.viewPagerOrganizerProfile);

        FloatingActionButton createEventFabButton = view.findViewById(R.id.createEventFabButton);

        createEventFabButton.setOnClickListener(v -> {
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