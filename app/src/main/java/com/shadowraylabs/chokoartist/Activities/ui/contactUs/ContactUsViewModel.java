package com.shadowraylabs.chokoartist.Activities.ui.contactUs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContactUsViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public ContactUsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is contact us fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
