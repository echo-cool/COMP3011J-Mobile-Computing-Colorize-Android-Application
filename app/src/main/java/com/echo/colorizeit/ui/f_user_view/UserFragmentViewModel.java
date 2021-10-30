package com.echo.colorizeit.ui.f_user_view;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import cn.leancloud.LCObject;
import cn.leancloud.LCUser;
import io.reactivex.disposables.Disposable;

/**
 * @author Wang Yuyang
 * @date 2021-09-22 13:52:43
 */
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

    public void updateUserProfile() {
        LCUser currentUser = LCUser.getCurrentUser();
        currentUser.fetchInBackground().subscribe(new io.reactivex.Observer<LCObject>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(LCObject user) {
                if (currentUser != null) {
                    setUsername(currentUser.getUsername());
                    setBalance(currentUser.getDouble("Balance"));
                    setProcessedCount(currentUser.getInt("ProcessedCount"));
                    setRemainingCount(currentUser.getInt("RemainingCount"));

                } else {

                }
            }
            public void onError(Throwable throwable) {}
            public void onComplete() {}
        });



    }
}
