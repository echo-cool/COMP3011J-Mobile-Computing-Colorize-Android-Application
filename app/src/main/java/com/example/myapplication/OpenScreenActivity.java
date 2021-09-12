package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.OpenScreenBinding;

import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class OpenScreenActivity extends Activity {
    OpenScreenBinding binding;
    Activity _this = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences setting = getSharedPreferences("Config", Context.MODE_PRIVATE);
        Boolean user_first = setting.getBoolean("FIRST", true);
        makeFullScreen();
        Animation animation_fade = AnimationUtils.loadAnimation(this, R.anim.anim_open_screen);
        Animation animation_scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        binding = OpenScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (user_first) {//第一次
            setting.edit().putBoolean("FIRST", false).commit();
//            Toast.makeText(this, "Is the first time open This APP", Toast.LENGTH_LONG).show();
            binding.animationView.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    userAutologin();
                    binding.animationView.setVisibility(View.INVISIBLE);
                    binding.textView2.setVisibility(View.VISIBLE);
                    binding.background.setVisibility(View.VISIBLE);
                    binding.OpenScreenImageVIew.setVisibility(View.VISIBLE);
                    binding.textView4.setVisibility(View.VISIBLE);
                    binding.spinKit.setVisibility(View.VISIBLE);

                    YoYo.with(Techniques.RollIn)
                            .duration(800)
                            .repeat(0)
                            .playOn(binding.OpenScreenImageVIew);
                    YoYo.with(Techniques.StandUp)
                            .duration(800)
                            .repeat(0)
                            .playOn(binding.textView2);
                    YoYo.with(Techniques.StandUp)
                            .duration(800)
                            .repeat(0)
                            .playOn(binding.textView4);
                    binding.spinKit.startAnimation(animation_fade);
                    binding.background.startAnimation(animation_fade);

                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            });
        } else {
            userAutologin();
//            Toast.makeText(this, "Not the first time", Toast.LENGTH_LONG).show();
            binding.animationView.setVisibility(View.INVISIBLE);
            binding.textView2.setVisibility(View.VISIBLE);
            binding.background.setVisibility(View.VISIBLE);
            binding.OpenScreenImageVIew.setVisibility(View.VISIBLE);
            binding.textView4.setVisibility(View.VISIBLE);
            binding.spinKit.setVisibility(View.VISIBLE);

            YoYo.with(Techniques.RollIn)
                    .duration(800)
                    .repeat(0)
                    .playOn(binding.OpenScreenImageVIew);
            YoYo.with(Techniques.StandUp)
                    .duration(800)
                    .repeat(0)
                    .playOn(binding.textView2);
            YoYo.with(Techniques.StandUp)
                    .duration(800)
                    .repeat(0)
                    .playOn(binding.textView4);
            binding.spinKit.startAnimation(animation_fade);
            binding.background.startAnimation(animation_fade);

//            if (currentUser != null) {
//                new Handler().postDelayed(() -> {
//                    _this.startActivity(new Intent(_this, MainActivity.class));
//                    _this.finish();
//                }, 1200);
//            }
//            else{
//                showAlterDialog();
//
//            }
        }

//        binding.OpenScreenImageVIew.startAnimation(animation_fade);
//        binding.textView2.startAnimation(animation_fade);
//        binding.textView4.startAnimation(animation_fade);
    }
    private void userAutologin(){
        //Login
        LCUser user = new LCUser();
        String UUID = Util.getUUID(getBaseContext());
        user.setUsername(UUID);
        user.setPassword(UUID);
        LCUser.logIn(UUID, UUID).subscribe(new Observer<LCUser>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(LCUser user) {
                // 登录成功
                LCUser.changeCurrentUser(user, true);
                gotoMainActivity();
            }
            public void onError(Throwable throwable) {
                // 登录失败（可能是密码错误）
                user.signUpInBackground().subscribe(new Observer<LCUser>() {
                    public void onSubscribe(Disposable disposable) {}
                    public void onNext(LCUser user) {
                        // 注册成功
                        System.out.println("注册成功。objectId：" + user.getObjectId());
                        LCUser.changeCurrentUser(user, true);
                        gotoMainActivity();
                    }
                    public void onError(Throwable throwable) {
                        LCUser.logInAnonymously().subscribe(new Observer<LCUser>() {
                            public void onSubscribe(Disposable disposable) {}
                            public void onNext(LCUser user) {
                                // user 是新的匿名用户
                                LCUser.changeCurrentUser(user, true);
                                gotoMainActivity();
                            }
                            public void onError(Throwable throwable) {
                                showAlterDialog();
                            }
                            public void onComplete() {}
                        });
                    }
                    public void onComplete() {}
                });
            }
            public void onComplete() {}
        });
    }
    private void showAlterDialog(){
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(this);

        alterDiaglog.setTitle("No Internet");//文字
        alterDiaglog.setMessage("Please connect to Internet to continue.");//提示消息

        alterDiaglog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _this.finish();
            }
        });
        alterDiaglog.show();
    }

    private void gotoMainActivity(){
        new Handler().postDelayed(() -> {
            _this.startActivity(new Intent(_this, MainActivity.class));
            _this.finish();
        }, 1200);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
