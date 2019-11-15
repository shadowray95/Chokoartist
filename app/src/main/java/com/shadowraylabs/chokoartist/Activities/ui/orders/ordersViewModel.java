package com.shadowraylabs.chokoartist.Activities.ui.orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ordersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ordersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}