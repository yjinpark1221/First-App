package com.example.firstapp.ui.phonebook

import android.Manifest
import android.animation.ObjectAnimator
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.BoardAdapter
import com.example.firstapp.BoardItem
import com.example.firstapp.PhoneDetailActivity
import com.example.firstapp.PhoneNewActivity
import com.example.firstapp.R
import com.example.firstapp.databinding.FragmentPhonebookBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Collections
import java.util.Locale

class PhonebookFragment : Fragment()  {
    companion object {
        var itemList = ArrayList<BoardItem>()
        var searchList = ArrayList<BoardItem>()
        var first = true
    }

    private var isFabOpen: Boolean = false
    private var _binding: FragmentPhonebookBinding? = null
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    public lateinit var boardAdapter : BoardAdapter
    private lateinit var rvBoard : RecyclerView
    private lateinit var context : FragmentActivity
    private lateinit var recyclerCountText : TextView
    private lateinit var searchInput : EditText
    private lateinit var fab : FloatingActionButton
    private lateinit var addFab : FloatingActionButton
    private lateinit var importFab : FloatingActionButton
    private lateinit var progressBar: ProgressBar

    // 데이터를 전달해야 할 곳에서 다음과 같이 호출합니다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val phonebookViewModel =
            ViewModelProvider(this).get(PhonebookViewModel::class.java)

        _binding = FragmentPhonebookBinding.inflate(inflater, container, false)
        val root: View = binding.root
        context = this.activity as FragmentActivity

        if (first) {
            val json = readDataFromAssets("data.json")
            if (json != null) {
                val tmp = parseJsonToBoardItems(json)
                for (boardItem in tmp) {
                    itemList.add(boardItem)
                }
                first = false
            }
        }
        rvBoard = binding.rvBoard
        recyclerCountText = binding.recyclerCount

        boardAdapter = BoardAdapter(ArrayList(itemList))
        boardAdapter.refresh()
        recyclerCountText.text = "${itemList.size} ${getString(R.string.recycler_count)}"

        rvBoard.adapter = boardAdapter
        rvBoard.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        boardAdapter.itemClickListener = object : BoardAdapter.OnItemClickListener {
            override fun onItemClick(item : BoardItem) {
                val intent = Intent(context, PhoneDetailActivity::class.java)
                intent.putExtra("name", item.name)
                intent.putExtra("phone", item.phone)
                intent.putExtra("email", item.email)
                startActivity(intent)
            }

            override fun onCallClick(number: String) {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$number")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        // 권한 있음
                        startActivity(callIntent)
                    } else {
                        // 권한 요청
                        ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.CALL_PHONE), 1)
                    }
                } else {
                    // 권한이 허용 가정
                    startActivity(callIntent)
                }
            }
        }

        // editText (검색 기능)
        searchInput = binding.searchInput
        searchInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(editable: Editable?) {
                val searchText: String = searchInput.getText().toString()
                searchList.clear()
                if (searchText == "") {
                    searchList = ArrayList(itemList)
                } else {
                    // 검색어 포함 확인
                    for (a in 0 until itemList.size) {
                        if (itemList[a].name.toLowerCase()
                            .contains(searchText.lowercase(Locale.getDefault()))
                            or itemList[a].phone.toLowerCase()
                            .contains(searchText.lowercase(Locale.getDefault()))
                            or itemList[a].email.toLowerCase()
                            .contains(searchText.lowercase(Locale.getDefault()))
                        ) {
                            searchList.add(itemList[a])
                        }
                    }
                }
                boardAdapter.setItems(searchList)
            }
        })

        // fab (연락처 추가 기능)
        fab = binding.phoneFab
        addFab = binding.addFab
        importFab = binding.importFab

        val progressBar: ProgressBar = binding.progressBar

        fab.setOnClickListener {
            toggleFab()
        }
        addFab.setOnClickListener {
            val intent = Intent(context, PhoneNewActivity::class.java)
            startActivity(intent)
        }
        importFab.setOnClickListener {
            importFab.isEnabled = false
            progressBar.visibility = View.VISIBLE

            // 로딩 작업 수행 (예: 네트워크 요청)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                } else {
                    readContacts()
                }
            } else {
                readContacts()
            }
            progressBar.visibility = View.INVISIBLE
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        searchInput.setText("")
        boardAdapter.setItems(itemList)
        recyclerCountText.text = "${itemList.size} ${getString(R.string.recycler_count)}"
    }

    fun parseJsonToBoardItems(json: String): ArrayList<BoardItem> {
        val gson = Gson()
        val itemType = object : TypeToken<ArrayList<BoardItem>>() {}.type
        return gson.fromJson(json, itemType)
    }

    fun readDataFromAssets(fileName: String): String? {
        val assetManager: AssetManager = context.assets
        return try {
            val inputStream = assetManager.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val json = reader.use { it.readText() }
            json
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    private fun toggleFab() {
        if (isFabOpen) {
            ObjectAnimator.ofFloat(addFab, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(importFab, "translationY", 0f).apply { start() }
            fab.setImageResource(R.drawable.ic_baseline_add_24)
        } else {
            ObjectAnimator.ofFloat(addFab, "translationY", -600f).apply { start() }
            ObjectAnimator.ofFloat(importFab, "translationY", -300f).apply { start() }
            fab.setImageResource(R.drawable.ic_baseline_clear_24)
        }

        isFabOpen = !isFabOpen

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun readContacts() {
        val contacts = ArrayList<BoardItem>()

        val contentResolver: ContentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )

                    while (phoneCursor != null && phoneCursor.moveToNext()) {
                        val phoneNumber = phoneCursor.getString(
                            phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )

                        // 이메일 주소 가져오기
                        val emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                        )

                        var email: String = ""
                        if (emailCursor != null && emailCursor.moveToNext()) {
                            email = emailCursor.getString(
                                emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                            )
                        }

                        emailCursor?.close()

                        val contact = BoardItem(name, phoneNumber, email)
                        contacts.add(contact)
                    }
                    phoneCursor?.close()
                }
            }
        }
        cursor?.close()

        // 연락처 정보 사용하기
        for (contact in contacts) {
            trySave(contact.name, contact.phone, contact.email)
        }
        boardAdapter.setItems(itemList)
        recyclerCountText.text = "${itemList.size} ${getString(R.string.recycler_count)}"
        Toast.makeText(context, "Imported total ${contacts.size}", Toast.LENGTH_SHORT).show()
    }
    fun trySave(name : String, phone : String, email : String) {
        for (item in PhonebookFragment.itemList) {
            if (item.name == name) {
                val alertDialogBuilder = AlertDialog.Builder(context)
                alertDialogBuilder.setTitle("연락처 이름 중복")
                alertDialogBuilder.setMessage("'$name'(이)라는 이름의 연락처가 이미 있습니다. 덮어 쓰시겠습니까?")

                alertDialogBuilder.setPositiveButton("예") { dialog, which ->
                    itemList.remove(item)
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
        itemList.add(newItem)
    }
}