package com.espica.ui.login

import android.util.Log
import com.espica.EspicaApp
import com.espica.data.SettingManager
import com.espica.data.network.ApiClient
import com.espica.data.network.MyDisposableObserver
import com.espica.data.network.response.DefaultResponse
import com.espica.data.network.response.RegisterResponse
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class LoginPresenterImp(val apiClient: ApiClient):LoginContract.Presenter {

    val compositeDisposable = CompositeDisposable()

    lateinit var mView: LoginContract.View

    fun setView(view: LoginContract.View)
    {
        mView = view
    }

    override fun sendPhone(phone: String) {

        compositeDisposable.add(apiClient.registerDevice(UUID.randomUUID().toString()).subscribeWith(object :
            MyDisposableObserver<DefaultResponse<RegisterResponse>>() {
            override fun onSuccess(response: DefaultResponse<RegisterResponse>) {
                Log.i("loginPresenter",response.data?.key)

            }

        }))
    }

    override fun destroy() {
        compositeDisposable.clear()
    }
}