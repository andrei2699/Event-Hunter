package com.example.eventhunter.ui.mainpage.collaborators;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventhunter.ui.mainpage.collaborators.collaboratorCard.CollaboratorCard;

import java.util.List;

public class MainPageCollaboratorsViewModel extends ViewModel {

    private MutableLiveData<List<CollaboratorCard>> collaborators;

    public MainPageCollaboratorsViewModel() {
        collaborators = new MutableLiveData<>();
    }

    public void setEventCards(List<CollaboratorCard> events) {
        this.collaborators.setValue(events);
    }

    public MutableLiveData<List<CollaboratorCard>> getEventCards() {
        return collaborators;
    }
}