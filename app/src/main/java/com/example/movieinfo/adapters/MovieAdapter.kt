package com.example.movieinfo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieinfo.databinding.AdapterBinding
import com.example.movieinfo.model.Search

class MovieAdapter:RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    inner class MovieViewHolder(private val binding: AdapterBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(movie: Search){
           binding.apply {
               Glide.with(itemView.context).load(movie.Poster).into(image)
               title.text=movie.Title
               type.text=movie.Type
               year.text=movie.Year
           }
        }
    }
    private val differCallbacks=object :DiffUtil.ItemCallback<Search>(){
        override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
            return oldItem.imdbID==newItem.imdbID
        }

        override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
            return oldItem==newItem
        }
    }
    val differ=AsyncListDiffer(this,differCallbacks)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            AdapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movielist=differ.currentList[position]
        holder.itemView.setOnClickListener {
            onItemClickListener?.let{
                it(movielist)
            }
            holder.bind(movielist)
        }
    }
    private var onItemClickListener:((Search)->Unit)?=null
    fun setOnItemClickListener(listener:(Search)->Unit){
        onItemClickListener=listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}