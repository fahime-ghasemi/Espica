package com.espica.ui.home

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.espica.BaseFragment
import com.espica.EspicaApp
import com.espica.R
import com.espica.data.network.ApiClient
import com.espica.tools.AnimationFactory
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.loading.*

class ReviewFragment : BaseFragment(), ExerciseContract.View {

    lateinit var presenter: ExercisePresenter

    override val layoutResId = R.layout.fragment_review

    companion object {
        fun newInstance(): ReviewFragment {
            return ReviewFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ExercisePresenter(ApiClient((activity!!.application as EspicaApp).networkApiService))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onNewFragmentAttached(1)
        presenter.view = this
        val animationIn = AnimationUtils.loadAnimation(context, R.anim.flipper_left_in)
        val animationOut = AnimationUtils.loadAnimation(context,R.anim.flipper_left_out)

//        simpleViewFlipper.inAnimation = animationIn
//        simpleViewFlipper.outAnimation = animationOut
        var flag= false
        btn1.setOnClickListener {
//            if(flag) {
//                simpleViewFlipper.showNext()
//                flag = true
//            }
//            else
//            {
//                flag = false
//                simpleViewFlipper.showPrevious()
//            }

            AnimationFactory.flipTransition(simpleViewFlipper, AnimationFactory.FlipDirection.LEFT_RIGHT,200);

        }



    }

    override fun hideLoading() {
        loading.visibility = View.GONE
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

}