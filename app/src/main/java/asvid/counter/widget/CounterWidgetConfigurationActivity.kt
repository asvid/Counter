package asvid.counter.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import asvid.counter.CounterListAdapter
import asvid.counter.CounterListListener
import asvid.counter.Di
import asvid.counter.R
import asvid.counter.data.CounterItem
import asvid.counter.data.CounterItemManager
import kotlin.properties.Delegates

/**
 * Created by adam on 15.01.17.
 */

class CounterWidgetConfigurationActivity : Activity(), CounterListListener {


    private var counterAdapter: CounterListAdapter by Delegates.notNull()
    private var counterList: RecyclerView by Delegates.notNull()

    private var mAppWidgetId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)
        setContentView(R.layout.counter_widget_configuration_activity)

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
        Di.storage.saveWidget(widget)

        // Request asvid.counter.widget update
        CounterWidgetProvider.updateAppWidget(this, mAppWidgetId, counterItem)

        setResult(RESULT_OK)
        finish()
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
