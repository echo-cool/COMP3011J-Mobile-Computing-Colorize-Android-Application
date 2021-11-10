package com.echo.colorizeit.ui.a_open_screen_activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.echo.colorizeit.MainActivity;
import com.echo.colorizeit.ui.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.OpenScreenBinding;

/**
 * @author Wang Yuyang
 * @date 2021-09-22 13:52:43
 */
public class OpenScreenActivity extends BaseActivity {
    private OpenScreenBinding binding;
    private Activity _this = this;
    private OpenScreenViewModel model;
    private Animation anim_play_view_fade_out;
    private Animation openScreen_element_fade_in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(OpenScreenViewModel.class);
        anim_play_view_fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        openScreen_element_fade_in = AnimationUtils.loadAnimation(this, R.anim.anim_open_screen);
        makeFullScreen();


        anim_play_view_fade_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
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
                binding.spinKit.startAnimation(openScreen_element_fade_in);
                binding.background.startAnimation(openScreen_element_fade_in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
        binding = OpenScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.animationView.enableMergePathsForKitKatAndAbove(true);

        if (model.check_if_user_first_enter(this)) {//第一次
            model.mark_user_entered_this_app();

//            Toast.makeText(this, "Is the first time open This APP", Toast.LENGTH_LONG).show();
            binding.animationView.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    model.userAutologin(_this);
                    binding.animationView.startAnimation(anim_play_view_fade_out);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            });
        } else {
            model.userAutologin(_this);
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
            binding.spinKit.startAnimation(openScreen_element_fade_in);
            binding.background.startAnimation(openScreen_element_fade_in);

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
        model.getGotoMainActivity().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                    gotoMainActivity();
            }
        });
        model.getShowAlterDialog().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                    showAlterDialog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(model.getShowAlterDialog().getValue())
            showAlterDialog();
    }

    private void showAlterDialog() {
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(this);

        alterDiaglog.setTitle("No Internet");//文字
        alterDiaglog.setMessage("Please connect to Internet to continue.");//提示消息
        alterDiaglog.setPositiveButton("Play a Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alterDiaglog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _this.finish();
            }
        });
        alterDiaglog.show();
    }

    private void gotoMainActivity() {
//        new Handler().postDelayed(() -> {
//            Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_open_screen_fadeout);
//            animation.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    binding.forground.setVisibility(View.VISIBLE);
//                    binding.OpenScreenImageVIew.setBackgroundColor(Color.BLACK);
//                    binding.background.setBackgroundColor(Color.BLACK);
//                    binding.openscreenBase.setBackgroundColor(Color.BLACK);
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//            binding.forground.startAnimation(animation);
//        }, 1000);
        new Handler().postDelayed(() -> {
            _this.startActivity(new Intent(_this, MainActivity.class));
            _this.finish();
        }, 1500);
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
