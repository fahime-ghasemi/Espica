package com.espica.ui.login

import android.util.Log
import android.widget.Toast
import com.espica.R
import com.espica.data.network.ApiClient
import com.espica.data.network.MyDisposableObserver
import com.espica.data.network.response.DefaultResponse
import com.espica.data.network.response.OTPResponse
import com.espica.data.network.response.RegisterResponse
import com.espica.data.network.response.VerifyCodeResponse
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class LoginPresenterImp(val apiClient: ApiClient) : LoginContract.Presenter {

    val compositeDisposable = CompositeDisposable()

    lateinit var getPhoneView: LoginContract.GetPhoneView
    lateinit var sendCodeView: LoginContract.SendCodeView


    override fun sendPhone(phone: String) {
        getPhoneView.showLoading()
        compositeDisposable.add(apiClient.registerDevice(UUID.randomUUID().toString()).subscribeWith(object :
            MyDisposableObserver<DefaultResponse<RegisterResponse>>() {
            override fun onSuccess(response: DefaultResponse<RegisterResponse>) {
                Log.i("loginPresenter", response.data?.key)
                compositeDisposable.add(apiClient.sendOtp(phone, response.data?.key).subscribeWith(object :
                    MyDisposableObserver<DefaultResponse<OTPResponse>>() {
                    override fun onSuccess(response: DefaultResponse<OTPResponse>) {
                        Log.i("loginPresenter", response.toString())
                        if (response.status.code.equals("200")) {
                            getPhoneView.hideLoading()
                            getPhoneView.showVerifyPage()
                        }
                        else
                            getPhoneView.showError(response.status?.message)
                    }

                }))
            }

            override fun onError(throwable: Throwable) {
                super.onError(throwable)
                getPhoneView.showError(throwable.message)
            }

        }))
    }

    override fun verifyCode(code: String, mobile: String?) {
        sendCodeView.showLoading()
        compositeDisposable.add(apiClient.verifyCode(code, mobile).subscribeWith(object :
            MyDisposableObserver<DefaultResponse<VerifyCodeResponse>>() {
            override fun onSuccess(response: DefaultResponse<VerifyCodeResponse>) {
                sendCodeView.hideLoading()
                if(response.status!!.code!!.equals("500"))
                    sendCodeView.showError(R.string.error_code)
                else {
                    sendCodeView.saveUserInfo(response.data?.userId)
                    sendCodeView.showMainPage()
                }
            }

            override fun onError(throwable: Throwable) {
                super.onError(throwable)
                sendCodeView.showError(throwable.message)
            }

        }))
    }

    override fun destroy() {
        compositeDisposable.clear()
    }
}