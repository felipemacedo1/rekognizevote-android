package com.rekognizevote.core.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageUtils {
    
    fun compressImage(file: File, maxSizeKB: Int = 500): File? {
        return try {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val rotatedBitmap = rotateImageIfRequired(bitmap, file.absolutePath)
            
            var quality = 90
            var compressedFile: File
            
            do {
                compressedFile = File(file.parent, "compressed_${file.name}")
                val outputStream = FileOutputStream(compressedFile)
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                outputStream.close()
                quality -= 10
            } while (compressedFile.length() > maxSizeKB * 1024 && quality > 10)
            
            compressedFile
        } catch (e: Exception) {
            null
        }
    }
    
    private fun rotateImageIfRequired(bitmap: Bitmap, imagePath: String): Bitmap {
        return try {
            val exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                else -> bitmap
            }
        } catch (e: IOException) {
            bitmap
        }
    }
    
    private fun rotateImage(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    
    fun validateImageFile(file: File): Boolean {
        if (!file.exists() || file.length() == 0L) return false
        
        return try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(file.absolutePath, options)
            options.outWidth > 0 && options.outHeight > 0
        } catch (e: Exception) {
            false
        }
    }
}