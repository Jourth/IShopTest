package ru.juxlab.tt.ishoptest.data


import com.google.gson.annotations.SerializedName

data class FlashSale(
    var category: String,
    var discount: Int,
    @SerializedName("image_url")
    var imageUrl: String,
    var name: String,
    var price: Double
)