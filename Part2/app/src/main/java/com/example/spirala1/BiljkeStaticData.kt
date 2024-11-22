package com.example.spirala1



val biljke = listOf(
    Biljka(
        naziv = "Bosiljak (Ocimum basilicum)",
        porodica = "Lamiaceae (usnate)",
        medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
        medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.REGULACIJAPROBAVE),
        profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
        jela = listOf("Salata od paradajza", "Punjene tikvice"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
    ),
    Biljka(
        naziv = "Nana (Mentha spicata)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine.",
        medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PROTIVBOLOVA),
        profilOkusa = ProfilOkusaBiljke.MENTA,
        jela = listOf("Jogurt sa voćem", "Gulaš"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.UMJERENA),
        zemljisniTipovi = listOf(Zemljiste.GLINENO, Zemljiste.CRNICA)
    ),
    Biljka(
        naziv = "Kamilica (Matricaria chamomilla)",
        porodica = "Asteraceae (glavočike)",
        medicinskoUpozorenje = "Može uzrokovati alergijske reakcije kod osjetljivih osoba.",
        medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Čaj od kamilice"),
        klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
    ),
    Biljka(
        naziv = "Ružmarin (Rosmarinus officinalis)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Treba ga koristiti umjereno i konsultovati se sa ljekarom pri dugotrajnoj upotrebi ili upotrebi u većim količinama.",
        medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPRITISKA),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Pečeno pile", "Grah","Gulaš"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
        zemljisniTipovi = listOf(Zemljiste.SLJUNKOVITO, Zemljiste.KRECNJACKO)
    ),
    Biljka(
        naziv = "Lavanda (Lavandula angustifolia)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine. Također, treba izbjegavati kontakt lavanda ulja sa očima.",
        medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PODRSKAIMUNITETU),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Jogurt sa voćem"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
    ),
    Biljka(
        naziv = "Kadulja (Salvia officinalis)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Osobe koje uzimaju lijekove za povećanje krvnog tlaka trebaju izbjegavati konzumaciju kadulje.",
        medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PROTUUPALNO),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Piletina s kaduljom", "Krem juha od bundeve"),
        klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUHA),
        zemljisniTipovi = listOf(Zemljiste.GLINENO, Zemljiste.KRECNJACKO)
    ),
    Biljka(
        naziv = "Peršun (Petroselinum crispum)",
        porodica = "Apiaceae (mrazovci)",
        medicinskoUpozorenje = "Osobe alergične na hranu poput celera i mrkve mogu biti alergične na peršun.",
        medicinskeKoristi = listOf(MedicinskaKorist.PODRSKAIMUNITETU, MedicinskaKorist.PROTUUPALNO),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Salata od paradajza", "Punjene tikvice"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
    ),
    Biljka(
        naziv = "Matičnjak (Melissa officinalis)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Osobe koje uzimaju sedative trebaju izbjegavati konzumaciju matičnjaka.",
        medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Čaj od matičnjaka", "Ledeni čaj"),
        klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUHA),
        zemljisniTipovi = listOf(Zemljiste.GLINENO, Zemljiste.CRNICA)
    ),
    Biljka(
        naziv = "Estragon (Artemisia dracunculus)",
        porodica = "Asteraceae (glavočike)",
        medicinskoUpozorenje = "Izbjegavajte konzumaciju estragona ako imate bolesti bubrega.",
        medicinskeKoristi = listOf(MedicinskaKorist.REGULACIJAPROBAVE),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Francuska salata", "Umak od estragona"),
        klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUHA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
    ),
    Biljka(
        naziv = "Lovor (Laurus nobilis)",
        porodica = "Lauraceae",
        medicinskoUpozorenje = "Prekomjerna konzumacija lovora može uzrokovati gastrointestinalne probleme.",
        medicinskeKoristi = listOf(MedicinskaKorist.REGULACIJAPROBAVE),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Rižoto", "Variva"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.UMJERENA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.GLINENO)
    )
)

