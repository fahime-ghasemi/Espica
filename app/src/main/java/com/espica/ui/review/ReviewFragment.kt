package com.espica.ui.home

import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.View
import com.espica.BaseFragment
import com.espica.EspicaApp
import com.espica.R
import com.espica.data.network.ApiClient
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

        prepareCardAnim()

    }

    private fun prepareCardAnim() {
        val flipLeftIn = AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in)
        val flipLeftOut = AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_out)

        val flipRightIn = AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_in)
        val flipRightOut = AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_out)

        cardContainer.setOnClickListener {
            if (!mIsBackVisible) {
                flipLeftOut.setTarget(cartFront)
                flipLeftIn.setTarget(cartBack)
                flipLeftOut.start()
                flipLeftIn.start()
                mIsBackVisible = true
            } else {
                flipRightIn.setTarget(cartFront)
                flipRightOut.setTarget(cartBack)
                flipRightIn.start()
                flipRightOut.start()
                mIsBackVisible = false
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