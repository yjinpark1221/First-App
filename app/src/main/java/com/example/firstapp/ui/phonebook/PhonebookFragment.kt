package com.example.firstapp.ui.phonebook

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.databinding.FragmentPhonebookBinding
import com.example.firstapp.BoardItem
import com.example.firstapp.BoardAdapter
import com.example.firstapp.PhoneActivity
import com.example.firstapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PhonebookFragment : Fragment() {
    companion object {
        var itemList = ArrayList<BoardItem>()
    }
    private var _binding: FragmentPhonebookBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    public lateinit var boardAdapter : BoardAdapter
    private lateinit var rvBoard : RecyclerView
    private lateinit var context : FragmentActivity

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

        rvBoard = binding.rvBoard

        boardAdapter = BoardAdapter(itemList)
        boardAdapter.notifyDataSetChanged()

        context = this.activity as FragmentActivity
        rvBoard.adapter = boardAdapter
        rvBoard.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        boardAdapter.itemClickListener = object : BoardAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val item = itemList[position]
            }
        }

        // fab (연락처 추가 기능)
        val fab : FloatingActionButton = binding.phoneFab
        fab.setOnClickListener {
//            Toast.makeText(context, "+ 클릭함", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, PhoneActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        boardAdapter.notifyDataSetChanged()
        rvBoard.scrollToPosition(itemList.size - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
