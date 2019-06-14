package com.espica.ui.dialog.leitner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.espica.EspicaApp
import com.espica.R
import com.espica.data.network.ApiClient
import com.espica.ui.dialog.BaseDialogFragment
import kotlinx.android.synthetic.main.fragment_add_to_leitner.*
import kotlinx.android.synthetic.main.fragment_get_phone.*

class AddToLeitnerDialog : BaseDialogFragment(), LeitnerContract.AddToLeitnerView {

    lateinit var presenter: LeitnerPresenter

    override fun getLayoutRes(): Int {
        return R.layout.fragment_add_to_leitner
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = LeitnerPresenter(ApiClient((activity!!.application as EspicaApp).networkApiService))
        presenter.addToLeitnerView = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        confirm.setOnClickListener {
            if (validInput())
                presenter.addToLeitner(phrase.text.toString(),desc.text.toString(),"5")
        }
    }

    private fun validInput(): Boolean {
        //todo vlidation
        return true
    }

    override fun showLoading() {

    }

    override fun hideLoading() {
    }
}