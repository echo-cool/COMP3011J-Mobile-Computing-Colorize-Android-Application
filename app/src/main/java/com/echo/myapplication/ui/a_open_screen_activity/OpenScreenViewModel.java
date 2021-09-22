package com.echo.myapplication.ui.a_open_screen_activity;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.echo.myapplication.Util;

import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class OpenScreenViewModel extends ViewModel {
    private MutableLiveData<SharedPreferences> setting;
    private MutableLiveData<Boolean> Is_user_first_enter_this_app;
    private MutableLiveData<Boolean> gotoMainActivity;
    private MutableLiveData<Boolean> showAlterDialog;

    public OpenScreenViewModel() {
        setting = new MutableLiveData<>();
        Is_user_first_enter_this_app = new MutableLiveData<>();
        gotoMainActivity = new MutableLiveData<>();
        showAlterDialog = new MutableLiveData<>();

    }

    public Boolean check_if_user_first_enter(Context context){
        setting.setValue(context.getSharedPreferences("Config", Context.MODE_PRIVATE));
        Is_user_first_enter_this_app.setValue(setting.getValue().getBoolean("FIRST", true));
        return Is_user_first_enter_this_app.getValue();
    }


    public void mark_user_entered_this_app() {
        setting.getValue().edit().putBoolean("FIRST", false).commit();
    }
    public void userAutologin(Context context) {
        //Login
        LCUser user = new LCUser();
        String UUID = Util.getUUID(context);
        user.setUsername(UUID);
        user.setPassword(UUID);
        LCUser.logIn(UUID, UUID).subscribe(new Observer<LCUser>() {
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(LCUser user) {
                // 登录成功
                LCUser.changeCurrentUser(user, true);
                gotoMainActivity.postValue(true);
            }

            public void onError(Throwable throwable) {
                // 登录失败（可能是密码错误）
                user.signUpInBackground().subscribe(new Observer<LCUser>() {
                    public void onSubscribe(Disposable disposable) {
                    }

                    public void onNext(LCUser user) {
                        // 注册成功
                        System.out.println("注册成功。objectId：" + user.getObjectId());
                        LCUser.changeCurrentUser(user, true);
                        gotoMainActivity.postValue(true);
                    }

                    public void onError(Throwable throwable) {
                        LCUser.logInAnonymously().subscribe(new Observer<LCUser>() {
                            public void onSubscribe(Disposable disposable) {
                            }

                            public void onNext(LCUser user) {
                                // user 是新的匿名用户
                                LCUser.changeCurrentUser(user, true);
                                gotoMainActivity.postValue(true);
                            }

                            public void onError(Throwable throwable) {
                                showAlterDialog.postValue(true);
                            }

                            public void onComplete() {
                            }
                        });
                    }

                    public void onComplete() {
                    }
                });
            }

            public void onComplete() {
            }
        });
    }

    public MutableLiveData<SharedPreferences> getSetting() {
        return setting;
    }

    public MutableLiveData<Boolean> getIs_user_first_enter_this_app() {
        return Is_user_first_enter_this_app;
    }

    public MutableLiveData<Boolean> getGotoMainActivity() {
        return gotoMainActivity;
    }

    public MutableLiveData<Boolean> getShowAlterDialog() {
        return showAlterDialog;
    }
}
