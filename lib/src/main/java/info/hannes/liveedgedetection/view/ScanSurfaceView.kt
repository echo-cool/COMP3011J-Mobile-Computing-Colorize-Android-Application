package info.hannes.liveedgedetection.view

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.shapes.PathShape
import android.hardware.Camera
import android.hardware.Camera.*
import android.media.AudioManager
import android.os.CountDownTimer
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import info.hannes.liveedgedetection.*
import info.hannes.liveedgedetection.utils.ScanUtils
import info.hannes.liveedgedetection.utils.configureCameraAngle
import info.hannes.liveedgedetection.utils.decodeBitmapFromByteArray
import org.opencv.core.*
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import timber.log.Timber
import java.io.IOException
import kotlin.math.abs


/**
 * This class previews the live images from the camera
 */
class ScanSurfaceView(context: Context, iScanner: IScanner, val TIME_HOLD_STILL: Long = DEFAULT_TIME_POST_PICTURE) : FrameLayout(context), SurfaceHolder.Callback {

    private var surfaceView: SurfaceView = SurfaceView(context)
    private val scanCanvasView: ScanCanvasView
    private var vWidth = 0
    private var vHeight = 0
    private var camera: Camera? = null
    private val iScanner: IScanner
    private var autoCaptureTimer: CountDownTimer? = null
    private var millisLeft = 0L
    private var isAutoCaptureScheduled = false
    private var previewSize: Camera.Size? = null
    private var isCapturing = false

