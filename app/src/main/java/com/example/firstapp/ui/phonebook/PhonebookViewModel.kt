package com.example.firstapp.ui.phonebook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhonebookViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is phonebook Fragment"
    }
    val text: LiveData<String> = _text
}