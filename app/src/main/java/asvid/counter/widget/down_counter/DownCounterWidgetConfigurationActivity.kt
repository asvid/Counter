package asvid.counter.widget.down_counter

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.ImageView
import asvid.counter.Di
import asvid.counter.R.color
import asvid.counter.R.id
import asvid.counter.R.layout
import asvid.counter.data.counter.CounterItem
import asvid.counter.data.counter.CounterItemManager
import asvid.counter.data.widget.CounterWidget
import asvid.counter.data.widget.WidgetSize
import asvid.counter.dialogs.ColorDialogCallback
import asvid.counter.dialogs.DateTimeDialogCallback
import asvid.counter.dialogs.DialogManager
import asvid.counter.widget.CounterWidgetProvider
import asvid.counter.widget.views.WidgetPreview
import kotlinx.android.synthetic.main.down_counter_widget_configuration_activity.down_counter_time
import kotlinx.android.synthetic.main.down_counter_widget_configuration_activity.name
import kotlinx.android.synthetic.main.down_counter_widget_configuration_activity.setDate
import org.ocpsoft.prettytime.PrettyTime
import timber.log.Timber
import java.util.Date
import kotlin.properties.Delegates

class DownCounterWidgetConfigurationActivity : AppCompatActivity(), TextWatcher {

    private var widgetColor: ImageView by Delegates.notNull()
    private var widgetColorValue: Int by Delegates.notNull()

    private var mAppWidgetId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)
        setContentView(layout.down_counter_widget_configuration_activity)

        Di.analyticsHelper.sendScreenName(this, "CounterWidgetConfigurationActivity")

        setView()
        handleIntent()
    }

    private fun setView() {
        widgetColor = findViewById(id.widgetColor) as ImageView
        widgetColor.setOnClickListener {
            showColors()
        }
        widgetColorValue = resources.getColor(color.colorAccent)
        name.addTextChangedListener(this)

        setDate.setOnClickListener {
            showDateDialog()
        }
//        addButton.setOnClickListener { addItem(name.text.toString(), getValue()) }
    }

    private fun showDateDialog() {
        DialogManager.showDateTimeDialog(this, object : DateTimeDialogCallback {
            override fun onCancel() {

            }

            override fun onDateSelected(newDate: Date) {
                Timber.d("selected date: $newDate")
                down_counter_time.text = PrettyTime().format(newDate)
            }
        })
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
        val size = WidgetSize()
        size.heightFactor = 1
        size.widthFactor = 1
        widget.size = size
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
        val widgetView = WidgetPreview(this)

        val nameString = name.text.toString()
        var valueString = down_counter_time.text.toString()

        if (TextUtils.isEmpty(valueString)) {
            valueString = "0"
        }
        widgetView.setNameText(nameString)
        widgetView.setValueText(valueString)
        widgetView.setStrokeColor(widgetColorValue)

        widgetColor.setImageBitmap(widgetView.getBitmap())
    }

    override fun afterTextChanged(p0: Editable?) {
        drawWidgetImage()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
}
