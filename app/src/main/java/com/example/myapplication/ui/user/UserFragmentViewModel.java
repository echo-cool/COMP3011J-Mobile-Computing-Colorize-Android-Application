package com.example.myapplication.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserFragmentViewModel extends ViewModel {
    private MutableLiveData<Double> balance;
    private MutableLiveData<Integer> processedCount;
    private MutableLiveData<Integer> remainingCount;
    private MutableLiveData<String> username;

    public UserFragmentViewModel() {
        balance = new MutableLiveData<>(0.0);
        processedCount = new MutableLiveData<>(0);
        remainingCount = new MutableLiveData<>(0);
        username = new MutableLiveData<>("Can't get username");
    }

    public MutableLiveData<String> getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username.postValue(username);
    }


    public MutableLiveData<Double> getBalance() {
        return balance;
    }

    public MutableLiveData<Integer> getProcessedCount() {
        return processedCount;
    }

    public MutableLiveData<Integer> getRemainingCount() {
        return remainingCount;
    }

    public void setBalance(Double balance) {
        this.balance.postValue(balance);
    }


    public void setProcessedCount(Integer processedCount) {
        this.processedCount.postValue(processedCount);
    }



    public void setRemainingCount(Integer remainingCount) {
        this.remainingCount.postValue(remainingCount);
    }
}