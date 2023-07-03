package com.example.firstapp.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.Album
import com.example.firstapp.ImageAdapter
import com.example.firstapp.R
import com.example.firstapp.databinding.FragmentAlbumDetailsBinding

class AlbumDetailsFragment : Fragment() {

    private var _binding: FragmentAlbumDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlbumDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val args: AlbumDetailsFragmentArgs by navArgs()
        val album: Album? = args.album

        if (album != null) {
            // Use the album data to display the images or perform any other required operations
            val rvBoard : RecyclerView = binding.albumDetails
            val adapter = ImageAdapter(album.imageList)
            rvBoard.adapter = adapter
            rvBoard.layoutManager = GridLayoutManager(requireContext(), 3)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}