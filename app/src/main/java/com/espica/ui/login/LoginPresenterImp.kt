package com.espica.ui.login

import com.espica.EspicaApp
import com.espica.data.network.ApiClient
import io.reactivex.disposables.CompositeDisposable

class LoginPresenterImp(apiClient: ApiClient):LoginContract.Presenter {

    val compositeDisposable = CompositeDisposable()
    lateinit var mView: LoginContract.View

    fun setView(view: LoginContract.View)
    {
        mView = view
    }
    override fun destroy() {
        compositeDisposable.clear()
    }
}