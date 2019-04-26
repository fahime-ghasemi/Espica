package com.espica

import android.content.Context
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    private val LOG_TAG = this.javaClass.simpleName

    protected abstract val layoutResId: Int
    private var mListener: MainActivityListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as MainActivityListener
    }

}
