package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.OpenScreenBinding;

public class OpenScreenActivity extends Activity {
    OpenScreenBinding binding;
    Activity _this=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        Animation animation_fade = AnimationUtils.loadAnimation(this, R.anim.anim_open_screen);
        Animation animation_scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        binding = OpenScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        binding.OpenScreenImageVIew.startAnimation(animation_fade);
//        binding.textView2.startAnimation(animation_fade);
//        binding.textView4.startAnimation(animation_fade);
        binding.animationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
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

                new Handler().postDelayed(()->{
                    _this.startActivity(new Intent(_this, MainActivity.class));
                    _this.finish();
                },1200);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });

    }

    public void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
