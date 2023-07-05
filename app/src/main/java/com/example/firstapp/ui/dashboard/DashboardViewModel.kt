package com.example.firstapp.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firstapp.Album

class SharedViewModel : ViewModel() {
    val albumList: MutableLiveData<ArrayList<Album>> by lazy {
        MutableLiveData<ArrayList<Album>>()
    }
}