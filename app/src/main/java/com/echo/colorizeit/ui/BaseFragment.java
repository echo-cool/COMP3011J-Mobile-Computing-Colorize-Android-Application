package com.echo.colorizeit.ui;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

/**
 * @author Wang Yuyang
 * @date 2021-09-22 13:52:43
 */
public class BaseFragment extends Fragment {
    public static final int READ_WRITE_STORAGE = 52;
    private ProgressDialog mProgressDialog;


    protected void showLoading(@NonNull String message) {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    protected void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected void showSnackbar(@NonNull String message) {
        View view = getActivity().findViewById(android.R.id.content);
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
