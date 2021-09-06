package info.hannes.liveedgedetection.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File


fun Context.getBaseDirectoryFromPathString(path: String): File = getDir(path, Context.MODE_PRIVATE)

fun Context.storeBitmap(bitmap: Bitmap, file: File) {
    getUriForFile(file).run {
        contentResolver.openOutputStream(this)?.run {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
            close()
        }
    }
}

fun Context.getUriForFile(file: File): Uri =
        FileProvider.getUriForFile(this, "$packageName.provider", file)

fun Context.viewPdf(pdfFile: File) {
    val path: Uri = this.getUriForFile(pdfFile)

    // Setting the intent for pdf reader
    val pdfIntent = Intent(Intent.ACTION_VIEW)
    pdfIntent.setDataAndType(path, "application/pdf")
    pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    try {
        startActivity(pdfIntent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "Can't read pdf file", Toast.LENGTH_SHORT).show()
    }
}
