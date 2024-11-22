package com.example.spirala1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BiljkaListAdapter(private var biljke: List<Biljka>, private var moda: Mod, private val listener: BiljkaClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateModa(newModa: Mod) {
        moda = newModa
        // Ažuriraj prikaz s filtriranim podacima
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (moda) {
            Mod.Medicinski -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.medicinski_item, parent, false)
                BiljkaViewHolderMedicinski(view, listener)
            }
            Mod.Kuharski -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.kuharski_item, parent, false)
                BiljkaViewHolderKuharski(view, listener)
            }
            Mod.Botanicki -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.botanicki_item, parent, false)
                BiljkaViewHolderBotanicki(view, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (moda) {
            Mod.Medicinski -> 0
            Mod.Botanicki -> 1
            Mod.Kuharski -> 2
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val biljka = biljke[position]
        when (holder) {
            is BiljkaViewHolderMedicinski -> holder.bind(biljka)
            is BiljkaViewHolderKuharski -> holder.bind(biljka)
            is BiljkaViewHolderBotanicki -> holder.bind(biljka)
        }
    }

    override fun getItemCount(): Int {
        return biljke.size
    }

    fun updateListPom(newList: List<Biljka>) {
        biljke = newList
        notifyDataSetChanged()
    }

}
