package com.shadowraylabs.chokoartist.Activities.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class accountViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public accountViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tools fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}