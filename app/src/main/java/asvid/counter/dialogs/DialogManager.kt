package asvid.counter.dialogs

import android.content.Context
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.TextView
import asvid.counter.Di
import asvid.counter.R
import asvid.counter.data.CounterItem
import asvid.counter.data.CounterItemManager

class DialogManager(val context: Context) {

    private fun getBuilder() = AlertDialog.Builder(context)

    fun showCounterEditDialog(counter: CounterItem, callback: DialogCallback) {
        val builder: AlertDialog.Builder = getBuilder()
        builder.setTitle(Di.context.resources.getString(R.string.edit_counter_dialog_title))

        val view = LayoutInflater.from(Di.context).inflate(R.layout.edit_counter_dialog, null)
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

}