package com.example.spirala1

import android.os.Parcelable
import java.io.Serializable

class Biljka(
    var naziv: String,
    var porodica: String,
    var medicinskoUpozorenje: String?,
    var medicinskeKoristi: List<MedicinskaKorist>?,
    var profilOkusa: ProfilOkusaBiljke?,
    var jela: List<String>?,
    var klimatskiTipovi: List<KlimatskiTip>?,
    var zemljisniTipovi: List<Zemljiste>?
): Serializable {
    override fun toString(): String {
        return "Biljka(naziv='$naziv', porodica='$porodica')"
    }
}
