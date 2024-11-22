package com.example.spirala1

import android.content.Intent
import android.provider.MediaStore
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anything
import org.junit.Rule
import org.junit.Test


class VlastitiTestovi {

    @get:Rule
    var activityRule = ActivityScenarioRule(NovaBiljkaActivity::class.java)

    private fun postaviEditText() {
        onView(withId(R.id.nazivET)).perform(typeText("Djumbir"))
        closeSoftKeyboard()
        onView(withId(R.id.porodicaET)).perform(typeText("Zimbvacae"))
        closeSoftKeyboard()
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("Povecava pritisak"))
        closeSoftKeyboard()
        onView(withId(R.id.jeloET)).perform(typeText("Peceni krompir"))
        closeSoftKeyboard()
    }

    @Test
    fun testValidacijaDuzine1() {
        onView(withId(R.id.nazivET)).perform(typeText("k"))
        closeSoftKeyboard()
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Naziv mora imati između 2 i 20 znakova.")))
    }

    @Test
    fun testValidacijaDuzine2() {
        onView(withId(R.id.nazivET)).perform(typeText("Mrkva"))
        closeSoftKeyboard()
        onView(withId(R.id.porodicaET)).perform(typeText("b"))
        closeSoftKeyboard()
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Porodica mora imati između 2 i 20 znakova.")))
    }

    @Test
    fun testValidacijaDuzine3() {
        onView(withId(R.id.nazivET)).perform(typeText("Kamilica"))
        closeSoftKeyboard()
        onView(withId(R.id.porodicaET)).perform(typeText("kkkkkk"))
        closeSoftKeyboard()
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("c"))
        closeSoftKeyboard()
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Medicinsko upozorenje mora imati između 2 i 20 znakova.")))
    }

    @Test
    fun testValidacijaMedicinskaKoristLV() {
        postaviEditText()
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.dodajBiljkuBtn)).check(matches(isDisplayed()))
    }

    @Test
    fun testValidacijaKlimatskiTipLV() {
        postaviEditText()
        onData(anything()).inAdapterView(withId(R.id.medicinskaKoristLV)).atPosition(1).perform(click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.dodajBiljkuBtn)).check(matches(isDisplayed()))
    }


    @Test
    fun testValidacijaZemljisniTipLV() {
        postaviEditText()
        onData(anything()).inAdapterView(withId(R.id.medicinskaKoristLV)).atPosition(3).perform(click())
        onData(anything()).inAdapterView(withId(R.id.klimatskiTipLV)).atPosition(2).perform(click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.dodajBiljkuBtn)).check(matches(isDisplayed()))
    }

    @Test
    fun testValidacijaProfilOkusaLV() {
        postaviEditText()
        onData(anything()).inAdapterView(withId(R.id.medicinskaKoristLV)).atPosition(1).perform(click())
        onData(anything()).inAdapterView(withId(R.id.klimatskiTipLV)).atPosition(2).perform(click())
        onData(anything()).inAdapterView(withId(R.id.zemljisniTipLV)).atPosition(1).perform(click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.dodajBiljkuBtn)).check(matches(isDisplayed()))
    }

    @Test
    fun testValidacijaDodavanjaDuplihJela() {
        onView(withId(R.id.jeloET)).perform(typeText("Pizza"))
        closeSoftKeyboard()
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        closeSoftKeyboard()
        onView(withId(R.id.jeloET)).perform(typeText("pizza"))
        closeSoftKeyboard()
        onView(withId(R.id.dodajJeloBtn)).perform(click())

        onView(withId(R.id.dodajJeloBtn))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testSlikanjeBiljke() {
        val activityScenario = ActivityScenario.launch(NovaBiljkaActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.uslikajBiljkuBtn)).perform(ViewActions.click())
        activityScenario.onActivity { activity ->
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activity.startActivityForResult(takePictureIntent, NovaBiljkaActivity.REQUEST_IMAGE_CAPTURE)
        }
    }
}





