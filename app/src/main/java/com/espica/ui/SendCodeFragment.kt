package com.espica.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.espica.R

class SendCodeFragment : Fragment() {

    companion object {
        fun newInstance():SendCodeFragment
        {
            return SendCodeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_send_code,container,false)
    }


}