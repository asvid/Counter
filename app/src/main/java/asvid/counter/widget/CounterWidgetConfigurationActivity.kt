package asvid.counter.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import asvid.counter.CounterListAdapter
import asvid.counter.CounterListListener
import asvid.counter.Di
import asvid.counter.R
import asvid.counter.data.CounterItem
import asvid.counter.data.CounterItemManager
import com.thebluealliance.spectrum.SpectrumDialog
import kotlin.properties.Delegates

/**
 * Created by adam on 15.01.17.
 */

class CounterWidgetConfigurationActivity : AppCompatActivity(), CounterListListener {


    private var counterAdapter: CounterListAdapter by Delegates.notNull()
    private var counterList: RecyclerView by Delegates.notNull()
    private var widgetColor: ImageView by Delegates.notNull()

    private var mAppWidgetId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)
        setContentView(R.layout.counter_widget_configuration_activity)

        widgetColor = findViewById(R.id.widgetColor) as ImageView
        widgetColor.setOnClickListener {
            showColors()
        }

        val name = findViewById(R.id.name) as EditText
        val value = findViewById(R.id.value) as EditText
        val addButton = findViewById(R.id.addButton) as Button

        addButton.setOnClickListener { addItem(name.text.toString(), value.text.toString()) }

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        // If they gave us an intent without the asvid.counter.widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }
        setList()
    }

    private fun setList() {
        val itemList = CounterItemManager.getAllCounterItems()
        counterAdapter = CounterListAdapter(itemList, this)

        counterList = findViewById(R.id.counterList) as RecyclerView
        counterList.adapter = counterAdapter
        counterList.layoutManager = LinearLayoutManager(this)
    }

    private fun addItem(name: String, value: String) {
        val counterItem = CounterItem()
        counterItem.name = name
        counterItem.value = Integer.parseInt(value)
        CounterItemManager.saveCounterItem(counterItem)

        createWidget(counterItem)
    }

    private fun createWidget(counterItem: CounterItem) {
        val widget = CounterWidget()
        widget.counterItem = counterItem
        widget.id = mAppWidgetId.toLong()

        val drawable = widgetColor.background as ColorDrawable
        widget.color = drawable.color
        Di.storage.saveWidget(widget)

        // Request asvid.counter.widget update
        CounterWidgetProvider.updateAppWidget(this, mAppWidgetId, widget)

        setResult(RESULT_OK)
        finish()
    }

    private fun showColors() {
        SpectrumDialog.Builder(this).setColors(R.array.demo_colors)
            .setSelectedColorRes(R.color.md_blue_500)
            .setDismissOnColorSelected(true)
            .setOutlineWidth(2)
            .setOnColorSelectedListener { positiveResult, color ->
                if (positiveResult) {
                    widgetColor.setBackgroundColor(
                        Color.parseColor("#" + Integer.toHexString(color).toUpperCase()))
                }
            }
            .build()
            .show(supportFragmentManager, "dialog_demo_1")
    }

    override fun onItemDelete(item: CounterItem, position: Int) {
        CounterItemManager.deleteCounterItem(item)
        counterAdapter.removeItem(position)
    }

    override fun onItemClicked(item: CounterItem, position: Int) {
        createWidget(item)
    }

    override fun onItemIncrement(item: CounterItem, position: Int) {
        CounterItemManager.incrementAndSave(item)
        counterAdapter.notifyItemChanged(position)
    }

    override fun onItemDecrement(item: CounterItem, position: Int) {
        CounterItemManager.decrementAndSave(item)
        counterAdapter.notifyItemChanged(position)
    }
}
