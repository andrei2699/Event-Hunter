package com.example.eventhunter.profile.organizer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapterOrganizer extends FragmentPagerAdapter {

    private final int numberOfTabs;

    public PagerAdapterOrganizer(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new OrganizerFutureEventsFragment();
            case 2:
                return new OrganizerPastEventsFragment();
            case 0:
            default:
                return new OrganizerInfoFragment();
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
