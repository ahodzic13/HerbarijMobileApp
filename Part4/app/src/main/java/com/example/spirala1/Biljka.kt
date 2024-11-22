package com.example.spirala1

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable
@Entity
class Biljka(
    @PrimaryKey(autoGenerate = true) var id:Long?=null,
    var naziv: String,
    @ColumnInfo ("family") var porodica: String,
    var medicinskoUpozorenje: String?,
    var medicinskeKoristi: List<MedicinskaKorist>?,
    @TypeConverters(Converters::class)
    var profilOkusa: ProfilOkusaBiljke?,
    @TypeConverters(Converters::class)
    var jela: List<String>?,
    @TypeConverters(Converters::class)
    var klimatskiTipovi: List<KlimatskiTip>?,
    @TypeConverters(Converters::class)
    var zemljisniTipovi: List<Zemljiste>?,
    var onlineChecked: Boolean = false
): Serializable {
    override fun toString(): String {
        return "Biljka(naziv='$naziv', porodica='$porodica')"
    }

    fun copy(
        id: Long? = this.id,
        naziv: String = this.naziv,
        porodica: String = this.porodica,
        medicinskoUpozorenje: String? = this.medicinskoUpozorenje,
        medicinskeKoristi: List<MedicinskaKorist>? = this.medicinskeKoristi,
        profilOkusa: ProfilOkusaBiljke? = this.profilOkusa,
        jela: List<String>? = this.jela,
        klimatskiTipovi: List<KlimatskiTip>? = this.klimatskiTipovi,
        zemljisniTipovi: List<Zemljiste>? = this.zemljisniTipovi,
        onlineChecked: Boolean = this.onlineChecked
    ): Biljka {
        return Biljka(
            id = id,
            naziv = naziv,
            porodica = porodica,
            medicinskoUpozorenje = medicinskoUpozorenje,
            medicinskeKoristi = medicinskeKoristi,
            profilOkusa = profilOkusa,
            jela = jela,
            klimatskiTipovi = klimatskiTipovi,
            zemljisniTipovi = zemljisniTipovi,
            onlineChecked = onlineChecked
        )
    }
}
