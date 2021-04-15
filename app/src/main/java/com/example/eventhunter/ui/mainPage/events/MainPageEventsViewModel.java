package com.example.eventhunter.ui.mainPage.events;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventhunter.ui.mainPage.events.eventCard.EventCard;

import java.util.List;

public class MainPageEventsViewModel extends ViewModel {

    private MutableLiveData<List<EventCard>> events;

    public MainPageEventsViewModel() {
        events = new MutableLiveData<>();
    }

    public void setEventCards(List<EventCard> events) {
        this.events.setValue(events);
    }

    public MutableLiveData<List<EventCard>> getEventCards() {
        return events;
    }
}