package asvid.counter.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import asvid.counter.CounterListAdapter
import asvid.counter.CounterListListener
import asvid.counter.Di
import asvid.counter.R
import asvid.counter.custom_views.WidgetView
import asvid.counter.data.CounterItem
import asvid.counter.data.CounterItemManager
import com.thebluealliance.spectrum.SpectrumDialog
import kotlin.properties.Delegates

/**
 * Created by adam on 15.01.17.
 */

class CounterWidgetConfigurationActivity : AppCompatActivity(), CounterListListener, TextWatcher {


    private var counterAdapter: CounterListAdapter by Delegates.notNull()
    private var counterList: RecyclerView by Delegates.notNull()
    private var widgetColor: ImageView by Delegates.notNull()
    private var widgetColorValue: Int by Delegates.notNull()

    private var name: EditText by Delegates.notNull()
    private var value: EditText by Delegates.notNull()

    private var mAppWidgetId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)
        setContentView(R.layout.counter_widget_configuration_activity)

        Di.analyticsHelper.sendScreenName(this, "CounterWidgetConfigurationActivity")

        widgetColor = findViewById(R.id.widgetColor) as ImageView
        widgetColor.setOnClickListener {
            showColors()
        }
        widgetColorValue = resources.getColor(R.color.colorAccent)
        name = findViewById(R.id.name) as EditText
        value = findViewById(R.id.value) as EditText
        val addButton = findViewById(R.id.addButton) as Button

        name.addTextChangedListener(this)
        value.addTextChangedListener(this)

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
        widget.color = widgetColorValue
        Di.storage.saveWidget(widget)

        // Request asvid.counter.widget update
        CounterWidgetProvider.updateAppWidget(this, mAppWidgetId.toLong(), widget)

        setResult(RESULT_OK)
        finish()
    }

    private fun showColors() {
        SpectrumDialog.Builder(this).setColors(R.array.demo_colors)
            .setSelectedColorRes(R.color.md_blue_500)
            .setDismissOnColorSelected(true)
            .setOutlineWidth(2)
            .setTitle(resources.getString(R.string.select_widget_color))
            .setNegativeButtonText(R.string.cancel)
            .setOnColorSelectedListener { positiveResult, color ->
                if (positiveResult) {
                    widgetColorValue = color
                    drawWidgetImage()
                }
            }
            .build()
            .show(supportFragmentManager, "dialog_demo_1")
    }

    private fun drawWidgetImage() {
        val myView = WidgetView(this)
        val nameString = name.text.toString()
        var valueString = value.text.toString()

        if (TextUtils.isEmpty(valueString)) {
            valueString = "0"
        }

        myView.setNameText(nameString)
        myView.setValueText(valueString.toInt())
        myView.setStrokeColor(widgetColorValue)
        widgetColor.setImageBitmap(myView.getBitmap())
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

    override fun afterTextChanged(p0: Editable?) {
        drawWidgetImage()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
}
