package com.shadowraylabs.chokoartist.Activities.ui.adminPanel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class adminPanelViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public adminPanelViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}