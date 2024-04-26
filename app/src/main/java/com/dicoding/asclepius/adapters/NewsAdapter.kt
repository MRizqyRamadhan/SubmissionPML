package com.dicoding.asclepius.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.reponse.ArticlesItem
import com.dicoding.asclepius.databinding.ListNewsBinding

@Suppress("DEPRECATION")
class NewsAdapter: ListAdapter<ArticlesItem, NewsAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener{
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val newsUrl = user.url
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsUrl))
                holder.itemView.context.startActivity(intent)
            }
        }
    }
    class MyViewHolder(val binding: ListNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem) {
            binding.tvNewsTitle.text = article.title
            val profileCircleImageView = binding.tvPhoto
            val profileImageUrl = article.urlToImage

            Glide.with(profileCircleImageView.context)
                .load(profileImageUrl)
                .into(profileCircleImageView)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}