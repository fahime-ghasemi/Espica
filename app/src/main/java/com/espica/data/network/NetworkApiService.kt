package com.espica.data.network

import com.espica.data.network.response.DefaultResponse
import com.espica.data.network.response.RegisterResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface NetworkApiService {

    @Headers("Accept: application/json")
    @POST(Url.REGISTER_WITH_PHONE)
    fun register(
        @Path(value = Param.CLIENT_ID, encoded = true) clientId: String, @Path(
            value = Param.NONCE,
            encoded = true
        ) nonce: String, @Body requestBody: RequestBody
    ): Observable<RegisterResponse>

    @Headers("Accept: application/json")
    @POST(Url.GET_ALL_VIDEOS)
    fun getAllVideos() :Observable<DefaultResponse>

}
