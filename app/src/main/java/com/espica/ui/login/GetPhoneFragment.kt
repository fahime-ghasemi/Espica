package com.espica.ui.login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.espica.EspicaApp
import com.espica.R
import com.espica.data.network.ApiClient
import kotlinx.android.synthetic.main.fragment_get_phone.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class GetPhoneFragment : Fragment() {

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

    private fun validatePhone(): Boolean {
        //todo validate phone
        return true
    }


}
