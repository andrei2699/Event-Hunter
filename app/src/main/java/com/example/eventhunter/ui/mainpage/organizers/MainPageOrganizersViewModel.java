package com.example.eventhunter.ui.mainpage.organizers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainPageOrganizersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MainPageOrganizersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Organizers fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}