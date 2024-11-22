package com.example.spirala1

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class VlastitiTestoviSpirala4 {
    private lateinit var biljkaDao: BiljkaDAO
    private lateinit var db: BiljkaDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, BiljkaDatabase::class.java).build()
        biljkaDao = db.biljkaDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun testDodavanjeBiljke() = runBlocking {
        val biljka = Biljka(
            naziv = "Bosiljak (Ocimum basilicum)",
            porodica = "Lamiaceae (usnate)",
            medicinskoUpozorenje = "Achtung achtung",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Sos", "Salata"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO)
        )

        biljkaDao.saveBiljka(biljka)
        val listaBiljaka = biljkaDao.getAllBiljkas()
        assertThat(listaBiljaka.size, `is`(1))
        assertThat(listaBiljaka[0].naziv, `is`("Bosiljak (Ocimum basilicum)"))
    }

    @Test
    fun testBrisanjeSvihBiljaka() = runBlocking {
        val biljka = Biljka(
            naziv = "Kamilica (Matricaria chamomilla)",
            porodica = "Asteraceae (glavočike)",
            medicinskoUpozorenje = "Achtung",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE),
            profilOkusa =  ProfilOkusaBiljke.LJUTO,
            jela = listOf("Hambas"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO),
        )

        biljkaDao.saveBiljka(biljka)
        assertThat(biljkaDao.getAllBiljkas().size, `is`(1))

        biljkaDao.clearData()
        assertThat(biljkaDao.getAllBiljkas().size, `is`(0))
    }

    //Napravio test zbog sebe da testiram fixOfflineBiljka()
    @Test
    fun testFixOfflineBiljka() = runBlocking {
        // Insert a plant with onlineChecked set to false
        val biljka = Biljka(
            naziv = "Mentha spicata",
            porodica = "aaa",
            medicinskoUpozorenje = null,
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Čaj"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO),
            onlineChecked = false
        )

        biljkaDao.saveBiljka(biljka)

        val updatedCount = biljkaDao.fixOfflineBiljka()

        val updatedBiljka = biljkaDao.getAllBiljkas()[0]

        assertThat(updatedCount, `is`(1))
        assertThat(updatedBiljka.onlineChecked, `is`(true))

        assertThat(updatedBiljka.porodica, `is`("Lamiaceae"))

        assertThat(updatedBiljka.klimatskiTipovi, `is`(notNullValue()))
        assertThat(updatedBiljka.klimatskiTipovi, `is`(hasItem(KlimatskiTip.SUBTROPSKA)))

    }


}
