package com.example.firstapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.example.firstapp.ui.dashboard.DashboardFragmentDirections

class AlbumAdapter(private val albumList: List<Album>) :
    RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albumList[position]
        holder.bind(album)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val albumNameTextView: TextView = itemView.findViewById(R.id.albumNameTextView)
        private val imageRecyclerView: RecyclerView = itemView.findViewById(R.id.imageRecyclerView)

        fun bind(album: Album) {
            albumNameTextView.text = album.albumName

            val spanCount = 2 // Number of columns in the grid
            val layoutManager = GridLayoutManager(itemView.context, spanCount)
            imageRecyclerView.layoutManager = layoutManager

            val imageAdapter = ImageAdapter(album.imageList)
            imageRecyclerView.adapter = imageAdapter
        }

//        fun onClick(v: View?) {
//            val position = adapterPosition
//            if (position != RecyclerView.NO_POSITION) {
//                val clickedAlbum = albumList[position]
//                val action = DashboardFragmentDirections.actionDashboardFragmentToAlbumDetailsFragment(clickedAlbum)
//                itemView.findNavController().navigate(action)
//            }
//        }
    }
}