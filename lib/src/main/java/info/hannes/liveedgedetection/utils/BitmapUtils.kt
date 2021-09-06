package info.hannes.liveedgedetection.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.PointF
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.utils.Converters
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.*

fun Bitmap.enhanceReceipt(topLeft: Point, topRight: Point, bottomLeft: Point, bottomRight: Point): Bitmap {
    var resultWidth = (topRight.x - topLeft.x).toInt()
    val bottomWidth = (bottomRight.x - bottomLeft.x).toInt()
    if (bottomWidth > resultWidth)
        resultWidth = bottomWidth
    var resultHeight = (bottomLeft.y - topLeft.y).toInt()
    val bottomHeight = (bottomRight.y - topRight.y).toInt()
    if (bottomHeight > resultHeight)
        resultHeight = bottomHeight
    val inputMat = Mat(this.height, this.height, CvType.CV_8UC1)
    Utils.bitmapToMat(this, inputMat)
    val outputMat = Mat(resultWidth, resultHeight, CvType.CV_8UC1)
    val source: MutableList<Point> = ArrayList()
    source.add(topLeft)
    source.add(topRight)
    source.add(bottomLeft)
    source.add(bottomRight)
    val startM = Converters.vector_Point2f_to_Mat(source)
    val ocvPOut1 = Point(0.0, 0.0)
    val ocvPOut2 = Point(resultWidth.toDouble(), 0.0)
    val ocvPOut3 = Point(0.0, resultHeight.toDouble())
    val ocvPOut4 = Point(resultWidth.toDouble(), resultHeight.toDouble())
    val dest: MutableList<Point> = ArrayList()
    dest.add(ocvPOut1)
    dest.add(ocvPOut2)
    dest.add(ocvPOut3)
    dest.add(ocvPOut4)
    val endM = Converters.vector_Point2f_to_Mat(dest)
    val perspectiveTransform = Imgproc.getPerspectiveTransform(startM, endM)
    Imgproc.warpPerspective(inputMat, outputMat, perspectiveTransform, Size(resultWidth.toDouble(), resultHeight.toDouble()))
    val output = Bitmap.createBitmap(resultWidth, resultHeight, Bitmap.Config.ARGB_8888)
    Utils.matToBitmap(outputMat, output)
    return output
}

fun Bitmap.getPolygonDefaultPoints(): ArrayList<PointF> {
    val points: ArrayList<PointF> = ArrayList()
    points.add(PointF(this.width * 0.14f, this.height.toFloat() * 0.13f))
    points.add(PointF(this.width * 0.84f, this.height.toFloat() * 0.13f))
    points.add(PointF(this.width * 0.14f, this.height.toFloat() * 0.83f))
    points.add(PointF(this.width * 0.84f, this.height.toFloat() * 0.83f))
    return points
}

fun Bitmap.resizeToScreenContentSize(newWidth: Int, newHeight: Int): Bitmap? {
    val width = this.width
    val height = this.height
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    // CREATE A MATRIX FOR THE MANIPULATION
    val matrix = Matrix()
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight)

    // "RECREATE" THE NEW BITMAP
    val resizedBitmap = Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
    this.recycle()
    return resizedBitmap
}

fun Bitmap.resize(maxWidth: Int, maxHeight: Int): Bitmap {
    var image = this
    return if (maxHeight > 0 && maxWidth > 0) {
        val width = image.width
        val height = image.height
        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
        var finalWidth = maxWidth
        var finalHeight = maxHeight
        if (ratioMax > 1) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
        }
        image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
        image
    } else {
        image
    }
}

@Suppress("unused")
fun ByteArray.loadEfficientBitmap(width: Int, height: Int): Bitmap? {
    val bmp: Bitmap

    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeByteArray(this, 0, this.size, options)

    // Calculate inSampleSize
    options.inSampleSize = options.calculateInSampleSize(width, height)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    bmp = BitmapFactory.decodeByteArray(this, 0, this.size, options)
    return bmp
}

fun ByteArray.decodeBitmapFromByteArray(reqWidth: Int, reqHeight: Int): Bitmap {
    // Raw height and width of image
    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeByteArray(this, 0, this.size, options)

    // Calculate inSampleSize
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    options.inSampleSize = inSampleSize

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeByteArray(this, 0, this.size, options)
}

private fun BitmapFactory.Options.calculateInSampleSize(reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val height = this.outHeight
    val width = this.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

fun Bitmap.saveToExternalMemory(fileDirectory: String, fileName: String, quality: Int): Pair<String, String> {
    val path = File(fileDirectory, fileName)
    try {
        val fileOutputStream = FileOutputStream(path)
        //Compress method used on the Bitmap object to write  image to output stream
        this.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
        fileOutputStream.close()
    } catch (e: Exception) {
        Timber.e(e)
    }
    return Pair(File(fileDirectory).absolutePath, fileName)
}

fun Bitmap.saveToInternalMemory(fileDirectory: String, fileName: String, context: Context, quality: Int): Pair<String, String> {
    val directory = context.getBaseDirectoryFromPathString(fileDirectory)
    val path = File(directory, fileName)
    try {
        val fileOutputStream = FileOutputStream(path)
        //Compress method used on the Bitmap object to write  image to output stream
        this.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
        fileOutputStream.close()
    } catch (e: Exception) {
        Timber.e(e)
    }
    return Pair(directory.absolutePath, fileName)
}

fun Bitmap.rotate(degrees: Float): Bitmap =
        Bitmap.createBitmap(this, 0, 0, width, height, Matrix().apply { postRotate(degrees) }, true)
