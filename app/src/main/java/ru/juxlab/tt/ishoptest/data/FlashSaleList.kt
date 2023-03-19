package ru.juxlab.tt.ishoptest.data


import com.google.gson.annotations.SerializedName

data class FlashSaleList(
    @SerializedName("flash_sale")
    var flashSale: List<FlashSale>
)