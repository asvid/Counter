package asvid.counter.dialogs

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.widget.TextView
import asvid.counter.Di
import asvid.counter.R
import asvid.counter.data.counter.CounterItem
import asvid.counter.data.counter.CounterItemManager
import com.thebluealliance.spectrum.SpectrumDialog
import timber.log.Timber
import java.util.Calendar
import java.util.Date

object DialogManager {

  private fun getBuilder(context: Context) = AlertDialog.Builder(context)

  fun showCounterEditDialog(context: Context, counter: CounterItem, callback: DialogCallback) {
    val builder = getBuilder(context)
    builder.setTitle(context.resources.getString(R.string.edit_counter_dialog_title))

    val view = LayoutInflater.from(context).inflate(R.layout.edit_counter_dialog, null)
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
        CounterItemManager.saveAndUpdateWidget(counter)
      }
      callback.onPositiveClicked()
    })
    builder.setNegativeButton(Di.context.resources.getString(R.string.cancel), { _, _
      ->
      callback.onNegativeClicked()
    })
    builder.setView(view)
    builder.show()
  }

  fun showCounterDeleteDialog(context: Context,
      counterItem: CounterItem, dialogCallback: DialogCallback) {
    val builder = getBuilder(context)
    builder.setTitle(context.resources.getString(R.string.delete_counter_dialog_title))
    builder.setMessage(context.resources.getString(R.string.delete_counter_dialog_content))

    builder.setPositiveButton(context.resources.getString(R.string.ok), { _, _ ->
      dialogCallback.onPositiveClicked()
    })
    builder.setNegativeButton(Di.context.resources.getString(R.string.cancel), { _, _
      ->
      dialogCallback.onNegativeClicked()
    })
    builder.show()
  }

  fun showColors(context: Context, fragmentManager: android.support.v4.app.FragmentManager,
      callback: ColorDialogCallback) {
    SpectrumDialog.Builder(context).setColors(R.array.demo_colors)
        .setSelectedColorRes(R.color.md_blue_500)
        .setDismissOnColorSelected(true)
        .setOutlineWidth(2)
        .setTitle(context.resources.getString(R.string.select_widget_color))
        .setNegativeButtonText(R.string.cancel)
        .setOnColorSelectedListener { positiveResult, color ->
          if (positiveResult) {
            callback.onPositiveClicked(color)
          }
        }
        .build()
        .show(fragmentManager, "dialog_demo_1")
  }

  fun showDateDialog(
      context: Context,
      callback: DateTimeDialogCallback) {
    val currentTime = Date()
    val calendar = Calendar.getInstance()

    Timber.d("current time: ${currentTime.year} ${currentTime.month} ${currentTime.day}")

    val dialog = DatePickerDialog(context,
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
          val newDate = Calendar.getInstance()
          newDate.set(year, monthOfYear, dayOfMonth)
          val date = newDate.time
          callback.onDateSelected(date)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH))

    dialog.show()
  }

  fun showTimeDialog(context: Context, callback: DateTimeDialogCallback) {
    val date = Date()
    TimePickerDialog(context,
        OnTimeSetListener { _, hour, minute ->
          date.hours = hour
          date.minutes = minute
          callback.onDateSelected(date)
        }, date.hours,
        date.minutes, DateFormat.is24HourFormat(context)).show()
  }

}