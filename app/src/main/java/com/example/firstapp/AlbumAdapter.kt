package com.example.firstapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlbumAdapter(var albumList: List<Album>) :
    RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    interface OnAlbumClickListener {
        fun onAlbumClick(int : Int) {}
    }
    var albumClickListener: OnAlbumClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albumList[position]

        holder.albumNameTextView.text= album.albumName
        holder.albumimageView.setImageResource(album.imageList[0])
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumNameTextView: TextView = itemView.findViewById(R.id.albumNameTextView)
        val albumimageView: ImageView = itemView.findViewById(R.id.albumImageView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedAlbum = albumList[position]
                    albumClickListener?.onAlbumClick(position)
                }
            }
        }
    }
}