package com.example.spirala1

import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface BiljkaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBiljka(biljka: Biljka): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertBiljkaBitmap(biljkaBitmap: BiljkaBitmap): Long

    @Query("SELECT * FROM BiljkaBitmap WHERE idBiljke = :idBiljke LIMIT 1")
    suspend fun getImageForBiljke(idBiljke: Long): BiljkaBitmap?

    @Transaction
    suspend fun addImage(idBiljke: Long, bitmap: Bitmap): Boolean {
        val existingImage = getImageForBiljke(idBiljke)
        return if (existingImage != null) {
            false
        } else {
            val biljkaBitmap = BiljkaBitmap(null, idBiljke, bitmap)
            insertBiljkaBitmap(biljkaBitmap)
            true
        }
    }

    @Query("SELECT * FROM BiljkaBitmap WHERE idBiljke = :idBiljke")
    suspend fun getBiljkaBitmap(idBiljke: Long): BiljkaBitmap?

    @Query("SELECT * FROM Biljka")
    suspend fun getAllBiljkas(): List<Biljka>

    @Query("DELETE FROM Biljka")
    suspend fun clearAllBiljkas()

    @Query("DELETE FROM BiljkaBitmap")
    suspend fun clearAllBiljkaBitmaps()

    @Transaction
    suspend fun clearData() {
        clearAllBiljkas()
        clearAllBiljkaBitmaps()
    }

    @Query("SELECT * FROM Biljka WHERE onlineChecked = 0")
    suspend fun getOfflineBiljkas(): List<Biljka>

    @Update
    suspend fun updateBiljka(biljka: Biljka)

    @Transaction
    suspend fun fixOfflineBiljka(): Int {
        val offlineBiljkas = getOfflineBiljkas()
        var updatedCount = 0

        for (biljka in offlineBiljkas) {
            val originalBiljka = biljka.copy()
            val updatedBiljka = TrefleDAO().fixData(biljka)
            updatedBiljka.onlineChecked = true
            if (originalBiljka != updatedBiljka) {
                updateBiljka(updatedBiljka)
                updatedCount++
            }
        }

        return updatedCount
    }
}
