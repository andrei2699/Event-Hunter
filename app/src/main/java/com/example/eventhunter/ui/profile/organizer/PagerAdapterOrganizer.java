package com.example.eventhunter.ui.profile.organizer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapterOrganizer extends FragmentPagerAdapter {

    private int numberOfTabs;

    public PagerAdapterOrganizer(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OrganizerInfoFragment();
            case 1:
                return new OrganizerPastEventsFragment();
            case 2:
                return new OrganizerFutureEventsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
