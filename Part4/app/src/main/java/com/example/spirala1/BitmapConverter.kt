package com.example.spirala1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import kotlinx.coroutines.CoroutineStart
import java.io.ByteArrayOutputStream
import android.util.Base64
import kotlin.io.encoding.ExperimentalEncodingApi



class BitmapConverter {

    @OptIn(ExperimentalStdlibApi::class)
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @TypeConverter
    fun toBitmap(encodedString: String): Bitmap {
        val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}