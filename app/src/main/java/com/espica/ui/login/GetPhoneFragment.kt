package com.espica.ui.login


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.espica.EspicaApp
import com.espica.R
import com.espica.data.network.ApiClient
import com.espica.ui.dialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_get_phone.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class GetPhoneFragment : Fragment(),LoginContract.GetPhoneView {
    lateinit var listener: LoginActivityListener
    var progress: ProgressDialog? = null

    override fun showVerifyPage() {
        listener.onSmsSent(phone.text.toString())
    }

    override fun showLoading() {
        progress = ProgressDialog.newInstance()
        progress?.show(childFragmentManager, "")
    }

    override fun hideLoading() {
        progress?.dismiss()
    }

    lateinit var presenter:LoginPresenterImp

    companion object {
        fun newInstance(): GetPhoneFragment
        {
            return GetPhoneFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = LoginPresenterImp(ApiClient((activity!!.application as EspicaApp).networkApiService))
        presenter.getPhoneView = this
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_get_phone,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        send.setOnClickListener {
            if(validatePhone())
            presenter.sendPhone(phone.text.toString())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as LoginActivityListener
    }

    private fun validatePhone(): Boolean {
        //todo validate phone
        return true
    }

    override fun showError(message: String?) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
}
