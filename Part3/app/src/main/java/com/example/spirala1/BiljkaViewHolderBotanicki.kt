package com.example.spirala1

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BiljkaViewHolderBotanicki(itemView: View, private val listener: BiljkaClickListener, private val glavnaListaBiljke: MutableList<Biljka>, private val klikanje: Boolean) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        // Dodavanje OnClickListener na itemView
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        // Pozivanje metode onBiljkaClicked() kada je kliknuta biljka
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION && !klikanje) {
            val biljka = glavnaListaBiljke[position]
            listener.onBiljkaClicked(biljka)
        }
    }

    fun bind(biljka: Biljka){
        itemView.findViewById<TextView>(R.id.nazivItem).text = biljka.naziv
        itemView.findViewById<TextView>(R.id.porodicaItem).text = biljka.porodica
        val klimaHolder = biljka.klimatskiTipovi
        val zemljisteHolder = biljka.zemljisniTipovi
        if(klimaHolder?.isNotEmpty() == true) itemView.findViewById<TextView>(R.id.klimatskiTipItem).text = klimaHolder[0].opis
        if(zemljisteHolder?.isNotEmpty() == true) itemView.findViewById<TextView>(R.id.zemljisniTipItem).text = zemljisteHolder[0].toString()

        val imageView = itemView.findViewById<ImageView>(R.id.slikaItem)
        val trefleDAO = TrefleDAO()
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = trefleDAO.getImage(biljka)
            withContext(Dispatchers.Main) {
                imageView.setImageBitmap(bitmap)
            }
        }
    }

}

