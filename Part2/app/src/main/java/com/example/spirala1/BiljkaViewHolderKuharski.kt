package com.example.spirala1

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BiljkaViewHolderKuharski (itemView: View, private val listener: BiljkaClickListener, private val glavnaListaBiljke: MutableList<Biljka>): RecyclerView.ViewHolder (itemView), View.OnClickListener {

    init {
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
        itemView.findViewById<TextView>(R.id.profilOkusaItem).text = biljka.profilOkusa.opis
        val jelaHolder = biljka.jela
        if(jelaHolder.isNotEmpty()) itemView.findViewById<TextView>(R.id.jelo1Item).text = jelaHolder[0]
        if(jelaHolder.size>1) itemView.findViewById<TextView>(R.id.jelo2Item).text = jelaHolder[1]
        if(jelaHolder.size>2) itemView.findViewById<TextView>(R.id.jelo3Item).text = jelaHolder[2]
    }
}