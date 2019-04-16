package com.espica.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.espica.R
import android.util.Log
import androidx.fragment.app.Fragment


/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : Fragment() {

    companion object {
        fun newInstance():LoginFragment
        {
            return newInstance()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_login,container,false)
    }



}
