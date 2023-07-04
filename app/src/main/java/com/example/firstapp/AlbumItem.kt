package com.example.firstapp

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


data class Album(
    val albumName: String?,
    val imageList: List<Int>
) : Parcelable {
    // Implement the Parcelable methods here

    // Write object values to parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(albumName)
        parcel.writeIntArray(imageList.toIntArray())
    }

    // Create a Parcelable.Creator for creating instances of Album from a Parcel
    companion object CREATOR : Parcelable.Creator<Album> {
        // Read object values from parcel and create a new instance of Album
        override fun createFromParcel(parcel: Parcel): Album {
            return Album(parcel.readString(), parcel.createIntArray()?.toList() ?: emptyList())
        }

        override fun newArray(size: Int): Array<Album?> {
            return arrayOfNulls(size)
        }
    }

    // Describe the contents of the Parcelable object
    override fun describeContents(): Int {
        return 0
    }

    // Other properties and methods of the Album class
}
