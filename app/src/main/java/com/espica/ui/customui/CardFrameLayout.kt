package com.espica.ui.customui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class CardFrameLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {


        if (ev!!.action == MotionEvent.ACTION_UP || ev!!.action == MotionEvent.ACTION_DOWN
        ) {
            return true
        }
        return false
    }
}