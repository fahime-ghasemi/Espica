package com.espica.ui.dialog.leitner

import com.espica.R
import com.espica.data.network.ApiClient
import com.espica.data.network.MyDisposableObserver
import com.espica.data.network.response.AddToLeitnerResponse
import com.espica.data.network.response.DefaultResponse
import com.google.gson.JsonObject
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType
import okhttp3.RequestBody

class LeitnerPresenter(val apiClient: ApiClient) : LeitnerContract.Presenter {
    val compositeDisposable = CompositeDisposable()
    lateinit var addToLeitnerView : LeitnerContract.AddToLeitnerView

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
                if(response.status?.equals("200") == true) {
                    addToLeitnerView.hideLoading()
                    addToLeitnerView.showToast(R.string.message_add_to_leitner)
                }
            }

        }))
    }

    override fun destroy() {
        compositeDisposable.clear()
    }

}