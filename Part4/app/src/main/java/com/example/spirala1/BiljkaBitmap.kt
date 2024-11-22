package com.example.spirala1

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(
    foreignKeys = [ForeignKey(
        entity = Biljka::class,
        parentColumns = ["id"],
        childColumns = ["idBiljke"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BiljkaBitmap(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var idBiljke: Long,
    @TypeConverters(Converters::class) var bitmap: Bitmap
)