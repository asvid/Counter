package asvid.counter.modules.counter_details

import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import asvid.counter.R.id
import asvid.counter.R.layout
import asvid.counter.data.Change
import asvid.counter.modules.counter_details.ChangeHistoryAdapter.ChangeViewHolder
import org.ocpsoft.prettytime.PrettyTime

class ChangeHistoryAdapter(private val items: List<Change>) : Adapter<ChangeViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ChangeViewHolder, position: Int) {
        val item = items[position]
        if (item.isValid) {
            holder.item = item
            holder.indexView.text = "${position + 1}"
            holder.changeNameView.text = getChange(item)
            holder.changeDateView.text = PrettyTime().format(item.date)
        }
    }

    private fun getChange(item: Change): CharSequence? {
        return "some change I duno Im busy"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout.change_list_item, parent, false)
        return ChangeViewHolder(view)
    }

    inner class ChangeViewHolder(itemView: View) : ViewHolder(itemView) {

        var item: Change? = null
        var indexView = itemView.findViewById(id.indexView) as TextView
        var changeNameView = itemView.findViewById(id.changeNameView) as TextView
        var changeDateView = itemView.findViewById(id.changeDateView) as TextView
    }
}