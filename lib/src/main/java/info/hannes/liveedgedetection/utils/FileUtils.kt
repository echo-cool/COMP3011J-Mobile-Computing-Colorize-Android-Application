package info.hannes.liveedgedetection.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfVersion
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.WriterProperties
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import timber.log.Timber
import java.io.File


fun String.decodeBitmapFromFile(): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inPreferredConfig = Bitmap.Config.ARGB_8888
    return BitmapFactory.decodeFile(this, options)
}

fun Bitmap.store(file: File, context: Context) {
    context.getUriForFile(file).run {
        context.contentResolver.openOutputStream(this)?.run {
            compress(Bitmap.CompressFormat.JPEG, 100, this)
            close()
        }
    }
}

fun File.store(bitmap: Bitmap, context: Context) {
    context.getUriForFile(this).run {
        context.contentResolver.openOutputStream(this)?.run {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
            close()
        }
    }
}


fun File.createPdf(path: File): File {

    val file = File("$path${File.pathSeparatorChar}${this.nameWithoutExtension}.pdf")
    val pdf = PdfDocument(
            PdfWriter(file.absoluteFile.absolutePath,
                    WriterProperties()
                            .addXmpMetadata()
                            .setPdfVersion(PdfVersion.PDF_1_6)))
    val info = pdf.documentInfo
    info.title = "Mobilephone scan"
    //info.author = ""
    info.subject = "LiveEdgeDetection"
    info.keywords = "Mobilephone scan"
    info.creator = "https://github.com/hannesa2/LiveEdgeDetection"
    val document = Document(pdf)
    try {
        val imageData = ImageDataFactory.create(this.absolutePath)
        val pdfImg = Image(imageData)
        document.add(pdfImg)
    } catch (e: Exception) {
        Timber.e(e)
    } finally {
        document.close()
    }
    return file
}
