package com.example.nureca.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CountryItem(
    @SerializedName("Country")
    var country: String,
    @SerializedName("ISO2")
    var iSO2: String,
    @SerializedName("Slug")
    var slug: String
):Serializable