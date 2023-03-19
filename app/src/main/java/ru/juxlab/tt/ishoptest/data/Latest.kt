package ru.juxlab.tt.ishoptest.data


import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class Latest(
    val category: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val name: String,
    val price: Int,
    val image: Bitmap
)