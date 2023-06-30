package com.example.firstapp.ui.phonebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.databinding.FragmentPhonebookBinding
import com.example.firstapp.BoardItem
import com.example.firstapp.BoardAdapter
import com.example.firstapp.R

class PhonebookFragment : Fragment() {

    private var _binding: FragmentPhonebookBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val phonebookViewModel =
            ViewModelProvider(this).get(PhonebookViewModel::class.java)

        _binding = FragmentPhonebookBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rvBoard : RecyclerView = binding.rvBoard


        val itemList = ArrayList<BoardItem>()

        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))
        itemList.add(BoardItem("박연진","010-6789-0123","ppulppyo@naver.com"))


        val boardAdapter = BoardAdapter(itemList)
        boardAdapter.notifyDataSetChanged()

        val context = this.activity
        rvBoard.adapter = boardAdapter
        rvBoard.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        boardAdapter.itemClickListener = object : BoardAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val item = itemList[position]
                Toast.makeText(context, "${item.name} 클릭함", Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
