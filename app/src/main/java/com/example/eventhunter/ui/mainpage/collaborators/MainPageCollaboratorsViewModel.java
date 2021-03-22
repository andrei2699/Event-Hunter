package com.example.eventhunter.ui.mainpage.collaborators;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainPageCollaboratorsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MainPageCollaboratorsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Collaborators fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}