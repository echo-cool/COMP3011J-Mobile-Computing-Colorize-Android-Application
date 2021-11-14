package com.echo.colorizeit.ui.f_user_view;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.echo.colorizeit.ui.a_login_activity.LoginViewActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentUserBinding;

import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCUser;
import io.reactivex.disposables.Disposable;

/**
 * @author Wang Yuyang
 * @date 2021-09-22 13:52:43
 */
public class UserFragment extends Fragment implements PurchasesUpdatedListener {
    private UserFragmentViewModel model;

    private FragmentUserBinding binding;
    private UserFragment _this = this;
    private View root;
    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            // To be implemented in a later section.
        }
    };

    private BillingClient billingClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        model = new ViewModelProvider(this).get(UserFragmentViewModel.class);
        binding = FragmentUserBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        billingClient = BillingClient.newBuilder(getContext())
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

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
//                List<String> skuList = new ArrayList<>();
//                skuList.add("0");
//                skuList.add("1");
//                skuList.add("2");
//                skuList.add("3");
//                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
//                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
//                billingClient.startConnection(new BillingClientStateListener() {
//                    @Override
//                    public void onBillingSetupFinished(BillingResult billingResult) {
//                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                            // The BillingClient is ready. You can query purchases here.
//                            System.out.println("The BillingClient is ready. You can query purchases here.");
//                            billingClient.querySkuDetailsAsync(params.build(),
//                                    new SkuDetailsResponseListener() {
//                                        @Override
//                                        public void onSkuDetailsResponse(BillingResult billingResult,
//                                                                         List<SkuDetails> skuDetailsList) {
//                                            // Process the result.
//                                            System.out.println(billingResult.getDebugMessage());
//                                            System.out.println(skuDetailsList);
//                                        }
//                                    });
//                        }
//                    }
//
//                    @Override
//                    public void onBillingServiceDisconnected() {
//                        // Try to restart the connection on the next request to
//                        // Google Play by calling the startConnection() method.
//                        System.out.println("Disconnected");
//                    }
//                });


// Handle the result.

// SkuDetails object obtained above.
//                SkuDetails skuDetails = ...;
//
//                BillingFlowParams purchaseParams =
//                        BillingFlowParams.newBuilder()
//                                .setSkuDetails(skuDetails)
//                                .build();
//
//                BillingClient mBillingClient;
//                mBillingClient.launchBillingFlow(mActivity, purchaseParams);

// Purchase is handled in onPurchasesUpdated illustrated in the previous section.

                LCUser currentUser = LCUser.getCurrentUser();
                currentUser.put("Balance", model.getBalance().getValue() + 5);
                currentUser.saveInBackground().subscribe(new io.reactivex.Observer<LCObject>() {
                    public void onSubscribe(Disposable disposable) {
                    }

                    public void onNext(LCObject account) {
                        System.out.println("当前余额为：" + account.getDouble("Balance"));
                        model.setBalance(account.getDouble("Balance"));
                        Toast.makeText(getContext(), "New balance is: " + account.getDouble("Balance"), Toast.LENGTH_SHORT).show();
                    }

                    public void onError(Throwable throwable) {
                        Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();

                    }

                    public void onComplete() {
                    }
                });
            }
        });
        binding.AddRemaining5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LCUser currentUser = LCUser.getCurrentUser();
                if (model.getBalance().getValue() - 5 >= 0) {
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
                } else {
                    Toast.makeText(getContext(), "No enough balance !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.addRemaining10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LCUser currentUser = LCUser.getCurrentUser();
                if (model.getBalance().getValue() - 10 > 0) {
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
                } else {
                    Toast.makeText(getContext(), "No enough balance !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (LCUser.currentUser() == null) {
            Intent intent = new Intent(getActivity(), LoginViewActivity.class);
            startActivity(intent);
        } else {
            model.updateUserProfile();
        }

        binding.LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginViewActivity.class);
                startActivity(intent);

            }
        });
        if (LCUser.currentUser() != null)
            if (LCUser.currentUser().isAnonymous()) {
                binding.usernameText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), LoginViewActivity.class);
                        startActivity(intent);
                    }
                });
                binding.userAvatars.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), LoginViewActivity.class);
                        startActivity(intent);
                    }
                });
            }
        if (LCUser.currentUser() != null) {
            if (LCUser.currentUser().isAnonymous()) {
                binding.logoutButton.setVisibility(View.GONE);
            } else {
                binding.logoutButton.setVisibility(View.VISIBLE);
            }
        }
        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LCUser.currentUser() != null) {
                    LCUser.logOut();
                    Intent intent = new Intent(getActivity(), LoginViewActivity.class);
                    startActivity(intent);
                }
            }
        });


        return root;
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        // Logic from onActivityResult should be moved here.
    }

    @Override
    public void onStart() {
        super.onStart();
        if (LCUser.currentUser() == null) {
            Intent intent = new Intent(getActivity(), LoginViewActivity.class);
            startActivity(intent);
        }

    }
}
