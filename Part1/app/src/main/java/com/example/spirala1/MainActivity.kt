package com.example.spirala1

import CustomItemDecoration
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(), BiljkaClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BiljkaListAdapter
    private lateinit var resetButton: Button
    private lateinit var spinner1: Spinner


    private var moda: Mod = Mod.Medicinski

    private var referentnaBiljka: Biljka = biljke[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.biljkeRV)
        resetButton = findViewById(R.id.resetBtn)
        spinner1 = findViewById(R.id.modSpinner)




        // Postavljanje adaptera za RecyclerView
        adapter = BiljkaListAdapter(biljke, moda, this)
        recyclerView.adapter = adapter

        // Postavljanje LayoutManagera za RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.addItemDecoration(CustomItemDecoration())


        // Dodavanje listenera na spinner kako bi se promijenila moda
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
                // Postavljanje nove mode na temelju odabrane opcije u spinneru
                moda = when (position) {
                    0 -> Mod.Medicinski
                    1 -> Mod.Botanicki
                    2 -> Mod.Kuharski
                    else -> Mod.Medicinski
                }
                // Ažuriranje prikaza liste
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
    }


    private fun updateList() {
       /* val filteredList = when (moda) {
            Mod.Medicinski -> filterByMedicinskiMod()
            Mod.Botanicki -> filterByBotanickiMod()
            Mod.Kuharski -> filterByKuharskiMod()
        }*/
        adapter = BiljkaListAdapter(biljke, moda, this)
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
        return biljke.filter { biljka ->
            biljka.medicinskeKoristi.any { korist ->
                referentnaBiljka.medicinskeKoristi.contains(korist)
            }
        }
    }

    private fun filterByBotanickiMod(): List<Biljka> {
        val filteredList = biljke.filter { biljka ->
            biljka.porodica == referentnaBiljka.porodica &&
                    biljka.klimatskiTipovi.intersect(referentnaBiljka.klimatskiTipovi).isNotEmpty() &&
                    biljka.zemljisniTipovi.intersect(referentnaBiljka.zemljisniTipovi).isNotEmpty()
        }
        return filteredList
    }


    private fun filterByKuharskiMod(): List<Biljka> {
        return biljke.filter { biljka ->
            biljka.jela.any { jelo ->
                referentnaBiljka.jela.contains(jelo)
            } || biljka.profilOkusa == referentnaBiljka.profilOkusa
        }
    }

}