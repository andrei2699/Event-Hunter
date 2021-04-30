package com.example.eventhunter.profile.collaborator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapterCollaborator extends FragmentPagerAdapter {

    private int numberOfTabs;

    public PagerAdapterCollaborator(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CollaboratorInfoFragment();
            case 1:
                return new CollaboratorPastEventsFragment();
            case 2:
                return new CollaboratorFutureEventsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
