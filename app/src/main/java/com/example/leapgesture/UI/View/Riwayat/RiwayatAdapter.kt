package com.example.leapgesture.UI.View.Riwayat

import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leapgesture.R
import android.graphics.BitmapFactory

class RiwayatAdapter(private val listPrediksi: List<Prediksi>) :
    RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder>() {

    inner class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val hasilPrediksiText: TextView = itemView.findViewById(R.id.hasil_pred)
        val deskripsiText: TextView = itemView.findViewById(R.id.text_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_riwayat, parent, false)
        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val prediksi = listPrediksi[position]

        holder.hasilPrediksiText.text = prediksi.hasil ?: "-"
        holder.deskripsiText.text = prediksi.deskripsi ?: "-"

        // Decode Base64 gambar dan set ke ImageView
        prediksi.gambar?.let {
            try {
                val decodedBytes = Base64.decode(it, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                holder.imageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.imageView.setImageResource(R.drawable.logo) // fallback image
            }
        } ?: holder.imageView.setImageResource(R.drawable.logo)
    }

    override fun getItemCount(): Int = listPrediksi.size
}
