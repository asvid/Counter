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

        builder.setPositiveButton(Di.context.resources.getString(R.string.ok), { dialog, which ->
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
        builder.setNegativeButton(Di.context.resources.getString(R.string.cancel), { dialog, which
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

        builder.setPositiveButton(context.resources.getString(R.string.ok), { dialog, which ->
            dialogCallback.onPositiveClicked()
        })
        builder.setNegativeButton(Di.context.resources.getString(R.string.cancel), { dialog, which
            ->
            dialogCallback.onNegativeClicked()
        })
        builder.show()

    }

}