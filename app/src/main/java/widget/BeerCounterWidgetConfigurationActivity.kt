package widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import asvid.beercounter.App
import asvid.beercounter.CounterListAdapter
import asvid.beercounter.CounterListListener
import asvid.beercounter.R
import asvid.beercounter.data.CounterItem

/**
 * Created by adam on 15.01.17.
 */

class BeerCounterWidgetConfigurationActivity : Activity(), CounterListListener {

    private val mRowIDs: List<Long>? = null
    internal var mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private var mAdapter: CounterListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)
        setContentView(R.layout.beer_counter_widget_configuration_activity)

        val counterList = findViewById(R.id.counterList) as RecyclerView
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

        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }

        val itemList = (application as App).storage!!.allItems()
        mAdapter = CounterListAdapter(itemList, this)
        counterList.adapter = mAdapter
    }

    private fun addItem(name: String, value: String) {
        var value = value
        val counterItem = CounterItem()
        counterItem.name = name
        if (TextUtils.isEmpty(value)) value = "0"
        counterItem.value = Integer.parseInt(value)
        (application as App).storage!!.saveItem(counterItem)

        val widget = CounterWidget()
        widget.counterItem = counterItem
        widget.id = mAppWidgetId.toLong()
        (application as App).storage!!.saveWidget(widget)

        // Request widget update
        val appWidgetManager = AppWidgetManager.getInstance(this)
        BeerCounterWidgetProvider.updateAppWidget(this, appWidgetManager, mAppWidgetId,
            counterItem)

        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onItemDelete(item: CounterItem) {

    }

    override fun onItemClicked(item: CounterItem) {

    }

    override fun onItemIncrement(item: CounterItem) {

    }

    override fun onItemDecrement(item: CounterItem) {

    }
}
