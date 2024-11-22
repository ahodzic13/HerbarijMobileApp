package com.example.spirala1

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    fun bind(biljka: Biljka) {
        itemView.findViewById<TextView>(R.id.nazivItem).text = biljka.naziv
        itemView.findViewById<TextView>(R.id.upozorenjeItem).text = biljka.medicinskoUpozorenje
        val koristi = biljka.medicinskeKoristi
        if (koristi!!.isNotEmpty()) itemView.findViewById<TextView>(R.id.korist1Item).text =
            koristi[0].opis
        if (koristi.size > 1) itemView.findViewById<TextView>(R.id.korist2Item).text =
            koristi[1].opis
        if (koristi.size > 2) itemView.findViewById<TextView>(R.id.korist3Item).text =
            koristi[2].opis

        val imageView = itemView.findViewById<ImageView>(R.id.slikaItem)

        val context = itemView.context

        val database = BiljkaDatabase.getInstance(context)
        val biljkaDAO = database.biljkaDao()

        CoroutineScope(Dispatchers.IO).launch {
            biljka.id?.let { biljkaId ->
                val bitmapFromDb = biljkaDAO.getImageForBiljke(biljkaId)
                if (bitmapFromDb != null) {
                    withContext(Dispatchers.Main) {
                        val resizedBmp = Bitmap.createScaledBitmap(bitmapFromDb.bitmap, 100, 100, true)
                        imageView.setImageBitmap(resizedBmp)
                    }
                } else {
                    val trefleDAO = TrefleDAO()
                    val bitmapFromApi = trefleDAO.getImage(biljka)
                    if (bitmapFromApi != null) {
                        val resizedBmp = Bitmap.createScaledBitmap(bitmapFromApi, 100, 100, true)
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
