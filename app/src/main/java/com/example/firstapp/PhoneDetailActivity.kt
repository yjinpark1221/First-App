package com.example.firstapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        val name = intent.getStringExtra("name")
        val phone = intent.getStringExtra("phone")
        val email = intent.getStringExtra("email")

        for (i in 0 until PhonebookFragment.itemList.size) {
            if (PhonebookFragment.itemList[i].name == name) {
                index = i
                item = PhonebookFragment.itemList[index]
            }
        }
        setTitle("'$name' 연락처 상세")
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
                tryEdit(nameBuffer, phoneBuffer, emailBuffer)
                editToggle = false
                nameInput.isEnabled = false
                phoneInput.isEnabled = false
                emailInput.isEnabled = false
                editBtn.setText("Edit")
            }
            else {
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


    private fun tryEdit(name : String, phone : String, email : String) {
        for (i in 0 until PhonebookFragment.itemList.size) {
            if (i == index) continue
            val tmpItem = PhonebookFragment.itemList[i]
            if (tmpItem.name == name) {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("연락처 이름 중복")
                alertDialogBuilder.setMessage("'$name'(이)라는 이름의 연락처가 이미 있습니다. 덮어 쓰시겠습니까?")

                alertDialogBuilder.setPositiveButton("예") { dialog, which ->
                    PhonebookFragment.itemList.remove(tmpItem)
                    editPhone(name, phone, email)
                }

                alertDialogBuilder.setNegativeButton("아니오") { dialog, which ->
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
                return
            }
        }
        editPhone(name, phone, email)
    }

    private fun editPhone(name : String, phone : String, email : String) {
        val newIndex = PhonebookFragment.itemList.indexOf(item)
        PhonebookFragment.itemList[newIndex] = BoardItem(name, phone, email)
    }

    fun checkEmail(email : String) : Boolean{
        val emailValidation = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"
        return Pattern.matches(emailValidation, email)
    }

    fun isValid(name : String, phone : String, email : String) : Boolean{
        return name.isNotEmpty() and phone.isNotEmpty() and (email.isEmpty() or checkEmail(email))
    }

}