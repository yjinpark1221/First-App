package com.example.firstapp

import android.view.LayoutInflater
import com.example.firstapp.BoardItem
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import android.view.View

class BoardAdapter(private val itemList: ArrayList<BoardItem>) :
    RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.name.text = itemList[position].name
        holder.phone.text = itemList[position].phone
        holder.email.text = itemList[position].email
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }


    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.name)
        val phone = itemView.findViewById<TextView>(R.id.phone)
        val email = itemView.findViewById<TextView>(R.id.email)
    }
}