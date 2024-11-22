package com.example.spirala1

import android.os.Parcelable
import java.io.Serializable

class Biljka(
    val naziv: String,
    val porodica: String,
    val medicinskoUpozorenje: String,
    val medicinskeKoristi: List<MedicinskaKorist>,
    val profilOkusa: ProfilOkusaBiljke,
    val jela: List<String>,
    val klimatskiTipovi: List<KlimatskiTip>,
    val zemljisniTipovi: List<Zemljiste>
): Serializable {
    override fun toString(): String {
        return "Biljka(naziv='$naziv', porodica='$porodica')"
    }
}
