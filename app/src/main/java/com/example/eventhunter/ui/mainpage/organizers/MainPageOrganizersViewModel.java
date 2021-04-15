package com.example.eventhunter.ui.mainpage.organizers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventhunter.ui.mainpage.collaborators.collaboratorCard.CollaboratorCard;
import com.example.eventhunter.ui.mainpage.organizers.organizerCard.OrganizerCard;

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