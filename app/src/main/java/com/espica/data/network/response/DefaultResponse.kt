package com.espica.data.network.response

import com.google.gson.annotations.SerializedName

open class DefaultResponse<T> {
    @SerializedName("status")
    var status: Status? = null

    @SerializedName("data")
    var data: T? = null
}
