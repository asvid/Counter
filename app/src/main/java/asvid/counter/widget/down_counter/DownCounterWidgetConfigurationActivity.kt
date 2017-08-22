package asvid.counter.widget.down_counter

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.format.DateFormat
import android.widget.ImageView
import asvid.counter.Di
import asvid.counter.R
import asvid.counter.R.color
import asvid.counter.R.id
import asvid.counter.R.layout
import asvid.counter.data.down_counter.DownCounterWidget
import asvid.counter.dialogs.ColorDialogCallback
import asvid.counter.dialogs.DateTimeDialogCallback
import asvid.counter.dialogs.DialogManager
import asvid.counter.widget.views.WidgetPreview
import kotlinx.android.synthetic.main.activity_main.counterNameInputLayer
import kotlinx.android.synthetic.main.down_counter_widget_configuration_activity.addButton
import kotlinx.android.synthetic.main.down_counter_widget_configuration_activity.downCounterName
import kotlinx.android.synthetic.main.down_counter_widget_configuration_activity.downCounterNameLayout
import kotlinx.android.synthetic.main.down_counter_widget_configuration_activity.down_counter_date
import kotlinx.android.synthetic.main.down_counter_widget_configuration_activity.down_counter_time
import kotlinx.android.synthetic.main.down_counter_widget_configuration_activity.setDate
import kotlinx.android.synthetic.main.down_counter_widget_configuration_activity.setTime
import timber.log.Timber
import java.util.Date
import kotlin.properties.Delegates

class DownCounterWidgetConfigurationActivity : AppCompatActivity(), TextWatcher {

  private var widgetColor: ImageView by Delegates.notNull()
  private var widgetColorValue: Int by Delegates.notNull()

  private var mAppWidgetId: Int = -1

  var selectedDate: Date by Delegates.notNull()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setResult(Activity.RESULT_CANCELED)
    setContentView(layout.down_counter_widget_configuration_activity)

    Di.analyticsHelper.sendScreenName(this, "CounterWidgetConfigurationActivity")

    setView()
    handleIntent()
    drawWidgetImage()
  }

  private fun setView() {
    widgetColor = findViewById(id.widgetColor) as ImageView
    widgetColor.setOnClickListener {
      showColors()
    }
    widgetColorValue = resources.getColor(color.colorAccent)
    downCounterName.addTextChangedListener(this)

    setDate.setOnClickListener {
      showDateDialog()
    }
    setTime.setOnClickListener {
      showTimeDialog()
    }
    addButton.setOnClickListener {
      if (!TextUtils.isEmpty(downCounterName.text.toString())) addItem(
          downCounterName.text.toString(), selectedDate)
      else {
        downCounterNameLayout.error = resources.getString(R.string.no_name_error)
      }
    }
  }

  private fun showTimeDialog() {
    DialogManager.showTimeDialog(this, object : DateTimeDialogCallback {
      override fun onCancel() {

      }

      override fun onDateSelected(newDate: Date) {
        Timber.d("selected time: $newDate")
        selectedDate.hours = newDate.hours
        selectedDate.minutes = newDate.minutes
        down_counter_time.text = DateFormat.getTimeFormat(
            this@DownCounterWidgetConfigurationActivity).format(newDate)
      }
    })
  }

  private fun showDateDialog() {
    DialogManager.showDateDialog(this, object : DateTimeDialogCallback {
      override fun onCancel() {

      }

      override fun onDateSelected(newDate: Date) {
        Timber.d("selected date: $newDate")
        selectedDate = newDate
        addButton.isEnabled = true
        down_counter_date.text = DateFormat.getDateFormat(
            this@DownCounterWidgetConfigurationActivity).format(newDate)
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

  private fun addItem(name: String, date: Date) {
    val downCounter = DownCounterWidget()
    downCounter.id = mAppWidgetId.toLong()
    downCounter.name = name
    downCounter.date = date
    downCounter.color = widgetColorValue

    createWidget(downCounter)
  }

  private fun createWidget(counterItem: DownCounterWidget) {
    Di.storage.saveDownCounter(counterItem)

    DownCounterWidgetProvider.updateAppWidget(this, mAppWidgetId.toLong(), counterItem)

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
    val widgetView = WidgetPreview(R.layout.counter_widget_preview_layout_x2, this, null, 0, 0)

    var nameString = downCounterName.text.toString()
    if (TextUtils.isEmpty(nameString)) nameString = "your name here"
    val valueString = "134d 4h"
    widgetView.setNameText(nameString)
    widgetView.setValueText(valueString)
    widgetView.setStrokeColor(widgetColorValue)

    widgetColor.setImageBitmap(widgetView.getBitmap(2))
  }

  override fun afterTextChanged(p0: Editable?) {
    drawWidgetImage()
  }

  override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
  }

  override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

  }
}
