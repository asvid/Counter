package asvid.counter.modules.widget_list

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import asvid.counter.Di
import asvid.counter.R
import asvid.counter.R.layout
import asvid.counter.custom_views.WidgetView1x2
import asvid.counter.data.CounterItemManager
import asvid.counter.dialogs.ColorDialogCallback
import asvid.counter.dialogs.DialogManager
import asvid.counter.widget.CounterWidget
import com.mikepenz.iconics.view.IconicsButton
import org.ocpsoft.prettytime.PrettyTime
import timber.log.Timber

class WidgetListAdapter(
    private val items: List<CounterWidget>,
    val context: Context,
    val supportFragmentManager: android.support.v4.app.FragmentManager) : Adapter<CounterWidgetViewHolder>() {

    val prettyTime = PrettyTime()

    override fun onBindViewHolder(holder: CounterWidgetViewHolder, position: Int) {
        val item = items[position]
        Timber.d("counter widget: $item")
        if (item.isValid) {
            holder.item = item
            holder.counterName.text = item.counterItem?.name
            holder.widgetAdded.text = prettyTime.format(item.createDate)
            holder.widgetEdit.setOnClickListener {
                openColorDialog(holder)
            }
            holder.widgetImage.setImageBitmap(getWidgetBitmap(item))
        }
    }

    private fun getWidgetBitmap(item: CounterWidget): Bitmap? {
        val widgetView = WidgetView1x2(context)
        widgetView.setNameText(item.counterItem?.name)
        widgetView.setValueText(item.counterItem?.value)
        widgetView.setStrokeColor(item.color)

        return widgetView.getBitmap()
    }

    private fun openColorDialog(holder: CounterWidgetViewHolder) {
        DialogManager.showColors(context, supportFragmentManager, object : ColorDialogCallback {
            override fun onPositiveClicked(color: Int) {
                holder.item?.color = color
                Di.storage.saveWidget(holder.item!!)
                holder.widgetImage.setImageBitmap(getWidgetBitmap(holder.item!!))
                CounterItemManager.updateWidget(holder.item?.id)
            }

            override fun onNegativeClicked() {
            }

        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterWidgetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout.counter_widget_list_item, parent, false)
        return CounterWidgetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class CounterWidgetViewHolder(itemView: View) : ViewHolder(itemView) {

    var item: CounterWidget? = null
    var widgetImage = itemView.findViewById(R.id.widgetImage) as ImageView
    var counterName = itemView.findViewById(R.id.counterName) as TextView
    var widgetAdded = itemView.findViewById(R.id.widgetAdded) as TextView
    var widgetEdit = itemView.findViewById(R.id.widgetEdit) as IconicsButton

}
