package asvid.counter.modules.counter_details

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.LinearLayout
import asvid.counter.R.color
import asvid.counter.R.id
import asvid.counter.R.layout
import asvid.counter.charts.DayAxisValueFormatter
import asvid.counter.custom_views.WidgetView
import asvid.counter.data.Change
import asvid.counter.data.CounterItem
import asvid.counter.data.CounterItemManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_counter_details.changeDate
import kotlinx.android.synthetic.main.activity_counter_details.decrementButton
import kotlinx.android.synthetic.main.activity_counter_details.image
import kotlinx.android.synthetic.main.activity_counter_details.incrementButton
import kotlinx.android.synthetic.main.activity_counter_details.toolbarTitle
import kotlinx.android.synthetic.main.content_counter_details.changesChart
import kotlinx.android.synthetic.main.content_counter_details.changesList
import org.ocpsoft.prettytime.PrettyTime
import kotlin.properties.Delegates

class CounterDetailsActivity : AppCompatActivity(), OnOffsetChangedListener {

    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true
    private var isButtonsLayoutVisible = true

    private var mTitleContainer: LinearLayout by Delegates.notNull()
    private var mAppBarLayout: AppBarLayout by Delegates.notNull()
    private var mToolbar: Toolbar by Delegates.notNull()
    private var buttonsLayout: View by Delegates.notNull()

    private var counterItem: CounterItem by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_counter_details)

        val counterId = intent.extras.getLong(EXTRA_COUNTER)
        counterItem = CounterItemManager.getCounterItem(counterId)

        bindActivity()

        mAppBarLayout.addOnOffsetChangedListener(this)

        startAlphaAnimation(toolbarTitle, 0, View.INVISIBLE)

        setView()
        setHistoryList()
        setChart()
    }

    private fun setChart() {
        val entries = ArrayList<Entry>()
        val refTime = counterItem.changes[0].date!!.time
        for (change: Change in counterItem.changes) {
            val calculatedTime = (change.date!!.time) - (refTime)
            entries.add(Entry(calculatedTime.toFloat(), change.postValue!!.toFloat()))
        }
        val dataSet = LineDataSet(entries, "data")
        val lineData = LineData(dataSet)
        changesChart.data = lineData
        val xAxisFormatter = DayAxisValueFormatter(changesChart)
        val xAxis = changesChart.xAxis
        xAxis.valueFormatter = xAxisFormatter
        changesChart.invalidate()
    }

    private fun setHistoryList() {
        changesList.adapter = ChangeHistoryAdapter(counterItem.changes)
        changesList.layoutManager = LinearLayoutManager(this)
    }

    private fun setView() {
        toolbarTitle.text = counterItem.name
        if (counterItem.changes.isEmpty()) {
            changeDate.visibility = View.GONE
        } else changeDate.text = PrettyTime().format(
            counterItem.changes[counterItem.changes.lastIndex].date)

        incrementButton.setOnClickListener {
            CounterItemManager.incrementAndSave(counterItem)
            updateData()
        }
        decrementButton.setOnClickListener {
            CounterItemManager.decrementAndSave(counterItem)
            updateData()
        }

        setImage()

    }

    private fun updateData() {
        toolbarTitle.text = counterItem.name
        changeDate.text = PrettyTime().format(
            counterItem.changes[counterItem.changes.lastIndex].date)
        setImage()
    }

    private fun setImage() {
        val widgetView = WidgetView(this)
        widgetView.setNameText(counterItem.name)
        widgetView.setValueText(counterItem.value)
        widgetView.setStrokeColor(color.colorAccent)

        image.setImageBitmap(widgetView.getBitmap())
    }

    private fun bindActivity() {
        mToolbar = findViewById(id.main_toolbar) as Toolbar
        mTitleContainer = findViewById(id.main_linearlayout_title) as LinearLayout
        mAppBarLayout = findViewById(id.main_appbar) as AppBarLayout
        buttonsLayout = findViewById(id.buttonsLayout)
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
                startAlphaAnimation(toolbarTitle, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                mIsTheTitleVisible = true
            }
        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(toolbarTitle, ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.INVISIBLE)
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

        val EXTRA_COUNTER: String = "EXTRA_COUNTER"
    }
}
