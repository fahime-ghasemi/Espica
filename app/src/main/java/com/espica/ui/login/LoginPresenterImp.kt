package com.espica.ui.login

import android.util.Log
import com.espica.data.network.ApiClient
import com.espica.data.network.MyDisposableObserver
import com.espica.data.network.response.DefaultResponse
import com.espica.data.network.response.OTPResponse
import com.espica.data.network.response.RegisterResponse
import com.espica.data.network.response.VerifyCodeResponse
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class LoginPresenterImp(val apiClient: ApiClient):LoginContract.Presenter {

    val compositeDisposable = CompositeDisposable()

    lateinit var getPhoneView: LoginContract.GetPhoneView
    lateinit var sendCodeView: LoginContract.SendCodeView


    override fun sendPhone(phone: String) {
        getPhoneView.showLoading();
        compositeDisposable.add(apiClient.registerDevice(UUID.randomUUID().toString()).subscribeWith(object :
            MyDisposableObserver<DefaultResponse<RegisterResponse>>() {
            override fun onSuccess(response: DefaultResponse<RegisterResponse>) {
                Log.i("loginPresenter",response.data?.key)
                compositeDisposable.add(apiClient.sendOtp(phone,response.data?.key).subscribeWith(object : MyDisposableObserver<DefaultResponse<OTPResponse>>()
                {
                    override fun onSuccess(response: DefaultResponse<OTPResponse>) {
                        Log.i("loginPresenter",response.toString())
                        getPhoneView.showVerifyPage()
                    }

                }))
            }

        }))
    }

    override fun verifyCode(code: String, mobile: String?) {
        sendCodeView.showLoading()
        compositeDisposable.add(apiClient.verifyCode(code,mobile).subscribeWith(object :MyDisposableObserver<DefaultResponse<VerifyCodeResponse>>()
        {
            override fun onSuccess(response: DefaultResponse<VerifyCodeResponse>) {
                Log.i("loginPresenter",response.toString())

            }

        }))
    }

    override fun destroy() {
        compositeDisposable.clear()
    }
}