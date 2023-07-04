package com.example.firstapp.ui.dashboard

import android.R.attr.button
import android.R.attr.text
import android.R.id.edit
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.Album
import com.example.firstapp.AlbumAdapter
import com.example.firstapp.BoardAdapter
import com.example.firstapp.BoardItem
import com.example.firstapp.GalleryDetailActivity
import com.example.firstapp.ImageAdapter
import com.example.firstapp.PhoneDetailActivity
import com.example.firstapp.R
import com.example.firstapp.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {

    companion object {
        val albumList : ArrayList<Album> = ArrayList<Album> ()
        var first = true
    }
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dogImages = listOf(R.drawable.image_1, R.drawable.image_1, R.drawable.image_1, R.drawable.image_1)
        val catImages = listOf(R.drawable.image_2, R.drawable.image_2, R.drawable.image_2, R.drawable.image_2, R.drawable.image_2)
        if (first) {
            albumList.add(Album("Dog", dogImages))
            albumList.add(Album("Cat", catImages))
            albumList.add(Album("Cat2", catImages))
            first = false
        }

        val rvBoard : RecyclerView = binding.rvBoard
        val albumAdapter = AlbumAdapter(albumList)
        rvBoard.adapter = albumAdapter
        rvBoard.layoutManager = GridLayoutManager(requireContext(), 2)
        albumAdapter.albumClickListener = object : AlbumAdapter.OnAlbumClickListener {
            override fun onAlbumClick(index : Int) {
                val intent = Intent(context, GalleryDetailActivity::class.java)
                intent.putExtra("index", "${index}")
                startActivity(intent)
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}