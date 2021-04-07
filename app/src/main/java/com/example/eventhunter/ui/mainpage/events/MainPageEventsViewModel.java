package com.example.eventhunter.ui.mainpage.events;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainPageEventsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MainPageEventsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Events fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}