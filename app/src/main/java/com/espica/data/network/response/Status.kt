package com.espica.data.network.response

import com.google.gson.annotations.SerializedName

class Status {
    @SerializedName("code")
    var code: String = String()
    @SerializedName("message")
    var message: String = String()
}
