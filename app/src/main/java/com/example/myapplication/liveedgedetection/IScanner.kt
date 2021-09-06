package com.example.myapplication.liveedgedetection

import android.graphics.Bitmap

/**
 * Interface between activity and surface view
 */
interface IScanner {
    fun displayHint(scanHint: ScanHint)
    fun onPictureClicked(bitmap: Bitmap)
}