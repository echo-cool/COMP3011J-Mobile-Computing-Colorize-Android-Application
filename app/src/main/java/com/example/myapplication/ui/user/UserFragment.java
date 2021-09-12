package com.example.myapplication.ui.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.ImageUtil.GlideEngine;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSlideshowBinding;
import com.example.myapplication.databinding.FragmentUserBinding;
import com.example.myapplication.liveedgedetection.activity.ScanActivity;
import com.example.myapplication.ui.image_upload.ImageUploadActivityView;
import com.example.myapplication.ui.slideshow.SlideshowViewModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCUser;
import io.reactivex.disposables.Disposable;

public class UserFragment extends Fragment {
    private UserFragmentViewModel userFragmentViewModel;
    private FragmentUserBinding binding;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userFragmentViewModel = new ViewModelProvider(this).get(UserFragmentViewModel.class);
        binding = FragmentUserBinding.inflate(inflater, container, false);
        root = binding.getRoot();


        Fade slideTracition = new Fade();
        slideTracition.setDuration(getResources().getInteger(R.integer.config_navAnimTime));
        this.setEnterTransition(slideTracition);
        this.setExitTransition(slideTracition);

        userFragmentViewModel.getBalance().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double d) {
                binding.BalanceValue.setText(String.valueOf(d));
            }
        });
        userFragmentViewModel.getRemainingCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.RemainingValue.setText(String.valueOf(integer));
            }
        });
        userFragmentViewModel.getProcessedCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.CountValue.setText(String.valueOf(integer));
            }
        });
        userFragmentViewModel.getUsername().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.usernameText.setText(s);
            }
        });
        binding.TopUpBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LCUser currentUser = LCUser.getCurrentUser();
                currentUser.put("Balance", userFragmentViewModel.getBalance().getValue() + 5);
                currentUser.saveInBackground().subscribe(new io.reactivex.Observer<LCObject>() {
                    public void onSubscribe(Disposable disposable) {}
                    public void onNext(LCObject account) {
                        System.out.println("当前余额为：" + account.getDouble("Balance"));
                        userFragmentViewModel.setBalance(account.getDouble("Balance"));
                        Toast.makeText(getContext(), "New balance is: " + account.getDouble("Balance"), Toast.LENGTH_SHORT).show();
                    }
                    public void onError(Throwable throwable) {
                        Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();

                    }
                    public void onComplete() {}
                });
            }
        });
        binding.AddRemaining5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LCUser currentUser = LCUser.getCurrentUser();
                if(userFragmentViewModel.getBalance().getValue() - 5 > 0) {
                    currentUser.put("Balance", userFragmentViewModel.getBalance().getValue() - 5);
                    currentUser.put("RemainingCount", userFragmentViewModel.getRemainingCount().getValue() + 5);
                    currentUser.saveInBackground().subscribe(new io.reactivex.Observer<LCObject>() {
                        public void onSubscribe(Disposable disposable) {
                        }

                        public void onNext(LCObject account) {
                            System.out.println("当前余额为：" + account.getDouble("Balance"));
                            userFragmentViewModel.setBalance(account.getDouble("Balance"));
                            userFragmentViewModel.setRemainingCount(account.getInt("RemainingCount"));

                        }

                        public void onError(Throwable throwable) {
                            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                        }

                        public void onComplete() {
                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), "No enough balance !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.addRemaining10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LCUser currentUser = LCUser.getCurrentUser();
                if(userFragmentViewModel.getBalance().getValue() - 10 > 0) {
                    currentUser.put("Balance", userFragmentViewModel.getBalance().getValue() - 10);
                    currentUser.put("RemainingCount", userFragmentViewModel.getRemainingCount().getValue() + 10);
                    currentUser.saveInBackground().subscribe(new io.reactivex.Observer<LCObject>() {
                        public void onSubscribe(Disposable disposable) {
                        }

                        public void onNext(LCObject account) {
                            System.out.println("当前余额为：" + account.getDouble("Balance"));
                            userFragmentViewModel.setBalance(account.getDouble("Balance"));
                            userFragmentViewModel.setRemainingCount(account.getInt("RemainingCount"));
                        }

                        public void onError(Throwable throwable) {
                            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                        }

                        public void onComplete() {
                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), "No enough balance !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        updateUserProfile();


        return root;
    }

    private void updateUserProfile() {
        LCUser currentUser = LCUser.getCurrentUser();
        currentUser.fetchInBackground().subscribe(new io.reactivex.Observer<LCObject>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(LCObject user) {
                if (currentUser != null) {
                    userFragmentViewModel.setUsername(currentUser.getUsername());
                    userFragmentViewModel.setBalance(currentUser.getDouble("Balance"));
                    userFragmentViewModel.setProcessedCount(currentUser.getInt("ProcessedCount"));
                    userFragmentViewModel.setRemainingCount(currentUser.getInt("RemainingCount"));

                } else {

                }
            }
            public void onError(Throwable throwable) {}
            public void onComplete() {}
        });



    }
}
