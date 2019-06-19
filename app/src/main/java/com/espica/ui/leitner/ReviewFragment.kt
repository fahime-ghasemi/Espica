package com.espica.ui.home

import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.espica.BaseFragment
import com.espica.EspicaApp
import com.espica.MainActivity
import com.espica.R
import com.espica.data.EspicaManager
import com.espica.data.network.ApiClient
import com.espica.data.network.response.LeitnerCard
import com.espica.ui.dialog.ProgressDialog
import com.espica.ui.leitner.LeitnerContract
import com.espica.ui.leitner.LeitnerPresenter
import kotlinx.android.synthetic.main.fragment_review.*


class ReviewFragment : BaseFragment(), LeitnerContract.LeitnerView {

    lateinit var presenter: LeitnerPresenter
    private var mIsBackVisible = false
    var progress: ProgressDialog? = null
    var items: List<LeitnerCard>? = ArrayList()
    var position: Int = 0
    var currentItem: LeitnerCard? = null


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
        initUi()
        presenter.getLeitnerData(EspicaManager.getInstance(context).user.id.toString())

    }

    private fun initUi() {
        know.setOnClickListener {
            presenter.review(currentItem?.id, 5)
        }
        notKnow.setOnClickListener {
            presenter.review(currentItem?.id, 0)
        }
    }

    override fun showNextItem() {
        if (position + 1 == items!!.size) {
            Toast.makeText(context, R.string.no_leitner, Toast.LENGTH_LONG).show()
            return
        }
        position++
        currentItem = items!!.get(position)
        showCardData(currentItem)
    }

    private fun prepareCardAnim() {
        val flipLeftIn = AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in)
        val flipLeftOut = AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_out)

        val flipRightIn = AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_in)
        val flipRightOut = AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_out)

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

    override fun showLeitnerData(items: List<LeitnerCard>?) {
        this.items = items

        if (items!!.size > 0) {
            position = 0
            currentItem = items.get(position)
            noLeitner.visibility = View.GONE
            leitnerView.visibility = View.VISIBLE
            showCardData(currentItem)

        } else {
            noLeitner.visibility = View.VISIBLE
            leitnerView.visibility = View.GONE
        }
    }

    private fun showCardData(currentItem: LeitnerCard?) {
        cartFront.visibility = View.VISIBLE
        cartBack.visibility = View.GONE
        contentFront.setText(currentItem?.title)
        info.setText(currentItem?.description)
    }

    override fun hideLoading() {
        progress?.dismiss()
    }

    override fun showLoading() {
        progress = ProgressDialog.newInstance()
        progress?.show(childFragmentManager, "")
    }

}