package com.example.firstapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.firstapp.databinding.ActivityPhoneDetailBinding
import com.example.firstapp.ui.phonebook.PhonebookFragment
import java.util.regex.Pattern

class PhoneDetailActivity : AppCompatActivity() {
    private var index : Int = -1
    private lateinit var item : BoardItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_detail)

        index = intent.getIntExtra("index", -1)
        Toast.makeText(this, "${index}", Toast.LENGTH_SHORT).show()
        item = PhonebookFragment.itemList[index]


        var editToggle = false
        val editBtn : Button = findViewById(R.id.detail_edit_btn)
        val deleteBtn : Button = findViewById(R.id.detail_delete_btn)
        val shareBtn : Button = findViewById(R.id.detail_share_btn)
        editBtn.isEnabled = true

        val nameInput : EditText = findViewById(R.id.detail_name_input)
        val phoneInput : EditText = findViewById(R.id.detail_phone_input)
        val emailInput : EditText = findViewById(R.id.detail_email_input)

        nameInput.isEnabled = false
        phoneInput.isEnabled = false
        emailInput.isEnabled = false

        nameInput.setText(item.name)
        phoneInput.setText(item.phone)
        emailInput.setText(item.email)

        var nameBuffer : String = item.name
        var phoneBuffer : String = item.phone
        var emailBuffer : String = item.email

        nameInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                nameBuffer = nameInput.text.toString()
                editBtn.isEnabled = isValid(nameBuffer, phoneBuffer, emailBuffer)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        phoneInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                phoneBuffer = phoneInput.text.toString().trim()
                editBtn.isEnabled = isValid(nameBuffer, phoneBuffer, emailBuffer)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        emailInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                emailBuffer = emailInput.text.toString().trim()
                editBtn.isEnabled = isValid(nameBuffer, phoneBuffer, emailBuffer)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        editBtn.setOnClickListener{
            if (editToggle) {
                Toast.makeText(this, "${isValid(nameBuffer, phoneBuffer, emailBuffer)}", Toast.LENGTH_SHORT).show()
                editPhone(nameBuffer, phoneBuffer, emailBuffer)
                editToggle = false
                nameInput.isEnabled = false
                phoneInput.isEnabled = false
                emailInput.isEnabled = false
                editBtn.setText("Edit")
            }
            else {
                Toast.makeText(this, "${isValid(nameBuffer, phoneBuffer, emailBuffer)}", Toast.LENGTH_SHORT).show()
                editToggle = true
                nameInput.isEnabled = true
                phoneInput.isEnabled = true
                emailInput.isEnabled = true
                editBtn.setText("Save")
                editBtn.isEnabled = isValid(nameBuffer, phoneBuffer, emailBuffer)
            }
        }

        deleteBtn.setOnClickListener{
            PhonebookFragment.itemList.removeAt(index)
            finish()
        }
        shareBtn.setOnClickListener{
            Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editPhone(name : String, phone : String, email : String) {
        val newItem = BoardItem(name, phone, email)
        PhonebookFragment.itemList[index] = newItem
    }

    fun checkEmail(email : String) : Boolean{
        val emailValidation = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"
        return Pattern.matches(emailValidation, email)
    }

    fun isValid(name : String, phone : String, email : String) : Boolean{
        return name.isNotEmpty() and phone.isNotEmpty() and (email.isEmpty() or checkEmail(email))
    }

}