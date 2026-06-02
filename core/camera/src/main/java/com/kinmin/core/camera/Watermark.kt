package com.kinmin.core.camera

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Draws a timestamp + GPS watermark onto a photo and writes a compressed JPEG
 * to app-private storage. Returns the saved file. Compression keeps each photo
 * ~100-150 KB so the 1 GB Supabase free storage lasts for a pilot.
 */
object Watermark {

    fun stamp(
        source: Bitmap,
        outDir: File,
        lat: Double?,
        lng: Double?,
        quality: Int = 60,
        maxWidth: Int = 1000
    ): File {
        val scaled = scale(source, maxWidth)
        val bmp = scaled.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bmp)

        val time = SimpleDateFormat("dd MMM yyyy  HH:mm", Locale.getDefault()).format(Date())
        val gps = if (lat != null && lng != null)
            "GPS %.5f, %.5f".format(lat, lng) else "GPS unavailable"

        val pad = bmp.width * 0.025f
        val textSize = bmp.width * 0.038f

        val bg = Paint().apply { color = Color.argb(140, 0, 0, 0) }
        val boxHeight = textSize * 2.8f + pad
        canvas.drawRect(0f, bmp.height - boxHeight, bmp.width.toFloat(), bmp.height.toFloat(), bg)

        val text = Paint().apply {
            color = Color.WHITE
            this.textSize = textSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText(time, pad, bmp.height - boxHeight + textSize + pad / 2, text)
        canvas.drawText(gps, pad, bmp.height - boxHeight + textSize * 2.3f + pad / 2, text)

        val file = File(outDir, "kinmin_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, out)
        }
        return file
    }

    private fun scale(src: Bitmap, maxWidth: Int): Bitmap {
        if (src.width <= maxWidth) return src
        val ratio = maxWidth.toFloat() / src.width
        return Bitmap.createScaledBitmap(src, maxWidth, (src.height * ratio).toInt(), true)
    }
}
