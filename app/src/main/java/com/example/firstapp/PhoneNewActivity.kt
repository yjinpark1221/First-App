package com.example.firstapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.firstapp.ui.phonebook.PhonebookFragment
import java.util.regex.Pattern

class PhoneNewActivity : AppCompatActivity() {
    private lateinit var fragment : PhonebookFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_new)

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
                nameBuffer = nameInput.text.toString()
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
            trySave(nameBuffer, phoneBuffer, emailBuffer)
        }
    }

    fun trySave(name : String, phone : String, email : String) {
        for (item in PhonebookFragment.itemList) {
            if (item.name == name) {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("연락처 이름 중복")
                alertDialogBuilder.setMessage("'$name'(이)라는 이름의 연락처가 이미 있습니다. 덮어 쓰시겠습니까?")

                alertDialogBuilder.setPositiveButton("예") { dialog, which ->
                    PhonebookFragment.itemList.remove(item)
                    savePhone(name, phone, email)
                }

                alertDialogBuilder.setNegativeButton("아니오") { dialog, which ->
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
                return
            }
        }
        savePhone(name, phone, email)
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