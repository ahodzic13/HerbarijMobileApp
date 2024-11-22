package com.example.spirala1

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NovaBiljkaActivity : AppCompatActivity(){

    private lateinit var adapter: BiljkaListAdapter

    private val novaBiljkaList = mutableListOf<Biljka>()

    var index = -1

    private lateinit var nazivEditText: EditText
    private lateinit var porodicaEditText: EditText
    private lateinit var medicinskoUpozorenjeEditText: EditText
    private lateinit var jeloEditText: EditText
    private lateinit var medicinskaKoristListView: ListView
    private lateinit var klimatskiTipListView: ListView
    private lateinit var zemljisniTipListView: ListView
    private lateinit var profilOkusaListView: ListView
    private lateinit var jelaListView: ListView
    private lateinit var dodajJeloButton: Button
    private lateinit var dodajBiljkuButton: Button
    private lateinit var uslikajBiljkuButton: Button
    private lateinit var slikaImageView: ImageView


    private val jelaList = mutableListOf<String>()

    private lateinit var odabranaJela: MutableList<String>
    private lateinit var odabraniKlimatskiTipovi: MutableList<KlimatskiTip>
    private lateinit var odabraniProfilOkusa: ProfilOkusaBiljke
    private lateinit var odabraniZemljisniTipovi: MutableList<Zemljiste>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_biljka)

        nazivEditText = findViewById(R.id.nazivET)
        porodicaEditText = findViewById(R.id.porodicaET)
        medicinskoUpozorenjeEditText = findViewById(R.id.medicinskoUpozorenjeET)
        jeloEditText = findViewById(R.id.jeloET)
        medicinskaKoristListView = findViewById(R.id.medicinskaKoristLV)
        klimatskiTipListView = findViewById(R.id.klimatskiTipLV)
        zemljisniTipListView = findViewById(R.id.zemljisniTipLV)
        profilOkusaListView = findViewById(R.id.profilOkusaLV)
        jelaListView = findViewById(R.id.jelaLV)
        dodajJeloButton = findViewById(R.id.dodajJeloBtn)
        dodajBiljkuButton = findViewById(R.id.dodajBiljkuBtn)
        uslikajBiljkuButton = findViewById(R.id.uslikajBiljkuBtn)
        slikaImageView = findViewById(R.id.slikaIV)

        setupListViewAdapters()

        dodajJeloButton.setOnClickListener {
            dodajIliIzmijeniJelo()
        }

        dodajBiljkuButton.setOnClickListener {
            dodajBiljku()
        }

        uslikajBiljkuButton.setOnClickListener {
            uslikajBiljku()
        }

        jelaListView.setOnItemClickListener { parent, view, position, id ->
            val odabranoJelo = jelaList[position]
            index = position

            jeloEditText.setText(odabranoJelo)

            dodajJeloButton.text = getString(R.string.izmijeni_jelo)
        }

        dodajJeloButton.setOnClickListener {
            dodajIliIzmijeniJelo()
        }
    }


    private fun setupListViewAdapters() {
        medicinskaKoristListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        klimatskiTipListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        zemljisniTipListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        profilOkusaListView.choiceMode = ListView.CHOICE_MODE_SINGLE

        val medicinskeKoristiAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            MedicinskaKorist.values().map { it }
        )
        medicinskaKoristListView.adapter = medicinskeKoristiAdapter

        val klimatskiTipAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            KlimatskiTip.values().map { it }
        )
        klimatskiTipListView.adapter = klimatskiTipAdapter

        val zemljisniTipAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            Zemljiste.values().map { it.naziv } //Logicnije je postaviti naziv nego opis
        )
        zemljisniTipListView.adapter = zemljisniTipAdapter

        val profilOkusaAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_single_choice,
            ProfilOkusaBiljke.values().map { it }
        )
        profilOkusaListView.adapter = profilOkusaAdapter

        val jelaAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, jelaList)
        jelaListView.adapter = jelaAdapter
    }

    private fun dodajIliIzmijeniJelo() {
        val novoJelo = jeloEditText.text.toString().trim()

        val vecPostoji = jelaList.any { it.equals(novoJelo, ignoreCase = true) }

        if (vecPostoji && index == -1) {
            jeloEditText.setError("Ne možete dodati isto jelo više puta.")
        } else {
            if (novoJelo.isNotBlank()) {
                if (index != -1) {
                    jelaList[index] = novoJelo
                    (jelaListView.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                    jeloEditText.text.clear()
                    dodajJeloButton.text = getString(R.string.dodaj_jelo)
                    index = -1
                } else {
                    jelaList.add(novoJelo)
                    (jelaListView.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                    jeloEditText.text.clear()
                }
            } else {
                if (index != -1) {
                    jelaList.removeAt(index)
                    (jelaListView.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                    jeloEditText.text.clear()
                    dodajJeloButton.text = getString(R.string.dodaj_jelo)
                    index = -1
                } else {
                    Toast.makeText(this, "Unesite naziv jela", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun dodajBiljku() {
        val naziv = nazivEditText.text.toString()
        val porodica = porodicaEditText.text.toString()
        val medicinskoUpozorenje = medicinskoUpozorenjeEditText.text.toString()

        val odabraneMedicinskeKoristi = mutableListOf<MedicinskaKorist>()
        val selectedMedicinskeKoristiPositions = medicinskaKoristListView.checkedItemPositions
        for (i in 0 until selectedMedicinskeKoristiPositions.size()) {
            if (selectedMedicinskeKoristiPositions.valueAt(i)) {
                val korist = MedicinskaKorist.values()[selectedMedicinskeKoristiPositions.keyAt(i)]
                odabraneMedicinskeKoristi.add(korist)
            }
        }

        val odabraniJela = mutableListOf<String>()
        for (i in 0 until jelaList.size) {
            odabraniJela.add(jelaListView.getItemAtPosition(i).toString())
        }

        val odabraniKlimatskiTipovi = mutableListOf<KlimatskiTip>()
        val selectedKlimatskiTipoviPositions = klimatskiTipListView.checkedItemPositions
        for (i in 0 until selectedKlimatskiTipoviPositions.size()) {
            if (selectedKlimatskiTipoviPositions.valueAt(i)) {
                val tip = KlimatskiTip.values()[selectedKlimatskiTipoviPositions.keyAt(i)]
                odabraniKlimatskiTipovi.add(tip)
            }
        }

        val odabraniZemljisniTipovi = mutableListOf<Zemljiste>()
        val selectedZemljisniTipoviPositions = zemljisniTipListView.checkedItemPositions
        for (i in 0 until selectedZemljisniTipoviPositions.size()) {
            if (selectedZemljisniTipoviPositions.valueAt(i)) {
                val tip = Zemljiste.values()[selectedZemljisniTipoviPositions.keyAt(i)]
                odabraniZemljisniTipovi.add(tip)
            }
        }
        var validno = true
        if (naziv.isBlank() || naziv.length < 2 || naziv.length > 40) {
            nazivEditText.setError("Naziv mora imati između 2 i 40 znakova.")
            validno = false
        }

        if (porodica.isBlank() || porodica.length < 2 || porodica.length > 20) {
            porodicaEditText.setError("Porodica mora imati između 2 i 20 znakova.")
            validno = false
        }

        if (medicinskoUpozorenje.isBlank() || medicinskoUpozorenje.length < 2 || medicinskoUpozorenje.length > 20) {
            medicinskoUpozorenjeEditText.setError("Medicinsko upozorenje mora imati između 2 i 20 znakova.")
            validno = false
        }

        if (jelaList.isEmpty()) {
            Toast.makeText(this, "Molimo dodajte barem jedno jelo.", Toast.LENGTH_SHORT).show()
            validno = false
        }


        if(medicinskaKoristListView.checkedItemCount == 0){
            Toast.makeText(this, "Odaberite barem jednu medicinsku korist.", Toast.LENGTH_SHORT).show()
            validno = false
        }

        if (zemljisniTipListView.checkedItemCount == 0) {
            Toast.makeText(this, "Odaberite barem jedan zemljisni tip", Toast.LENGTH_SHORT).show()
            validno = false
        }

        if (klimatskiTipListView.checkedItemCount == 0) {
            Toast.makeText(this, "Odaberite barem jedan klimatski tip", Toast.LENGTH_SHORT).show()
            validno = false
        }


        val odabraniProfilOkusaPosition = profilOkusaListView.checkedItemPosition
        if (odabraniProfilOkusaPosition == ListView.INVALID_POSITION) {
            validno = false
            Toast.makeText(this, "Molimo odaberite profil okusa.", Toast.LENGTH_SHORT).show()
            return
        }
        val odabraniProfilOkusa = ProfilOkusaBiljke.entries[odabraniProfilOkusaPosition]



        if (validno) {
            val novaBiljka = Biljka(
                naziv,
                porodica,
                medicinskoUpozorenje,
                odabraneMedicinskeKoristi,
                odabraniProfilOkusa,
                odabraniJela,
                odabraniKlimatskiTipovi,
                odabraniZemljisniTipovi
            )
            CoroutineScope(Dispatchers.Main).launch {
                val ispravljenaBiljka = TrefleDAO().fixData(novaBiljka)
                val intent = Intent(this@NovaBiljkaActivity, MainActivity::class.java)
                intent.putExtra("novaBiljka", ispravljenaBiljka)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }


    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    private fun uslikajBiljku() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            slikaImageView.setImageBitmap(imageBitmap)
        }
    }
}

