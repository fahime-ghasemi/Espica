package com.espica.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.espica.R
import android.util.Log
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : Fragment() {

    lateinit var listener: LoginActivityListener
    companion object {
        fun newInstance():LoginFragment
        {
            return LoginFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_login,container,false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as LoginActivityListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginWithGoogle.setOnClickListener{

        }
        loginWithPhone.setOnClickListener {
            listener.onLoginWithPhone()
        }
    }


}
