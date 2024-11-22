package com.example.spirala1

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BiljkaViewHolderMedicinski (itemView: View, private val listener: BiljkaClickListener,
    private val glavnaListaBiljke: MutableList<Biljka>)  : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        // Dodavanje OnClickListener na itemView
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        // Pozivanje metode onBiljkaClicked() kada je kliknuta biljka
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            val biljka = glavnaListaBiljke[position]
            listener.onBiljkaClicked(biljka)
        }
    }

    fun bind(biljka: Biljka){
        itemView.findViewById<TextView>(R.id.nazivItem).text = biljka.naziv
        itemView.findViewById<TextView>(R.id.upozorenjeItem).text = biljka.medicinskoUpozorenje
        val koristi = biljka.medicinskeKoristi
        if(koristi.isNotEmpty()) itemView.findViewById<TextView>(R.id.korist1Item).text = koristi[0].opis
        if(koristi.size>1) itemView.findViewById<TextView>(R.id.korist2Item).text = koristi[1].opis
        if(koristi.size>2) itemView.findViewById<TextView>(R.id.korist3Item).text = koristi[2].opis
    }
}