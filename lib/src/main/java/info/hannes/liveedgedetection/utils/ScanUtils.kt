package info.hannes.liveedgedetection.utils

import android.graphics.PointF
import android.hardware.Camera
import info.hannes.liveedgedetection.ScanConstants
import info.hannes.liveedgedetection.view.Quadrilateral
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import timber.log.Timber
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

object ScanUtils {
    private fun compareFloats(left: Double, right: Double): Boolean {
        val epsilon = 0.00000001
        return abs(left - right) < epsilon
    }

    fun determinePictureSize(camera: Camera?, previewSize: Camera.Size): Camera.Size? {
        if (camera == null)
            return null
        val cameraParams = camera.parameters
        val pictureSizeList = cameraParams.supportedPictureSizes
        pictureSizeList.sortWith { size1: Camera.Size, size2: Camera.Size ->
            val h1 = sqrt((size1.width * size1.width + size1.height * size1.height).toDouble())
            val h2 = sqrt((size2.width * size2.width + size2.height * size2.height).toDouble())
            h2.compareTo(h1)
        }
        var retSize: Camera.Size? = null

        // if the preview size is not supported as a picture size
        val reqRatio = previewSize.width.toFloat() / previewSize.height
        var curRatio: Float
        var deltaRatio: Float
        var deltaRatioMin = Float.MAX_VALUE
        for (size in pictureSizeList) {
            curRatio = size.width.toFloat() / size.height
            deltaRatio = abs(reqRatio - curRatio)
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio
                retSize = size
            }
            if (compareFloats(deltaRatio.toDouble(), 0.0)) {
                break
            }
        }
        return retSize
    }

    fun getOptimalPreviewSize(camera: Camera?, w: Int, h: Int): Camera.Size? {
        if (camera == null) return null
        val targetRatio = h.toDouble() / w
        val cameraParams = camera.parameters
        val previewSizeList = cameraParams.supportedPreviewSizes
        previewSizeList.sortWith { size1: Camera.Size, size2: Camera.Size ->
            val ratio1 = size1.width.toDouble() / size1.height
            val ratio2 = size2.width.toDouble() / size2.height
            val ratioDiff1 = Math.abs(ratio1 - targetRatio)
            val ratioDiff2 = Math.abs(ratio2 - targetRatio)
            if (compareFloats(ratioDiff1, ratioDiff2)) {
                val h1 = sqrt((size1.width * size1.width + size1.height * size1.height).toDouble())
                val h2 = sqrt((size2.width * size2.width + size2.height * size2.height).toDouble())
                h2.compareTo(h1)
            }
            ratioDiff1.compareTo(ratioDiff2)
        }
        return previewSizeList[0]
    }

    fun getMaxCosine(maxCosine: Double, approxPoints: Array<Point>): Double {
        var internalMaxCosine = maxCosine
        for (i in 2..4) {
            val cosine = Math.abs(angle(approxPoints[i % 4], approxPoints[i - 2], approxPoints[i - 1]))
            Timber.i(cosine.toString())
            internalMaxCosine = max(cosine, internalMaxCosine)
        }
        return internalMaxCosine
    }

    private fun angle(p1: Point, p2: Point, p0: Point): Double {
        val dx1 = p1.x - p0.x
        val dy1 = p1.y - p0.y
        val dx2 = p2.x - p0.x
        val dy2 = p2.y - p0.y
        return (dx1 * dx2 + dy1 * dy2) / sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2) + 1e-10)
    }

    private fun sortPoints(src: Array<Point>): Array<Point> {
        val srcPoints = listOf(*src)
        val result = arrayOf<Point?>(null, null, null, null)
        val sumComparator = Comparator { lhs: Point, rhs: Point -> (lhs.y + lhs.x).compareTo(rhs.y + rhs.x) }
        val diffComparator = Comparator { lhs: Point, rhs: Point -> (lhs.y - lhs.x).compareTo(rhs.y - rhs.x) }

        // top-left corner = minimal sum
        result[0] = Collections.min(srcPoints, sumComparator)
        // bottom-right corner = maximal sum
        result[2] = Collections.max(srcPoints, sumComparator)
        // top-right corner = minimal difference
        result[1] = Collections.min(srcPoints, diffComparator)
        // bottom-left corner = maximal difference
        result[3] = Collections.max(srcPoints, diffComparator)
        return result.requireNoNulls()
    }

    private val morph_kernel = Mat(Size(ScanConstants.KSIZE_CLOSE.toDouble(), ScanConstants.KSIZE_CLOSE.toDouble()), CvType.CV_8UC1, Scalar(255.0))
    fun detectLargestQuadrilateral(originalMat: Mat): Quadrilateral? {
        Imgproc.cvtColor(originalMat, originalMat, Imgproc.COLOR_BGR2GRAY, 4)

        // Just OTSU/Binary thresholding is not enough.
        //Imgproc.threshold(mGrayMat, mGrayMat, 150, 255, THRESH_BINARY + THRESH_OTSU);

        /*
         *  1. We shall first blur and normalize the image for uniformity,
         *  2. Truncate light-gray to white and normalize,
         *  3. Apply canny edge detection,
         *  4. Cutoff weak edges,
         *  5. Apply closing(morphology), then proceed to finding contours.
         */

        // step 1.
        Imgproc.blur(originalMat, originalMat, Size(ScanConstants.KSIZE_BLUR.toDouble(), ScanConstants.KSIZE_BLUR.toDouble()))
        Core.normalize(originalMat, originalMat, 0.0, 255.0, Core.NORM_MINMAX)
        // step 2.
        // As most papers are bright in color, we can use truncation to make it uniformly bright.
        Imgproc.threshold(originalMat, originalMat, ScanConstants.TRUNC_THRESH.toDouble(), 255.0, Imgproc.THRESH_TRUNC)
        Core.normalize(originalMat, originalMat, 0.0, 255.0, Core.NORM_MINMAX)
        // step 3.
        // After above preprocessing, canny edge detection can now work much better.
        Imgproc.Canny(originalMat, originalMat, ScanConstants.CANNY_THRESH_U.toDouble(), ScanConstants.CANNY_THRESH_L.toDouble())
        // step 4.
        // Cutoff the remaining weak edges
        Imgproc.threshold(originalMat, originalMat, ScanConstants.CUTOFF_THRESH.toDouble(), 255.0, Imgproc.THRESH_TOZERO)
        // step 5.
        // Closing - closes small gaps. Completes the edges on canny image; AND also reduces stringy lines near edge of paper.
        Imgproc.morphologyEx(originalMat, originalMat, Imgproc.MORPH_CLOSE, morph_kernel, Point(-1.0, -1.0), 1)

        // Get only the 10 largest contours (each approximated to their convex hulls)
        val largestContour = findLargestContours(originalMat, 10)
        return if (null != largestContour) {
            findQuadrilateral(largestContour)
        } else
            null
    }

    private fun hull2Points(hull: MatOfInt, contour: MatOfPoint): MatOfPoint {
        val indexes = hull.toList()
        val points: MutableList<Point> = ArrayList()
        val ctrList = contour.toList()
        for (index in indexes) {
            points.add(ctrList[index])
        }
        val point = MatOfPoint()
        point.fromList(points)
        return point
    }

    private fun findLargestContours(inputMat: Mat, numTopContours: Int): List<MatOfPoint>? {
        val mHierarchy = Mat()
        val mContourList: List<MatOfPoint> = ArrayList()
        //finding contours - as we are sorting by area anyway, we can use RETR_LIST - faster than RETR_EXTERNAL.
        Imgproc.findContours(inputMat, mContourList, mHierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)

        // Convert the contours to their Convex Hulls i.e. removes minor nuances in the contour
        val mHullList: MutableList<MatOfPoint> = ArrayList()
        val tempHullIndices = MatOfInt()
        for (i in mContourList.indices) {
            Imgproc.convexHull(mContourList[i], tempHullIndices)
            mHullList.add(hull2Points(tempHullIndices, mContourList[i]))
        }
        // Release mContourList as its job is done
        for (c in mContourList) c.release()
        tempHullIndices.release()
        mHierarchy.release()
        if (mHullList.size != 0) {
            mHullList.sortWith { lhs: MatOfPoint?, rhs: MatOfPoint? -> Imgproc.contourArea(rhs).compareTo(Imgproc.contourArea(lhs)) }
            return mHullList.subList(0, min(mHullList.size, numTopContours))
        }
        return null
    }

    private fun findQuadrilateral(mContourList: List<MatOfPoint>): Quadrilateral? {
        for (c in mContourList) {
            val c2f = MatOfPoint2f(*c.toArray())
            val peri = Imgproc.arcLength(c2f, true)
            val approx = MatOfPoint2f()
            Imgproc.approxPolyDP(c2f, approx, 0.02 * peri, true)
            val points = approx.toArray()
            // select biggest 4 angles polygon
            if (approx.rows() == 4) {
                val foundPoints = sortPoints(points)
                return Quadrilateral(approx, foundPoints)
            }
        }
        return null
    }

    fun isScanPointsValid(points: Map<Int, PointF>) = points.size == 4

}
