package com.example.myapplication.ui.ClarityEnhancement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClarityEnhancementViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ClarityEnhancementViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}