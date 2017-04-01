package asvid.counter.widget

import android.app.Activity
import android.app.AlertDialog
import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import asvid.counter.Di
import asvid.counter.R
import asvid.counter.R.color
import asvid.counter.R.id
import asvid.counter.data.counter.CounterItem
import asvid.counter.data.counter.CounterItemManager
import asvid.counter.data.widget.CounterWidget
import asvid.counter.dialogs.ColorDialogCallback
import asvid.counter.dialogs.DialogManager
import asvid.counter.modules.main.CounterListAdapter
import asvid.counter.modules.main.CounterListAdapter.CounterItemViewHolder
import asvid.counter.modules.main.CounterListListener
import asvid.counter.widget.views.CounterWidgetView
import kotlin.properties.Delegates

class CounterWidgetConfigurationActivity : AppCompatActivity(), CounterListListener, TextWatcher {
    override fun onDetailsClicked(item: CounterItem, position: Int,
        holder: CounterItemViewHolder) {
        TODO(
            "not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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

        setView()
        handleIntent()
        setList()
    }

    private fun setView() {
        widgetColor = findViewById(id.widgetColor) as ImageView
        widgetColor.setOnClickListener {
            showColors()
        }
        widgetColorValue = resources.getColor(color.colorAccent)
        name = findViewById(id.name) as EditText
        name.addTextChangedListener(this)

        value = findViewById(id.value) as EditText
        value.addTextChangedListener(this)

        val addButton = findViewById(id.addButton) as Button
        addButton.setOnClickListener { addItem(name.text.toString(), getValue()) }
    }

    private fun getValue(): String {
        if (TextUtils.isEmpty(value.text)) return "0"
        return value.text.toString()
    }

    private fun handleIntent() {
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID)
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }
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
        widget.size = "1x1"
        Di.storage.saveWidget(widget)

        CounterWidgetProvider.updateAppWidget(this, mAppWidgetId.toLong(), widget)

        setResult(RESULT_OK)
        finish()
    }

    private fun showColors() {
        DialogManager.showColors(this, supportFragmentManager, object : ColorDialogCallback {
            override fun onPositiveClicked(color: Int) {
                widgetColorValue = color
                drawWidgetImage()
            }

            override fun onNegativeClicked() {
            }

        })
    }

    private fun drawWidgetImage() {
        val myView = CounterWidgetView(this)
        val nameString = name.text.toString()
        var valueString = value.text.toString()

        if (TextUtils.isEmpty(valueString)) {
            valueString = "0"
        }

        myView.setStrokeColor(widgetColorValue)
        widgetColor.setImageBitmap(myView.getBitmap())
    }

    override fun onItemDelete(item: CounterItem, position: Int) {
        CounterItemManager.deleteCounterItem(item)
        counterAdapter.removeItem(position)
    }

    override fun onItemEdit(counter: CounterItem, position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(Di.context.resources.getString(R.string.edit_counter_dialog_title))

        val view = LayoutInflater.from(this).inflate(R.layout.edit_counter_dialog, null)
        val counterName = view.findViewById(R.id.counterName) as TextView
        val counterValue = view.findViewById(R.id.counterValue) as TextView

        counterName.text = counter.name
        counterValue.text = counter.value.toString()

        builder.setPositiveButton(Di.context.resources.getString(R.string.ok), { _, _ ->
            if (TextUtils.isEmpty(counterValue.text)) {
                counterValue.text = "0"
            }
            if (!TextUtils.isEmpty(counterName.text)) {
                counter.name = counterName.text.toString()
                counter.value = (counterValue.text.toString()).toInt()
                CounterItemManager.saveAndUpdateWidget(counter)
            }
            counterAdapter.notifyItemChanged(position)
        })
        builder.setNegativeButton(Di.context.resources.getString(R.string.cancel), { _, _
            ->
        })
        builder.setView(view)
        builder.show()
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
