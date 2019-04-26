package com.espica

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlin.math.log

abstract class BaseFragment : Fragment() {
    private val LOG_TAG = this.javaClass.simpleName

    protected abstract val layoutResId: Int
    var mListener: MainActivityListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(LOG_TAG,"concreateView")
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as MainActivityListener
    }

}
