package com.espica.data.network.response

import com.google.gson.annotations.SerializedName

class Status {
    @SerializedName("code")
    var code: String? = null
    @SerializedName("message")
    var message: String? = null
}
