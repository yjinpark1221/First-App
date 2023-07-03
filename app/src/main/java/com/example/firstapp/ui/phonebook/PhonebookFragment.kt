package com.example.firstapp.ui.phonebook

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.databinding.FragmentPhonebookBinding
import com.example.firstapp.BoardItem
import com.example.firstapp.BoardAdapter
import com.example.firstapp.PhoneDetailActivity
import com.example.firstapp.PhoneNewActivity
import com.example.firstapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class PhonebookFragment : Fragment()  {
    companion object {
        var itemList = ArrayList<BoardItem>()
        var first = true
    }
    private var _binding: FragmentPhonebookBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    public lateinit var boardAdapter : BoardAdapter
    private lateinit var rvBoard : RecyclerView
    private lateinit var context : FragmentActivity
    private lateinit var recyclerCountText : TextView

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

        boardAdapter = BoardAdapter(itemList)
        boardAdapter.notifyDataSetChanged()
        recyclerCountText.text = "${itemList.size} ${getString(R.string.recycler_count)}"

        rvBoard.adapter = boardAdapter
        rvBoard.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        boardAdapter.itemClickListener = object : BoardAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, PhoneDetailActivity::class.java)
                intent.putExtra("index", position)
                startActivity(intent)
            }

            override fun onCallClick(number: String) {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$number")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        // 권한이 이미 허용된 경우
                        startActivity(callIntent)
                    } else {
                        // 권한 요청
                        ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.CALL_PHONE), 1)
                    }
                } else {
                    // 안드로이드 6.0 미만인 경우 권한이 허용되어 있다고 가정
                    startActivity(callIntent)
                }
            }
        }

        // fab (연락처 추가 기능)
        val fab: FloatingActionButton = binding.phoneFab
        fab.setOnClickListener {
            val intent = Intent(context, PhoneNewActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        boardAdapter.notifyDataSetChanged()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
