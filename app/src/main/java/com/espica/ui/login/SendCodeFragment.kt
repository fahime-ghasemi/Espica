package com.espica.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.espica.EspicaApp
import com.espica.MainActivity
import com.espica.R
import com.espica.data.BundleKeys
import com.espica.data.EspicaManager
import com.espica.data.SettingManager
import com.espica.data.network.ApiClient
import com.espica.ui.dialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_send_code.*

class SendCodeFragment : Fragment(), LoginContract.SendCodeView {
    lateinit var presenter: LoginPresenterImp
    var mobile :String? = null
    var progress: ProgressDialog? = null

    companion object {
        fun newInstance(bundle: Bundle): SendCodeFragment {
            val fragment =SendCodeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = LoginPresenterImp(ApiClient((activity!!.application as EspicaApp).networkApiService))
        presenter.sendCodeView = this
        mobile =  arguments?.getString(BundleKeys.MOBILE,"")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_send_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verify.setOnClickListener {
            if (codeIsValid())
                presenter.verifyCode(getCode(),mobile)
        }
    }

    private fun getCode(): String {
        return digit1.text.toString() + digit2.text.toString() + digit3.text.toString() + digit4.text.toString()
    }

    private fun codeIsValid(): Boolean {
        return true
    }

    override fun showMainPage() {
        startActivity(Intent(context, MainActivity::class.java))
        (context as LoginActivity).finish()
    }

    override fun saveUserInfo(userId: Int?) {
        SettingManager.getInstance(context).saveUserId(userId)
        SettingManager.getInstance(context).setLoginStatus(true)
        EspicaManager.getInstance(context).refreshUser(context)
    }

    override fun showLoading() {
        progress = ProgressDialog.newInstance()
        progress?.show(childFragmentManager, "")
    }

    override fun hideLoading() {
        progress?.dismiss()
    }

    override fun showError(error_code: Int) {
        Toast.makeText(context,error_code,Toast.LENGTH_LONG).show()
    }
}