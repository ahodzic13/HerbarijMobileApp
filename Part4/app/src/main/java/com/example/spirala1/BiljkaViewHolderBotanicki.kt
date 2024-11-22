package com.example.spirala1

import android.graphics.Bitmap
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

        if(klikanje){
            val trefleDAO = TrefleDAO()
            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = trefleDAO.getImage(biljka)
                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(bitmap)
                }
            }
        }else {

            val context = itemView.context

            val database = BiljkaDatabase.getInstance(context)
            val biljkaDAO = database.biljkaDao()

            CoroutineScope(Dispatchers.IO).launch {
                biljka.id?.let { biljkaId ->
                    val bitmapFromDb = biljkaDAO.getImageForBiljke(biljkaId)
                    if (bitmapFromDb != null) {
                        withContext(Dispatchers.Main) {
                            val resizedBmp =
                                Bitmap.createScaledBitmap(bitmapFromDb.bitmap, 100, 100, true)
                            imageView.setImageBitmap(resizedBmp)
                        }
                    } else {
                        val trefleDAO = TrefleDAO()
                        val bitmapFromApi = trefleDAO.getImage(biljka)
                        if (bitmapFromApi != null) {
                            val resizedBmp =
                                Bitmap.createScaledBitmap(bitmapFromApi, 100, 100, true)
                            biljkaDAO.addImage(biljkaId, resizedBmp)
                            withContext(Dispatchers.Main) {
                                imageView.setImageBitmap(resizedBmp)
                            }
                        }
                    }
                }
            }
        }
    }

}

