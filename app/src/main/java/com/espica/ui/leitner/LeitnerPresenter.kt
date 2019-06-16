package com.espica.ui.leitner

import com.espica.R
import com.espica.data.network.ApiClient
import com.espica.data.network.MyDisposableObserver
import com.espica.data.network.response.AddToLeitnerResponse
import com.espica.data.network.response.DefaultResponse
import com.espica.data.network.response.LeitnerDataResponse
import com.google.gson.JsonObject
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType
import okhttp3.RequestBody

class LeitnerPresenter(val apiClient: ApiClient) : LeitnerContract.Presenter {
    val compositeDisposable = CompositeDisposable()
    lateinit var addToLeitnerView : LeitnerContract.AddToLeitnerView

    lateinit var leitnerView: LeitnerContract.LeitnerView


    override fun addToLeitner(phrase:String,desc:String,userId:String)
    {
        addToLeitnerView.showLoading()
        val jsonObject = JsonObject()
        jsonObject.addProperty("title", phrase)
        jsonObject.addProperty("description", desc)
        jsonObject.addProperty("type", "word")
        jsonObject.addProperty("user_id", userId)
        val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString())

        compositeDisposable.add(apiClient.addToLeitner(requestBody).subscribeWith(object :
            MyDisposableObserver<DefaultResponse<AddToLeitnerResponse>>()
        {
            override fun onSuccess(response: DefaultResponse<AddToLeitnerResponse>) {
                addToLeitnerView.hideLoading()

                if(response.status?.code?.equals("200") == true) {
                    addToLeitnerView.showToast(R.string.message_add_to_leitner )
                }
                else
                    addToLeitnerView.showToast(R.string.error)
            }

        }))
    }

    override fun getLeitnerData(userId:String) {
        leitnerView.showLoading()
        val jsonObject = JsonObject()
//        jsonObject.addProperty("title", phrase)
//        jsonObject.addProperty("description", desc)
//        jsonObject.addProperty("type", "word")
        jsonObject.addProperty("user_id", userId)
        val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString())

        compositeDisposable.add(apiClient.getLeitnerData(requestBody).subscribeWith(object :
            MyDisposableObserver<DefaultResponse<LeitnerDataResponse>>() {
            override fun onSuccess(response: DefaultResponse<LeitnerDataResponse>) {
                leitnerView.hideLoading()
                leitnerView.showLeitnerData()
            }

        }))
    }

    override fun destroy() {
        compositeDisposable.clear()
    }

}