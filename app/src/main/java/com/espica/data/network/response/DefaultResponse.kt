package com.espica.data.network.response

import com.google.gson.annotations.SerializedName

class DefaultResponse {
    @SerializedName("status")
    var status: Status? = null
}
