package com.example.eventhunter.profile.collaborator;

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
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.profile.service.CollaboratorProfileService;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class CollaboratorProfileFragment extends Fragment {

    private CollaboratorProfileViewModel mViewModel;
    private TabLayout tabLayout;

    @Injectable
    private CollaboratorProfileService collaboratorProfileService;

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

        String collaboratorId = getArguments().getString("collaboratorId");
        if (collaboratorId != null && !collaboratorId.isEmpty()) {
            collaboratorProfileService.getCollaboratorProfileById(collaboratorId, collaboratorModel -> {
                mViewModel.setCollaboratorAddress(collaboratorModel.address);
                mViewModel.setCollaboratorEmail(collaboratorModel.email);
                mViewModel.setCollaboratorName(collaboratorModel.name);
                mViewModel.setCollaboratorPhoneNumber(collaboratorModel.phoneNumber);
            });
        }


        TextView collaboratorNameTextView = view.findViewById(R.id.collaboratorNameTextView);

        mViewModel.getCollaboratorName().observe(getViewLifecycleOwner(), collaboratorNameTextView::setText);

        setHasOptionsMenu(true);

        tabLayout = view.findViewById(R.id.tabLayoutCollaborator);
        TabItem infoTab = view.findViewById(R.id.infoTab);
        TabItem pastEventsTab = view.findViewById(R.id.pastEventsTab);
        TabItem futureEventsTab = view.findViewById(R.id.futureEventsTab);
        final ViewPager viewPagerCollaboratorProfile = view.findViewById(R.id.viewPagerCollaboratorProfile);

        PagerAdapterCollaborator pagerAdapter = new PagerAdapterCollaborator(requireActivity().getSupportFragmentManager(), tabLayout.getTabCount());

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