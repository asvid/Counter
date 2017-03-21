package asvid.counter

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.properties.Delegates

class CounterDetailsActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true
    private var isButtonsLayoutVisible = true

    private var mTitleContainer: LinearLayout by Delegates.notNull()
    private var mTitle: TextView by Delegates.notNull()
    private var mAppBarLayout: AppBarLayout by Delegates.notNull()
    private var mToolbar: Toolbar by Delegates.notNull()
    private var buttonsLayout: View by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter_details)

        bindActivity()

        mAppBarLayout!!.addOnOffsetChangedListener(this)

        startAlphaAnimation(mTitle, 0, View.INVISIBLE)
    }

    private fun bindActivity() {
        mToolbar = findViewById(R.id.main_toolbar) as Toolbar
        mTitle = findViewById(R.id.main_textview_title) as TextView
        mTitleContainer = findViewById(R.id.main_linearlayout_title) as LinearLayout
        mAppBarLayout = findViewById(R.id.main_appbar) as AppBarLayout
        buttonsLayout = findViewById(R.id.buttonsLayout)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()

        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
        handleButtonLayoutVisibility(percentage)
    }

    private fun handleButtonLayoutVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (isButtonsLayoutVisible) {
                startAlphaAnimation(buttonsLayout,
                    ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.INVISIBLE)
                isButtonsLayoutVisible = false
            }
        } else {
            if (!isButtonsLayoutVisible) {
                startAlphaAnimation(buttonsLayout,
                    ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.VISIBLE)
                isButtonsLayoutVisible = true
            }
        }
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                mIsTheTitleVisible = true
            }
        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                mIsTheTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.INVISIBLE)
                mIsTheTitleContainerVisible = false
            }
        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.VISIBLE)
                mIsTheTitleContainerVisible = true
            }
        }
    }

    companion object {

        private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
        private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
        private val ALPHA_ANIMATIONS_DURATION = 200

        fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
            val alphaAnimation = if (visibility == View.VISIBLE) AlphaAnimation(0f,
                1f) else AlphaAnimation(1f, 0f)

            alphaAnimation.duration = duration
            alphaAnimation.fillAfter = true
            v.startAnimation(alphaAnimation)
        }
    }
}
