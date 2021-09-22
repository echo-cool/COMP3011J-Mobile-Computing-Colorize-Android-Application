package com.example.myapplication.ui.f_user_view;

import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentUserBinding;

import cn.leancloud.LCObject;
import cn.leancloud.LCUser;
import io.reactivex.disposables.Disposable;

public class UserFragment extends Fragment {
    private UserFragmentViewModel model;
    private FragmentUserBinding binding;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        model = new ViewModelProvider(this).get(UserFragmentViewModel.class);
        binding = FragmentUserBinding.inflate(inflater, container, false);
        root = binding.getRoot();


        Fade slideTracition = new Fade();
        slideTracition.setDuration(getResources().getInteger(R.integer.config_navAnimTime));
        this.setEnterTransition(slideTracition);
        this.setExitTransition(slideTracition);

        model.getBalance().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double d) {
                binding.BalanceValue.setText(String.valueOf(d));
            }
        });
        model.getRemainingCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.RemainingValue.setText(String.valueOf(integer));
            }
        });
        model.getProcessedCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.CountValue.setText(String.valueOf(integer));
            }
        });
        model.getUsername().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.usernameText.setText(s);
            }
        });
        binding.TopUpBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LCUser currentUser = LCUser.getCurrentUser();
                currentUser.put("Balance", model.getBalance().getValue() + 5);
                currentUser.saveInBackground().subscribe(new io.reactivex.Observer<LCObject>() {
                    public void onSubscribe(Disposable disposable) {}
                    public void onNext(LCObject account) {
                        System.out.println("当前余额为：" + account.getDouble("Balance"));
                        model.setBalance(account.getDouble("Balance"));
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
                if(model.getBalance().getValue() - 5 > 0) {
                    currentUser.put("Balance", model.getBalance().getValue() - 5);
                    currentUser.put("RemainingCount", model.getRemainingCount().getValue() + 5);
                    currentUser.saveInBackground().subscribe(new io.reactivex.Observer<LCObject>() {
                        public void onSubscribe(Disposable disposable) {
                        }

                        public void onNext(LCObject account) {
                            System.out.println("当前余额为：" + account.getDouble("Balance"));
                            model.setBalance(account.getDouble("Balance"));
                            model.setRemainingCount(account.getInt("RemainingCount"));

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
                if(model.getBalance().getValue() - 10 > 0) {
                    currentUser.put("Balance", model.getBalance().getValue() - 10);
                    currentUser.put("RemainingCount", model.getRemainingCount().getValue() + 10);
                    currentUser.saveInBackground().subscribe(new io.reactivex.Observer<LCObject>() {
                        public void onSubscribe(Disposable disposable) {
                        }

                        public void onNext(LCObject account) {
                            System.out.println("当前余额为：" + account.getDouble("Balance"));
                            model.setBalance(account.getDouble("Balance"));
                            model.setRemainingCount(account.getInt("RemainingCount"));
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
        model.updateUserProfile();


        return root;
    }

}
