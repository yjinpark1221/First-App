package com.example.firstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.ui.dashboard.DashboardFragment

class GalleryDetailActivity : AppCompatActivity() {
    private var index : Int = -1
    private lateinit var album : Album

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_detail)
        index = (intent?.getStringExtra("index"))?.toInt() ?: 0
        Log.d("asdf", intent?.getStringExtra("index") ?: "asdfasdfasdf")
        Log.d("asdf", "index: " + index.toString())

        album = DashboardFragment.albumList[index]
        setTitle("'${album.albumName}' 앨범 상세")
        if (album != null) {
            // Use the album data to display the images or perform any other required operations
            val rvBoard : RecyclerView = findViewById(R.id.album_details)
            val adapter = ImageAdapter(this,album.imageList)
            rvBoard.adapter = adapter
            rvBoard.layoutManager = GridLayoutManager(this, 3)
        }
    }
}