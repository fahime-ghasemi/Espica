package com.espica.ui.home

import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.espica.BaseFragment
import com.espica.EspicaApp
import com.espica.MainActivity
import com.espica.R
import com.espica.data.network.ApiClient
import com.espica.ui.dialog.ProgressDialog
import com.espica.ui.leitner.LeitnerContract
import com.espica.ui.leitner.LeitnerPresenter
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.loading.*


class ReviewFragment : BaseFragment(), LeitnerContract.LeitnerView {

    lateinit var presenter: LeitnerPresenter
    private var mIsBackVisible = false
    var progress: ProgressDialog? = null


    override val layoutResId = com.espica.R.layout.fragment_review

    companion object {
        fun newInstance(): ReviewFragment {
            return ReviewFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = LeitnerPresenter(ApiClient((activity!!.application as EspicaApp).networkApiService))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onNewFragmentAttached(MainActivity.FRAGMENT_REVIEW)
        presenter.leitnerView = this
        changeCameraDistance()
        prepareCardAnim()
        presenter.getLeitnerData("5")

    }

    private fun prepareCardAnim() {
        val flipLeftIn = AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in)
        val flipLeftOut = AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_out)

        val flipRightIn = AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_in)
        val flipRightOut = AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_out)

//        cardContainer.setOnClickListener {
//            if (!mIsBackVisible) {
//                flipLeftOut.setTarget(cartFront)
//                flipLeftIn.setTarget(cartBack)
//                flipLeftOut.start()
//                flipLeftIn.start()
//                mIsBackVisible = true
//            } else {
//                flipRightIn.setTarget(cartFront)
//                flipRightOut.setTarget(cartBack)
//                flipRightIn.start()
//                flipRightOut.start()
//                mIsBackVisible = false
//            }
//
//        }



        goToBack.setOnClickListener {
            flipLeftOut.setTarget(cartFront)
            flipLeftIn.setTarget(cartBack)
            flipLeftOut.start()
            flipLeftIn.start()
        }
        goToFront.setOnClickListener {
            flipRightIn.setTarget(cartFront)
            flipRightOut.setTarget(cartBack)
            flipRightIn.start()
            flipRightOut.start()
        }
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        cartFront.setCameraDistance(scale)
        cartBack.setCameraDistance(scale)
    }

    override fun showLeitnerData() {
        Toast.makeText(context,"show leitner data",Toast.LENGTH_LONG).show()
    }

    override fun hideLoading() {
        progress?.dismiss()
    }

    override fun showLoading() {
        progress = ProgressDialog.newInstance()
        progress?.show(childFragmentManager, "")
    }

}