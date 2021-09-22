package com.echo.colorizeit.ui.a_camera_activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.camerakit.CameraKitView;
import com.example.myapplication.databinding.CamerakitViewBinding;


public class CameraKitActivity extends AppCompatActivity {

    private CamerakitViewBinding binding;
    private CameraKitView cameraKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CamerakitViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cameraKitView = binding.camera;
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
