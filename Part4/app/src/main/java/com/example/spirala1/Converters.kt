package com.example.spirala1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import kotlinx.coroutines.CoroutineStart
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class Converters {

    @TypeConverter
    fun fromMedicinskaKoristList(koristi: List<MedicinskaKorist>?): String? {
        return koristi?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toMedicinskaKoristList(data: String?): List<MedicinskaKorist>? {
        return data?.split(",")?.map { MedicinskaKorist.valueOf(it) }
    }

    @TypeConverter
    fun fromKlimatskiTipList(tipovi: List<KlimatskiTip>?): String? {
        return tipovi?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toKlimatskiTipList(data: String?): List<KlimatskiTip>? {
        return data?.split(",")?.map { KlimatskiTip.valueOf(it) }
    }

    @TypeConverter
    fun fromZemljisniTipList(tipovi: List<Zemljiste>?): String? {
        return tipovi?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toZemljisniTipList(data: String?): List<Zemljiste>? {
        return data?.split(",")?.mapNotNull { value ->
            try {
                Zemljiste.valueOf(value.trim())
            } catch (e: IllegalArgumentException) {
                println("Invalid value for Zemljiste enum: $value")
                null
            }
        }
    }


    @TypeConverter
    fun fromProfilOkusa(profilOkusa: ProfilOkusaBiljke?): String? {
        return profilOkusa?.name
    }

    @TypeConverter
    fun toProfilOkusa(data: String?): ProfilOkusaBiljke? {
        return data?.let { ProfilOkusaBiljke.valueOf(it) }
    }

    @TypeConverter
    fun fromJelaList(jela: List<String>?): String? {
        return jela?.joinToString(",")
    }

    @TypeConverter
    fun toJelaList(data: String?): List<String>? {
        return data?.split(",")
    }

    @OptIn(ExperimentalEncodingApi::class)
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.Default.encode(byteArray)
    }

    @OptIn(ExperimentalEncodingApi::class)
    @TypeConverter
    fun toBitmap(encodedString: String): Bitmap {
        val byteArray = Base64.Default.decode(encodedString)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
