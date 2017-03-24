package asvid.counter.modules.counter_details

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import asvid.counter.R.color
import asvid.counter.R.id
import asvid.counter.R.layout
import asvid.counter.custom_views.WidgetView
import asvid.counter.data.Change
import asvid.counter.data.CounterItem
import asvid.counter.data.CounterItemManager
import asvid.counter.utils.startAlphaAnimation
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_counter_details.changeDate
import kotlinx.android.synthetic.main.activity_counter_details.decrementButton
import kotlinx.android.synthetic.main.activity_counter_details.image
import kotlinx.android.synthetic.main.activity_counter_details.imageToolbar
import kotlinx.android.synthetic.main.activity_counter_details.incrementButton
import kotlinx.android.synthetic.main.activity_counter_details.toolbar
import kotlinx.android.synthetic.main.activity_counter_details.toolbarTitle
import kotlinx.android.synthetic.main.content_counter_details.changesChart
import kotlinx.android.synthetic.main.content_counter_details.changesList
import org.ocpsoft.prettytime.PrettyTime
import kotlin.properties.Delegates

class CounterDetailsActivity : AppCompatActivity(), OnOffsetChangedListener {

    private var mIsTheTitleVisible = false
    private var isButtonsLayoutVisible = true

    private var mAppBarLayout: AppBarLayout by Delegates.notNull()
    private var buttonsLayout: View by Delegates.notNull()

    private var counterItem: CounterItem by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_counter_details)

        val counterId = intent.extras.getLong(EXTRA_COUNTER)
        counterItem = CounterItemManager.getCounterItem(counterId)

        bindActivity()

        mAppBarLayout.addOnOffsetChangedListener(this)

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

        val imageBitmap = widgetView.getBitmap()

        image.setImageBitmap(imageBitmap)
        imageToolbar.setImageBitmap(imageBitmap)
    }

    private fun bindActivity() {
        mAppBarLayout = findViewById(id.main_appbar) as AppBarLayout
        buttonsLayout = findViewById(id.buttonsLayout)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()

        handleToolbarTitleVisibility(percentage)
        handleButtonLayoutVisibility(percentage)
    }

    private fun handleButtonLayoutVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (isButtonsLayoutVisible) {
                startAlphaAnimation(buttonsLayout,
                    ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.INVISIBLE)
                startAlphaAnimation(image,
                    ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.INVISIBLE)
                isButtonsLayoutVisible = false
            }
        } else {
            if (!isButtonsLayoutVisible) {
                startAlphaAnimation(buttonsLayout,
                    ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.VISIBLE)
                startAlphaAnimation(image,
                    ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.VISIBLE)
                isButtonsLayoutVisible = true
            }
        }
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(toolbar, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                mIsTheTitleVisible = true
            }
        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(toolbar, ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.INVISIBLE)
                mIsTheTitleVisible = false
            }
        }
    }

    companion object {
        private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
        private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
        private val ALPHA_ANIMATIONS_DURATION = 200
        val EXTRA_COUNTER: String = "EXTRA_COUNTER"
    }
}
