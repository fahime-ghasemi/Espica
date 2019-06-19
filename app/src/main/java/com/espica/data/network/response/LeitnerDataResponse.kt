package com.espica.data.network.response

import com.google.gson.annotations.SerializedName

class LeitnerDataResponse {
    @SerializedName("items")
    var items: List<LeitnerCard> = ArrayList()
}