    init {
        addView(surfaceView)
        scanCanvasView = ScanCanvasView(context)
        addView(scanCanvasView)
        val surfaceHolder = surfaceView.holder
        surfaceHolder.addCallback(this)
        this.iScanner = iScanner
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            requestLayout()
            openCamera()
            camera!!.setPreviewDisplay(holder)
        } catch (e: IOException) {
            Timber.e(e)
        }
    }

    private fun clearAndInvalidateCanvas() {
        scanCanvasView.clear()
        invalidateCanvas()
    }

    private fun invalidateCanvas() {
        scanCanvasView.invalidate()
    }

    private fun openCamera() {
        if (camera == null) {
            val info = CameraInfo()
            var defaultCameraId = 0
            for (i in 0 until getNumberOfCameras()) {
                getCameraInfo(i, info)
                if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                    defaultCameraId = i
                }
            }
            camera = open(defaultCameraId)
            val cameraParams = camera?.getParameters()
            val flashModes = cameraParams?.supportedFlashModes
            if (null != flashModes && flashModes.contains(Parameters.FLASH_MODE_AUTO)) {
                cameraParams.flashMode = Parameters.FLASH_MODE_AUTO
            }
            camera?.parameters = cameraParams
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (vWidth == vHeight) {
            return
        }
        if (previewSize == null)
            previewSize = ScanUtils.getOptimalPreviewSize(camera, vWidth, vHeight)
        val parameters = camera!!.parameters
        camera!!.setDisplayOrientation((context as Activity).configureCameraAngle())
        parameters.setPreviewSize(previewSize!!.width, previewSize!!.height)
        if (parameters.supportedFocusModes != null && parameters.supportedFocusModes.contains(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.focusMode = Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        } else if (parameters.supportedFocusModes != null && parameters.supportedFocusModes.contains(Parameters.FOCUS_MODE_AUTO)) {
            parameters.focusMode = Parameters.FOCUS_MODE_AUTO
        }
        val size = ScanUtils.determinePictureSize(camera, parameters.previewSize)
        size?.let {
            parameters.setPictureSize(it.width, it.height)
            parameters.pictureFormat = ImageFormat.JPEG
            camera!!.parameters = parameters
        }
        requestLayout()
        setPreviewCallback()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopPreviewAndFreeCamera()
    }

    private fun stopPreviewAndFreeCamera() {
        if (camera != null) {
            // Call stopPreview() to stop updating the preview surface.
            camera!!.stopPreview()
            camera!!.setPreviewCallback(null)
            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            camera!!.release()
            camera = null
        }
    }

    fun setPreviewCallback() {
        camera!!.startPreview()
        camera!!.setPreviewCallback(previewCallback)
    }

    private val previewCallback = PreviewCallback { data, camera ->
        if (null != camera) {
            try {
                val pictureSize = camera.parameters.previewSize
                Timber.v("onPreviewFrame - received image w=${pictureSize.width} h=${pictureSize.height}")
                val yuv = Mat(Size(pictureSize.width.toDouble(), pictureSize.height * 1.5), CvType.CV_8UC1)
                yuv.put(0, 0, data)
                val mat = Mat(Size(pictureSize.width.toDouble(), pictureSize.height.toDouble()), CvType.CV_8UC4)
                Imgproc.cvtColor(yuv, mat, Imgproc.COLOR_YUV2BGR_NV21, 4)
                yuv.release()
                val originalPreviewSize = mat.size()
                val originalPreviewArea = mat.rows() * mat.cols()
                val largestQuad = ScanUtils.detectLargestQuadrilateral(mat)
                clearAndInvalidateCanvas()
                mat.release()
                if (null != largestQuad) {
                    drawLargestRect(largestQuad.contour, largestQuad.points, originalPreviewSize, originalPreviewArea)
                } else {
                    showFindingReceiptHint()
                }
            } catch (e: Exception) {
                Timber.e(e)
                showFindingReceiptHint()
            }
        }
    }

    private fun drawLargestRect(approx: MatOfPoint2f, points: Array<Point>, stdSize: Size, previewArea: Int) {
        val path = Path()
        // Attention: axis are swapped
        val previewWidth = stdSize.height.toFloat()
        val previewHeight = stdSize.width.toFloat()
        Timber.v("previewWidth=$previewWidth previewHeight=$previewHeight")

        //Points are drawn in anticlockwise direction
        path.moveTo(previewWidth - points[0].y.toFloat(), points[0].x.toFloat())
        path.lineTo(previewWidth - points[1].y.toFloat(), points[1].x.toFloat())
        path.lineTo(previewWidth - points[2].y.toFloat(), points[2].x.toFloat())
        path.lineTo(previewWidth - points[3].y.toFloat(), points[3].x.toFloat())
        path.close()
        val area = abs(Imgproc.contourArea(approx))
        Timber.v("Contour Area=$area")
        val newBox = PathShape(path, previewWidth, previewHeight)
        val paint = Paint()
        val border = Paint()

        //Height calculated on Y axis
        var resultHeight = points[1].x - points[0].x
        val bottomHeight = points[2].x - points[3].x
        if (bottomHeight > resultHeight) resultHeight = bottomHeight

        //Width calculated on X axis
        var resultWidth = points[3].y - points[0].y
        val bottomWidth = points[2].y - points[1].y
        if (bottomWidth > resultWidth) resultWidth = bottomWidth
        Timber.v("resultWidth=$resultWidth resultHeight=$resultHeight")
        val imgDetectionPropsObj = ImageDetectionProperties(previewWidth.toDouble(), previewHeight.toDouble(), resultWidth, resultHeight,
                previewArea.toDouble(), area, points[0], points[1], points[2], points[3])
        val scanHint: ScanHint
        if (imgDetectionPropsObj.isDetectedAreaBeyondLimits) {
            scanHint = ScanHint.FIND_RECT
            cancelAutoCapture()
        } else if (imgDetectionPropsObj.isDetectedAreaBelowLimits) {
            cancelAutoCapture()
            scanHint = if (imgDetectionPropsObj.isEdgeTouching) {
                ScanHint.MOVE_AWAY
            } else {
                ScanHint.MOVE_CLOSER
            }
        } else if (imgDetectionPropsObj.isDetectedHeightAboveLimit) {
            cancelAutoCapture()
            scanHint = ScanHint.MOVE_AWAY
        } else if (imgDetectionPropsObj.isDetectedWidthAboveLimit || imgDetectionPropsObj.isDetectedAreaAboveLimit) {
            cancelAutoCapture()
            scanHint = ScanHint.MOVE_AWAY
        } else {
            if (imgDetectionPropsObj.isEdgeTouching) {
                cancelAutoCapture()
                scanHint = ScanHint.MOVE_AWAY
            } else if (imgDetectionPropsObj.isAngleNotCorrect(approx)) {
                cancelAutoCapture()
                scanHint = ScanHint.ADJUST_ANGLE
            } else {
                Timber.i("GREEN (resultWidth/resultHeight) > 4=${resultWidth / resultHeight} points[0].x == 0 && points[3].x == 0=${points[0].x}:${points[3].x} points[2].x == previewHeight && points[1].x == previewHeight=${points[2].x}:${points[1].x} previewHeight=$previewHeight")
                scanHint = ScanHint.CAPTURING_IMAGE
                clearAndInvalidateCanvas()
                if (!isAutoCaptureScheduled) {
                    scheduleAutoCapture(scanHint)
                }
            }
        }
        Timber.v("label=$scanHint preview Area 95%=${0.95 * previewArea} Preview Area 20%=${0.20 * previewArea} Area=$area")
        border.strokeWidth = 12f
        iScanner.displayHint(scanHint)
        setPaintAndBorder(scanHint, paint, border)
        scanCanvasView.clear()
        scanCanvasView.addShape(newBox, paint, border)
        invalidateCanvas()
    }

    private fun scheduleAutoCapture(scanHint: ScanHint) {
        isAutoCaptureScheduled = true
        millisLeft = 0L
        autoCaptureTimer = object : CountDownTimer(TIME_HOLD_STILL, 100) {
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished != millisLeft) {
                    millisLeft = millisUntilFinished
                }
            }

            override fun onFinish() {
                isAutoCaptureScheduled = false
                autoCapture(scanHint)
                Timber.v("$millisLeft")
            }
        }
        autoCaptureTimer?.start()
    }

    private fun autoCapture(scanHint: ScanHint) {
        if (isCapturing)
            return
        cancelAutoCapture()
        if (ScanHint.CAPTURING_IMAGE == scanHint) {
            try {
                isCapturing = true
                iScanner.displayHint(ScanHint.CAPTURING_IMAGE)
                camera!!.takePicture(mShutterCallBack, null, pictureCallback)
                camera!!.setPreviewCallback(null)
                // iScanner.displayHint(ScanHint.NO_MESSAGE);
                // clearAndInvalidateCanvas();
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun cancelAutoCapture() {
        if (isAutoCaptureScheduled) {
            isAutoCaptureScheduled = false
            if (autoCaptureTimer != null) {
                autoCaptureTimer!!.cancel()
            }
        }
    }

    private fun showFindingReceiptHint() {
        iScanner.displayHint(ScanHint.FIND_RECT)
        clearAndInvalidateCanvas()
    }

    private fun setPaintAndBorder(scanHint: ScanHint, paint: Paint, border: Paint) {
        var paintColor = 0
        var borderColor = 0
        when (scanHint) {
            ScanHint.MOVE_CLOSER, ScanHint.MOVE_AWAY, ScanHint.ADJUST_ANGLE -> {
                paintColor = Color.argb(30, 255, 38, 0)
                borderColor = Color.rgb(255, 38, 0)
            }
            ScanHint.FIND_RECT -> {
                paintColor = Color.argb(0, 0, 0, 0)
                borderColor = Color.argb(0, 0, 0, 0)
            }
            ScanHint.CAPTURING_IMAGE -> {
                paintColor = Color.argb(30, 38, 216, 76)
                borderColor = Color.rgb(38, 216, 76)
            }
            ScanHint.NO_MESSAGE -> Unit
        }
        paint.color = paintColor
        border.color = borderColor
    }

    private val pictureCallback = PictureCallback { data, camera ->
        camera.stopPreview()
        iScanner.displayHint(ScanHint.NO_MESSAGE)
        clearAndInvalidateCanvas()
        var bitmap = data.decodeBitmapFromByteArray(ScanConstants.HIGHER_SAMPLING_THRESHOLD, ScanConstants.HIGHER_SAMPLING_THRESHOLD)
        val matrix = Matrix()
        matrix.postRotate(90f)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        iScanner.onPictureClicked(bitmap)
        postDelayed({ isCapturing = false }, TIME_POST_PICTURE)
    }
    private val mShutterCallBack = ShutterCallback {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead of stretching it.
        vWidth = View.resolveSize(suggestedMinimumWidth, widthMeasureSpec)
        vHeight = View.resolveSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(vWidth, vHeight)
        previewSize = ScanUtils.getOptimalPreviewSize(camera, vWidth, vHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount > 0) {
            val width = r - l
            val height = b - t
            var previewWidth = width
            var previewHeight = height
            if (previewSize != null) {
                previewWidth = previewSize!!.width
                previewHeight = previewSize!!.height
                val displayOrientation = (context as Activity).configureCameraAngle()
                if (displayOrientation == 90 || displayOrientation == 270) {
                    previewWidth = previewSize!!.height
                    previewHeight = previewSize!!.width
                }
                Timber.d("previewWidth:$previewWidth previewHeight:$previewHeight")
            }
            val nW: Int
            val nH: Int
            val top: Int
            val left: Int
            val scale = 1.0f

            // Center the child SurfaceView within the parent.
            if (width * previewHeight < height * previewWidth) {
                Timber.d("center horizontally")
                val scaledChildWidth = (previewWidth * height / previewHeight * scale).toInt()
                nW = (width + scaledChildWidth) / 2
                nH = (height * scale).toInt()
                top = 0
                left = (width - scaledChildWidth) / 2
            } else {
                Timber.d("center vertically")
                val scaledChildHeight = (previewHeight * width / previewWidth * scale).toInt()
                nW = (width * scale).toInt()
                nH = (height + scaledChildHeight) / 2
                top = (height - scaledChildHeight) / 2
                left = 0
            }
            surfaceView.layout(left, top, nW, nH)
            scanCanvasView.layout(left, top, nW, nH)
            Timber.d("left=$left top=$top bottom=$nH")
        }
    }

    companion object {
        private const val TIME_POST_PICTURE = 1900L
        const val DEFAULT_TIME_POST_PICTURE = 2000L
    }
}