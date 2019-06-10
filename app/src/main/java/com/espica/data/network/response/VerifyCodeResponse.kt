package com.espica.data.network.response

import com.google.gson.annotations.SerializedName

class VerifyCodeResponse {
    @SerializedName("user_id")
    var userId: String = ""
}