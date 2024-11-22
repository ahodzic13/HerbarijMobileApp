package com.example.spirala1

import CustomItemDecoration
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.spirala1.TrefleDAO



class MainActivity : AppCompatActivity(), BiljkaClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BiljkaListAdapter
    private lateinit var resetButton: Button
    private lateinit var spinner1: Spinner

    private lateinit var pretragaEditText: EditText
    private lateinit var bojaSpinner: Spinner
    private lateinit var brzaPretragaButton: Button

    private val NOVA_BILJKA_REQUEST_CODE = 1

    private lateinit var glavnaListaBiljke: MutableList<Biljka>

    private var brzaPretragaAktivna: Boolean = false

    private var moda: Mod = Mod.Medicinski

    private lateinit var referentnaBiljka: Biljka

    private val novaBiljkaActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val novaBiljka = data?.getSerializableExtra("novaBiljka") as? Biljka
                novaBiljka?.let {
                    glavnaListaBiljke.add(it)
                    adapter.notifyDataSetChanged()
                    updateList()
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //dodao ja
        glavnaListaBiljke = mutableListOf()
        glavnaListaBiljke.addAll(biljke)
        referentnaBiljka = glavnaListaBiljke[0]

        recyclerView = findViewById(R.id.biljkeRV)
        resetButton = findViewById(R.id.resetBtn)
        spinner1 = findViewById(R.id.modSpinner)

        adapter = BiljkaListAdapter(glavnaListaBiljke, moda, this, glavnaListaBiljke, false)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.addItemDecoration(CustomItemDecoration())

        pretragaEditText = findViewById(R.id.pretragaET)
        bojaSpinner = findViewById(R.id.bojaSPIN)
        brzaPretragaButton = findViewById(R.id.brzaPretraga)


        val spinner: Spinner = findViewById(R.id.modSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcije_spinnera,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        val bojeAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.boje,
            android.R.layout.simple_spinner_item
        )
        bojeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bojaSpinner.adapter = bojeAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                moda = when (position) {
                    0 -> Mod.Medicinski
                    1 -> Mod.Botanicki
                    2 -> Mod.Kuharski
                    else -> Mod.Medicinski
                }
                if(brzaPretragaAktivna == true) {
                    updateList()
                }

                updateList2()
            }



            override fun onNothingSelected(parent: AdapterView<*>) {
            }

        }

        /*brzaPretragaButton.setOnClickListener {
            val searchTerm = pretragaEditText.text.toString()
            val flowerColor = bojaSpinner.selectedItem.toString()
            CoroutineScope(Dispatchers.Main).launch {
                val result = withContext(Dispatchers.IO) {
                    TrefleDAO().getPlantsWithFlowerColor(flowerColor, searchTerm)
                }
                when (result) {
                    is Result.Success -> {
                        val plants = result.data
                        adapter.updatePlants(plants)
                    }
                    is Result.Error -> {
                        val exception = result.exception
                        Log.e("MainActivity", "Error fetching plants: ${exception.message}")
                        Toast.makeText(this@MainActivity, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }else->{
                    Toast.makeText(this@MainActivity, "Error:", Toast.LENGTH_SHORT).show()

                }
                }
            }
        }*/
        brzaPretragaButton.setOnClickListener {
            val searchTerm = pretragaEditText.text.toString()
            val flowerColor = bojaSpinner.selectedItem.toString()
            brzaPretragaAktivna = true
            CoroutineScope(Dispatchers.Main).launch {
                val plants = withContext(Dispatchers.IO) {
                    TrefleDAO().getPlantsWithFlowerColor(flowerColor, searchTerm)
                }
                if (plants.isNotEmpty()) {
                    adapter.updatePlants(plants)
                } else {
                    Log.e("MainActivity", "No plants found.")
                    Toast.makeText(this@MainActivity, "No plants found.", Toast.LENGTH_SHORT).show()
                }
            }
        }





        resetButton.setOnClickListener {
            val trenutnaModa = moda
            moda = when (trenutnaModa) {
                Mod.Medicinski -> Mod.Medicinski
                Mod.Botanicki -> Mod.Botanicki
                Mod.Kuharski -> Mod.Kuharski
                else -> Mod.Medicinski
            }
            brzaPretragaAktivna=false
            updateList()
        }

        val novaBiljkaButton: Button = findViewById(R.id.novaBiljkaBtn)
        novaBiljkaButton.setOnClickListener {
            val intent = Intent(this, NovaBiljkaActivity::class.java)
            novaBiljkaActivityResult.launch(intent)
        }
    }

    private fun dodajNovuBiljku(biljka: Biljka) {
        glavnaListaBiljke.add(biljka)
    }


    private fun updateList() {
        val filteredList = when (moda) {
            Mod.Medicinski -> filterByMedicinskiMod()
            Mod.Botanicki -> filterByBotanickiMod()
            Mod.Kuharski -> filterByKuharskiMod()
        }
        adapter = BiljkaListAdapter(glavnaListaBiljke, moda, this, glavnaListaBiljke, true)
        recyclerView.adapter = adapter
    }

    private fun updateList2() {
        val filteredList = when (moda) {
            Mod.Medicinski -> filterByMedicinskiMod()
            Mod.Botanicki -> filterByBotanickiMod()
            Mod.Kuharski -> filterByKuharskiMod()
        }
        adapter.updateModa(moda)
        toggleSearchElements(moda == Mod.Botanicki)
    }

    private fun updateListPom() {
        val filteredList = when (moda) {
            Mod.Medicinski -> filterByMedicinskiMod()
            Mod.Botanicki -> filterByBotanickiMod()
            Mod.Kuharski -> filterByKuharskiMod()
        }
        adapter.updateListPom(filteredList)
    }


    override fun onBiljkaClicked(biljka: Biljka) {
        referentnaBiljka = biljka
        updateListPom()
    }

    private fun filterByMedicinskiMod(): List<Biljka> {
        return glavnaListaBiljke.filter { biljka ->
            biljka.medicinskeKoristi!!.any { korist ->
                referentnaBiljka.medicinskeKoristi!!.contains(korist)
            }
        }
    }

    private fun filterByBotanickiMod(): List<Biljka> {
        val filteredList = glavnaListaBiljke.filter { biljka ->

            biljka.porodica == referentnaBiljka.porodica &&
                    referentnaBiljka.klimatskiTipovi?.intersect(biljka.klimatskiTipovi ?: emptyList())!!.isNotEmpty() &&
                    referentnaBiljka.zemljisniTipovi?.intersect(biljka.zemljisniTipovi ?: emptyList())!!.isNotEmpty()

        }
        return filteredList
    }


    private fun filterByKuharskiMod(): List<Biljka> {
        return glavnaListaBiljke.filter { biljka ->
            biljka.jela!!.any { jelo ->
                referentnaBiljka.jela!!.contains(jelo)
            } || biljka.profilOkusa == referentnaBiljka.profilOkusa
        }
    }

    private fun toggleSearchElements(visible: Boolean) {
        if (visible) {
            pretragaEditText.visibility = View.VISIBLE
            bojaSpinner.visibility = View.VISIBLE
            brzaPretragaButton.visibility = View.VISIBLE
        } else {
            pretragaEditText.visibility = View.GONE
            bojaSpinner.visibility = View.GONE
            brzaPretragaButton.visibility = View.GONE
        }
    }

    /*private fun performQuickSearch(flowerColor: String, substr: String) {
        lifecycleScope.launch {
            try {
                val biljke = TrefleDAO.getPlantsWithFlowerColor(flowerColor, substr)
                adapter.updateListPom(biljke)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching plants: ${e.message}")
            }
        }
    }*/

    /*private fun performQuickSearch(flowerColor: String, substr: String) {
        if (substr.isNotEmpty()) {
            lifecycleScope.launch {
                try {
                    val biljke = TrefleDAO(applicationContext).getPlantsWithFlowerColor(flowerColor, substr)
                    adapter.updateListPom(biljke)
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error fetching plants: ${e.message}")
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Unesite pretragu", Toast.LENGTH_SHORT).show()
        }
    }*/


}