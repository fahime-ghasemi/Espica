package com.espica.ui.leitner

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.espica.EspicaApp
import com.espica.R
import com.espica.data.network.ApiClient
import com.espica.ui.dialog.BaseDialogFragment
import com.espica.ui.dialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_add_to_leitner.*

class AddToLeitnerDialog : BaseDialogFragment(), LeitnerContract.AddToLeitnerView {

    lateinit var presenter: LeitnerPresenter
    var progress: ProgressDialog? = null

    override fun getLayoutRes(): Int {
        return R.layout.fragment_add_to_leitner
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter =
            LeitnerPresenter(ApiClient((activity!!.application as EspicaApp).networkApiService))
        presenter.addToLeitnerView = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        confirm.setOnClickListener {
            if (validInput())
                presenter.addToLeitner(title.text.toString(), desc.text.toString(), "5")
        }
    }

    private fun validInput(): Boolean {
        //todo vlidation
        return true
    }

    override fun showLoading() {
        progress = ProgressDialog.newInstance()
        progress?.show(childFragmentManager, "")
    }

    override fun hideLoading() {
        progress?.dismiss()
    }

    override fun showToast(message_add_to_leitner: Int) {
        Toast.makeText(context, message_add_to_leitner,Toast.LENGTH_LONG).show()
    }
}