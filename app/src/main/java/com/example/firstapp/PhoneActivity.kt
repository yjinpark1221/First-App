package com.example.firstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.firstapp.databinding.ActivityMainBinding
import com.example.firstapp.ui.phonebook.PhonebookFragment
import java.util.regex.Pattern

class PhoneActivity : AppCompatActivity() {
    private lateinit var fragment : PhonebookFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)

        fragment = PhonebookFragment()

        val saveBtn : Button = findViewById(R.id.phone_save_btn)
        saveBtn.isEnabled = false

        val nameInput : EditText = findViewById(R.id.name_input)
        val phoneInput : EditText = findViewById(R.id.phone_input)
        val emailInput : EditText = findViewById(R.id.email_input)

        var nameBuffer : String = ""
        var phoneBuffer : String = ""
        var emailBuffer : String = ""


        nameInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                nameBuffer = nameInput.text.toString().trim()
                saveBtn.isEnabled = isValid(nameBuffer, phoneBuffer, emailBuffer)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        phoneInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                phoneBuffer = phoneInput.text.toString().trim()
                saveBtn.isEnabled = isValid(nameBuffer, phoneBuffer, emailBuffer)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        emailInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                emailBuffer = emailInput.text.toString().trim()
                saveBtn.isEnabled = isValid(nameBuffer, phoneBuffer, emailBuffer)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        saveBtn.setOnClickListener{
            savePhone(nameBuffer, phoneBuffer, emailBuffer)
        }
    }

    private fun savePhone(name : String, phone : String, email : String) {
        val newItem = BoardItem(name, phone, email)
        PhonebookFragment.itemList.add(newItem)
        finish()
    }

    fun checkEmail(email : String) : Boolean{
        val emailValidation = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"
        return Pattern.matches(emailValidation, email)
    }

    fun isValid(name : String, phone : String, email : String) : Boolean{
        return name.isNotEmpty() and phone.isNotEmpty() and (email.isEmpty() or checkEmail(email))
    }
}