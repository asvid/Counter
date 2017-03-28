package asvid.counter.dialogs

import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.TextView
import asvid.counter.Di
import asvid.counter.R
import asvid.counter.data.CounterItem
import asvid.counter.data.CounterItemManager
import com.thebluealliance.spectrum.SpectrumDialog

object DialogManager {

    private fun getBuilder(context: Context) = AlertDialog.Builder(context)

    fun showCounterEditDialog(context: Context, counter: CounterItem, callback: DialogCallback) {
        val builder = getBuilder(context)
        builder.setTitle(context.resources.getString(R.string.edit_counter_dialog_title))

        val view = LayoutInflater.from(context).inflate(R.layout.edit_counter_dialog, null)
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

}