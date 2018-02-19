package asvid.counter.widget

import android.app.Activity
import android.app.AlertDialog
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import asvid.counter.di.Di
import asvid.counter.R
import asvid.counter.R.color
import asvid.counter.R.id
import asvid.counter.data.counter.CounterItem
import asvid.counter.data.counter.CounterItemManager
import asvid.counter.data.widget.CounterWidget
import asvid.counter.data.widget.WidgetSize
import asvid.counter.dialogs.ColorDialogCallback
import asvid.counter.dialogs.DialogManager
import asvid.counter.modules.counter_details.CounterDetailsActivity
import asvid.counter.modules.main.ACTION
import asvid.counter.modules.main.CounterListAdapter
import asvid.counter.modules.main.CounterListAdapter.CounterItemViewHolder
import asvid.counter.widget.views.WidgetPreview
import kotlinx.android.synthetic.main.counter_widget_configuration_activity.counterList
import kotlinx.android.synthetic.main.counter_widget_configuration_activity.counterName
import kotlinx.android.synthetic.main.counter_widget_configuration_activity.counterNameLayout
import kotlinx.android.synthetic.main.counter_widget_configuration_activity.counterStartValue
import kotlinx.android.synthetic.main.counter_widget_configuration_activity.widgetColor
import kotlin.properties.Delegates

class CounterWidgetConfigurationActivity : AppCompatActivity(), TextWatcher {

  private var counterAdapter: CounterListAdapter by Delegates.notNull()

  private var widgetColorValue: Int by Delegates.notNull()


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
    widgetColor.setOnClickListener {
      showColors()
    }
    widgetColorValue = resources.getColor(color.colorAccent)
    counterName.addTextChangedListener(this)
    counterStartValue.addTextChangedListener(this)

    val addButton:Button = findViewById(id.addButton) as Button
    addButton.setOnClickListener {
      if (!TextUtils.isEmpty(counterName.text))
        addItem(counterName.text.toString(), getValue())
      else {
        counterNameLayout.error = resources.getString(R.string.no_name_error)
      }
    }
  }

  private fun getValue(): String {
    if (TextUtils.isEmpty(counterStartValue.text)) return "0"
    return counterStartValue.text.toString()
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
//    val itemList = CounterItemManager.getAllCounterItems()
//    counterAdapter = CounterListAdapter(itemList)
//
//    counterList.adapter = counterAdapter
//    counterList.layoutManager = LinearLayoutManager(this)
//
//    counterAdapter.getPositionClicks().subscribe { action ->
//      when (action.action) {
//        ACTION.DELETE -> onItemDelete(action.item, action.position)
//        ACTION.ITEM_CLICKED -> onItemClicked(action.item, action.position)
//        ACTION.EDIT -> onItemEdit(action.item, action.position)
//        ACTION.DETAILS -> onDetailsClicked(action.item, action.position, action.holder)
//        ACTION.INCREMENT -> onItemIncrement(action.item, action.position)
//        ACTION.DECREMENT -> onItemDecrement(action.item, action.position)
//      }
//    }
  }

  private fun addItem(name: String, value: String) {
    val counterItem = CounterItem()
    counterItem.name = name
    counterItem.value = Integer.parseInt(value)
//    CounterItemManager.saveCounterItem(counterItem)

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

    val nameString = counterName.text.toString()
    var valueString = counterStartValue.text.toString()

    if (TextUtils.isEmpty(valueString)) {
      valueString = "0"
    }
    widgetView.setNameText(nameString)
    widgetView.setValueText(valueString)
    widgetView.setStrokeColor(widgetColorValue)

    widgetColor.setImageBitmap(widgetView.getBitmap())
  }

  fun onItemDelete(item: CounterItem, position: Int) {
//    CounterItemManager.deleteCounterItem(item)
    counterAdapter.removeItem(position)
  }

  fun onItemEdit(counter: CounterItem, position: Int) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle(Di.context.resources.getString(R.string.edit_counter_dialog_title))

    val view = LayoutInflater.from(this).inflate(R.layout.edit_counter_dialog, null)
    val counterName: TextView = view.findViewById(R.id.counterName)
    val counterValue: TextView = view.findViewById(R.id.counterValue)

    counterName.text = counter.name
    counterValue.text = counter.value.toString()

    builder.setPositiveButton(Di.context.resources.getString(R.string.ok), { _, _ ->
      if (TextUtils.isEmpty(counterValue.text)) {
        counterValue.text = "0"
      }
      if (!TextUtils.isEmpty(counterName.text)) {
        counter.name = counterName.text.toString()
        counter.value = (counterValue.text.toString()).toInt()
//        CounterItemManager.saveAndUpdateWidget(counter)
      }
      counterAdapter.notifyItemChanged(position)
    })
    builder.setNegativeButton(Di.context.resources.getString(R.string.cancel), { _, _
      ->
    })
    builder.setView(view)
    builder.show()
  }

//  fun onItemClicked(item: CounterItem, position: Int) {
//    createWidget(item)
//  }
//
//  fun onItemIncrement(item: CounterItem, position: Int) {
//    CounterItemManager.incrementAndSave(item)
//    counterAdapter.notifyItemChanged(position)
//  }
//
//  fun onItemDecrement(item: CounterItem, position: Int) {
//    CounterItemManager.decrementAndSave(item)
//    counterAdapter.notifyItemChanged(position)
//  }

  fun onDetailsClicked(item: CounterItem, position: Int,
      holder: CounterItemViewHolder) {
    val intent = Intent(this, CounterDetailsActivity::class.java)
    intent.putExtra(CounterDetailsActivity.EXTRA_COUNTER, item.id)
    val p1: Pair<View, String> = Pair.create(
        holder.name, "counterNameTransition")
    val p2: Pair<View, String> = Pair.create(
        holder.changeDate, "counterChangeDateTransition")
    val options = ActivityOptionsCompat.
        makeSceneTransitionAnimation(this, p1, p2)
    startActivity(intent, options.toBundle())
  }

  override fun afterTextChanged(p0: Editable?) {
    drawWidgetImage()
  }

  override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
  }

  override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

  }
}
