package info.hannes.liveedgedetection.utils

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.Surface
import kotlin.math.roundToInt


fun Activity.configureCameraAngle(): Int {
    val cameraOrientationAngle: Int
    val display = this.windowManager.defaultDisplay
    cameraOrientationAngle = when (display.rotation) {
        Surface.ROTATION_90 -> 0
        Surface.ROTATION_180 -> 270
        Surface.ROTATION_270 -> 180
        Surface.ROTATION_0 -> 90
        else -> 90
    }
    return cameraOrientationAngle
}

/*
     * This method converts the dp value to px
     * @param context context
     * @param dp value in dp
     * @return px value
     */
fun Context.dp2px(dp: Float): Int {
    val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.resources.displayMetrics)
    return px.roundToInt()
}