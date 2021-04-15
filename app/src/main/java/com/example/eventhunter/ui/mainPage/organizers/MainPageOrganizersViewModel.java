package com.example.eventhunter.ui.mainPage.organizers;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventhunter.ui.mainPage.organizers.organizerCard.OrganizerCard;

import java.util.List;

public class MainPageOrganizersViewModel extends ViewModel {

    private MutableLiveData<List<OrganizerCard>> organizers;

    public MainPageOrganizersViewModel() {
        organizers = new MutableLiveData<>();
    }

    public void setEventCards(List<OrganizerCard> organizers) {
        this.organizers.setValue(organizers);
    }

    public MutableLiveData<List<OrganizerCard>> getOrganizerCards() {
        return organizers;
    }
}