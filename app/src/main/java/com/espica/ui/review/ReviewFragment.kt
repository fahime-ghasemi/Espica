package com.espica.ui.home

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ViewAnimator
import com.espica.BaseFragment
import com.espica.EspicaApp
import com.espica.R
import com.espica.data.network.ApiClient
import com.espica.tools.AnimationFactory
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.loading.*





class ReviewFragment : BaseFragment(), ExerciseContract.View {

    lateinit var presenter: ExercisePresenter
    private var mIsBackVisible = false

    override val layoutResId = com.espica.R.layout.fragment_review

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
        changeCameraDistance()
//        val animationIn = AnimationUtils.loadAnimation(context, R.anim.flipper_right_in)
//        val animationOut = AnimationUtils.loadAnimation(context,R.anim.flipper_left_out)

        val animationIn = AnimatorInflater.loadAnimator(context, com.espica.R.animator.in_animation)
        val animationOut = AnimatorInflater.loadAnimator(context, com.espica.R.animator.out_animation)

        val animationIn2 = AnimatorInflater.loadAnimator(context, com.espica.R.animator.in_animation2)
        val animationOut2 = AnimatorInflater.loadAnimator(context, com.espica.R.animator.out_animation2)

//        simpleViewFlipper.inAnimation = animationIn as ObjectAnimator
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
//
//            AnimationFactory.flipTransition(simpleViewFlipper as ViewAnimator?, AnimationFactory.FlipDirection.LEFT_RIGHT,300);
            //todo hamon cart flip haye male fragment ro estefade mikonam
            if (!mIsBackVisible) {
                animationOut.setTarget(cartFront);
                animationIn.setTarget(cartBack);
                animationOut.start();
                animationIn.start();
                mIsBackVisible = true;
            } else {
                animationOut2.setTarget(cartBack);
                animationIn2.setTarget(cartFront);
                animationOut2.start();
                animationIn2.start();
                mIsBackVisible = false;
            }

        }



    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        cartFront.setCameraDistance(scale)
        cartBack.setCameraDistance(scale)
    }

    override fun hideLoading() {
        loading.visibility = View.GONE
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

}