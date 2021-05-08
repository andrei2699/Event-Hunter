package com.example.eventhunter.profile.collaborator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapterCollaborator extends FragmentPagerAdapter {

    private final int numberOfTabs;

    public PagerAdapterCollaborator(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new CollaboratorFutureEventsFragment();
            case 2:
                return new CollaboratorPastEventsFragment();
            case 0:
            default:
                return new CollaboratorInfoFragment();
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
