package com.example.spirala1

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BiljkaViewHolderBotanicki(itemView: View, private val listener: BiljkaClickListener, private val glavnaListaBiljke: MutableList<Biljka>) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

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
        itemView.findViewById<TextView>(R.id.porodicaItem).text = biljka.porodica
        itemView.findViewById<TextView>(R.id.klimatskiTipItem).text = biljka.klimatskiTipovi[0].opis
        itemView.findViewById<TextView>(R.id.zemljisniTipItem).text = biljka.zemljisniTipovi[0].toString()
    }
}

