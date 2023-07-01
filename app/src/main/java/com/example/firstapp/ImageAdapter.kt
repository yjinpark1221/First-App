package com.example.firstapp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView

class ImageAdapter(private val mContext: Context) : BaseAdapter() {

    private val imageArray = intArrayOf(
        R.drawable.image_1, R.drawable.image_2, R.drawable.image_2, R.drawable.image_2,
        R.drawable.image_2, R.drawable.image_2, R.drawable.image_2, R.drawable.image_2
        , R.drawable.image_2, R.drawable.image_2, R.drawable.image_2, R.drawable.image_2
        , R.drawable.image_2, R.drawable.image_2, R.drawable.image_2, R.drawable.image_2
    )

    override fun getCount(): Int {
        return imageArray.size
    }

    override fun getItem(position: Int): Any {
        return imageArray[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {
            imageView = ImageView(mContext)
            imageView.layoutParams = ViewGroup.LayoutParams(340, 350) // Replace with your desired width and height values
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            imageView = convertView as ImageView
        }

        imageView.setImageResource(imageArray[position])
        return imageView
    }

}
