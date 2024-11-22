package com.example.spirala1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BiljkaListAdapter(private var biljkee: List<Biljka>, private var moda: Mod, private val listener: BiljkaClickListener, private val glavnaListaBiljke: MutableList<Biljka> ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateModa(newModa: Mod) {
        moda = newModa
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (moda) {
            Mod.Medicinski -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.medicinski_item, parent, false)
                BiljkaViewHolderMedicinski(view, listener, glavnaListaBiljke)
            }
            Mod.Kuharski -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.kuharski_item, parent, false)
                BiljkaViewHolderKuharski(view, listener, glavnaListaBiljke)
            }
            Mod.Botanicki -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.botanicki_item, parent, false)
                BiljkaViewHolderBotanicki(view, listener, glavnaListaBiljke)
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
        val biljka = biljkee[position]
        when (holder) {
            is BiljkaViewHolderMedicinski -> holder.bind(biljka)
            is BiljkaViewHolderKuharski -> holder.bind(biljka)
            is BiljkaViewHolderBotanicki -> holder.bind(biljka)
        }
    }

    override fun getItemCount(): Int {
        return biljkee.size
    }

    fun updateListPom(newList: List<Biljka>) {
        biljkee = newList
        notifyDataSetChanged()
    }

}
