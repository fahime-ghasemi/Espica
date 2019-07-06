package com.espica.data.network

import com.espica.data.network.response.*
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface NetworkApiService {

    @Headers("Accept: application/json")
    @POST(Url.GET_LEITNER)
    fun getLeitnerData(@Body requestBody: RequestBody): Observable<DefaultResponse<LeitnerDataResponse>>


    @Headers("Accept: application/json")
    @POST(Url.REGISTER_DEVICE)
    fun registerDevice(@Body requestBody: RequestBody): Observable<DefaultResponse<RegisterResponse>>

    @Headers("Accept: application/json")
    @GET(Url.GET_ALL_VIDEOS)
    fun getAllVideos(@Query(value = "offset", encoded = true) offset: Int): Observable<VideoListResponse>

    @Headers("Accept: application/json")
    @POST(Url.SEND_OTP)
    fun sendOtp(@Body requestBody: RequestBody): Observable<DefaultResponse<OTPResponse>>

    @Headers("Accept: application/json")
    @POST(Url.VERIFY_CODE)
    fun verifyCode(@Body requestBody: RequestBody): Observable<DefaultResponse<VerifyCodeResponse>>

    @Headers("Accept: application/json")
    @POST(Url.ADD_TO_LEITNER)
    fun addToLeitner(@Body requestBody: RequestBody): Observable<DefaultResponse<AddToLeitnerResponse>>

    @Headers("Accept: application/json")
    @POST(Url.LEITNER_REVIEW)
    fun review(@Body requestBody: RequestBody): Observable<DefaultResponse<ReviewResponse>>


    @GET(Url.SRT_FILE)
    fun readSrt(@Query(value = "video_id", encoded = true) videoId: Int): Observable<ResponseBody>
}
