package com.espica.data.network.response

import com.google.gson.annotations.SerializedName

class LeitnerCard {
    @SerializedName("title")
    var title: String = ""
    @SerializedName("description")
    var description: String = ""
    @SerializedName("card_id")
    var id: Int? = null
}
