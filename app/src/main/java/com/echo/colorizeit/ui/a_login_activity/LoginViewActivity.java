package com.echo.colorizeit.ui.a_login_activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.echo.colorizeit.MainActivity;
import com.echo.colorizeit.ui.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.LoginMainBinding;

import java.util.Calendar;
import java.util.regex.Pattern;

import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Author: WangYuyang
 * @Date: 2021/11/1-13:29
 * @Project: My Application
 * @Package: com.echo.colorizeit.ui.a_login_activity
 * @Description:
 **/
public class LoginViewActivity extends BaseActivity {
    private LoginMainBinding binding;
    private LoginViewModel model;
    private LoginViewActivity _this = this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginMainBinding.inflate(getLayoutInflater());
        model = new ViewModelProvider(this).get(LoginViewModel.class);
        makeFullScreen();
        setContentView(binding.getRoot());


        Calendar c = Calendar.getInstance();
        int h = c.get(Calendar.HOUR_OF_DAY);
        System.out.println(h);
        if(h >= 12 && h <=17 ){
            binding.textView6.setText("Afternoon");
        }
        else if(h >= 18 || h <= 4){
            binding.textView6.setText("Evening");
        }
        else{
            binding.textView6.setText("Morning");

        }

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.loginEmailInput.getText().toString();
                String password = binding.loginPasswordInput.getText().toString();
                if (!validateInput(username, password)) {
                    showSnackbar("Input is invalid, please check your input.");
                    return;
                }
                LCUser.logIn(username, password).subscribe(new Observer<LCUser>() {
                    public void onSubscribe(Disposable disposable) {
                    }

                    public void onNext(LCUser user) {
                        // 登录成功
                        LCUser.changeCurrentUser(user, true);
                        showSnackbar("Login success !");
                        Intent intent = new Intent(_this, MainActivity.class);
                        startActivity(intent);
                    }

                    public void onError(Throwable throwable) {
                        // 登录失败（可能是密码错误）
                        showSnackbar(throwable.getMessage());
                    }

                    public void onComplete() {
                    }
                });

            }
        });

        binding.loginEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", s.toString())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.loginEmailInput.setTextColor(getColor(R.color.white));
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.loginEmailInput.setTextColor(getColor(R.color.holo_red_dark));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.loginPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 6) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.loginPasswordInput.setTextColor(getColor(R.color.white));
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.loginPasswordInput.setTextColor(getColor(R.color.holo_red_dark));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String username = binding.loginEmailInput.getText().toString();
                String password = binding.loginPasswordInput.getText().toString();

                if (!validateInput(username, password)) {
                    showSnackbar("Input is invalid, please check your input.");
                    return;
                }

                LCUser currentUser = new LCUser();
                currentUser.setUsername(username);
                currentUser.setPassword(password);

                currentUser.signUpInBackground().subscribe(new Observer<LCUser>() {
                    public void onSubscribe(Disposable disposable) {
                    }

                    public void onNext(LCUser user) {
                        LCUser.changeCurrentUser(user, true);
                        showSnackbar("Login success !");
                        Intent intent = new Intent(_this, MainActivity.class);
                        startActivity(intent);
                    }

                    public void onError(Throwable throwable) {
                        // 注册失败（通常是因为用户名已被使用）
                        showSnackbar(throwable.getMessage());
                    }

                    public void onComplete() {
                    }
                });

            }


        });

    }

    private Boolean validateInput(String username, String password) {
        if (Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", username)) {
            if (password.length() >= 6) {
                return true;
            }
        }
        return false;

    }

    public void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
