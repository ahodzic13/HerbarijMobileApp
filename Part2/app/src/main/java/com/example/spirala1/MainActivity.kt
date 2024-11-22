package com.example.spirala1

import CustomItemDecoration
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(), BiljkaClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BiljkaListAdapter
    private lateinit var resetButton: Button
    private lateinit var spinner1: Spinner

    private val NOVA_BILJKA_REQUEST_CODE = 1

    private lateinit var glavnaListaBiljke: MutableList<Biljka>


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

        adapter = BiljkaListAdapter(glavnaListaBiljke, moda, this, glavnaListaBiljke)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.addItemDecoration(CustomItemDecoration())


        val spinner: Spinner = findViewById(R.id.modSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.opcije_spinnera,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

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
                updateList2()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
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
        adapter = BiljkaListAdapter(glavnaListaBiljke, moda, this, glavnaListaBiljke)
        recyclerView.adapter = adapter
    }

    private fun updateList2() {
        val filteredList = when (moda) {
            Mod.Medicinski -> filterByMedicinskiMod()
            Mod.Botanicki -> filterByBotanickiMod()
            Mod.Kuharski -> filterByKuharskiMod()
        }
        adapter.updateModa(moda)
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
            biljka.medicinskeKoristi.any { korist ->
                referentnaBiljka.medicinskeKoristi.contains(korist)
            }
        }
    }

    private fun filterByBotanickiMod(): List<Biljka> {
        val filteredList = glavnaListaBiljke.filter { biljka ->

            biljka.porodica == referentnaBiljka.porodica &&
                    biljka.klimatskiTipovi.intersect(referentnaBiljka.klimatskiTipovi).isNotEmpty() &&
                    biljka.zemljisniTipovi.intersect(referentnaBiljka.zemljisniTipovi).isNotEmpty()
        }
        return filteredList
    }


    private fun filterByKuharskiMod(): List<Biljka> {
        return glavnaListaBiljke.filter { biljka ->
            biljka.jela.any { jelo ->
                referentnaBiljka.jela.contains(jelo)
            } || biljka.profilOkusa == referentnaBiljka.profilOkusa
        }
    }

}