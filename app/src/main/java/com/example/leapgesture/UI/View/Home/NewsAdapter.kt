package com.example.leapgesture.UI.View.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.leapgesture.Data.Model.Article
import com.example.leapgesture.databinding.NewsGestureBinding
import com.squareup.picasso.Picasso

class NewsAdapter(private val articles: List<Article>, private val onClick: (Article) -> Unit) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsGestureBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
        holder.itemView.setOnClickListener {
            onClick(article)  // Memanggil listener klik
        }
    }

    override fun getItemCount() = articles.size

    class NewsViewHolder(private val binding: NewsGestureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.apply {
                title.text = article.title
                Picasso.get().load(article.urlToImage).into(image)
            }
        }
    }
}