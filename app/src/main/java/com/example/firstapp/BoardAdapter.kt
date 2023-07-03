package com.example.firstapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import com.example.firstapp.BoardItem
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity

class BoardAdapter(private val itemList: ArrayList<BoardItem>) :
    RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
        fun onCallClick(number: String) {}
    }

    var itemClickListener: OnItemClickListener? = null

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

        val button : ImageButton = itemView.findViewById<ImageButton>(R.id.call_btn)

        init {
            itemView.setOnClickListener{
                itemClickListener?.onItemClick(adapterPosition)
            }
            button.setOnClickListener{
                Toast.makeText(itemView.context, "call ${phone.text}}", Toast.LENGTH_SHORT).show()
                itemClickListener?.onCallClick(phone.text as String)
            }
        }
    }
}