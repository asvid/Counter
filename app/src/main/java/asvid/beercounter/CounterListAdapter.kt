package asvid.beercounter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import asvid.beercounter.data.CounterItem
import com.mikepenz.iconics.view.IconicsButton

/**
 * Created by adam on 15.01.17.
 */
class CounterListAdapter(private val items: MutableList<CounterItem>,
    private val listener: CounterListListener) : RecyclerView.Adapter<CounterListAdapter.CounterItemViewHolder>() {

    init {
        Log.d("CounterListAdapter", "list: " + items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.counter_item_card, parent, false)
        return CounterItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CounterItemViewHolder, position: Int) {
        val item = items[position]
        Log.d("CounterListAdapter", "item: " + item)
        if (item.isValid) {
            holder.item = item
            holder.name.text = item.name
            holder.value.text = item.value.toString()

            holder.deleteButton.setOnClickListener { listener.onItemDelete(item) }
            holder.decrementButton.setOnClickListener { listener.onItemDelete(item) }
            holder.incrementButton.setOnClickListener { listener.onItemIncrement(item) }
        }
    }

    override fun getItemId(i: Int): Long {
        return items[i].id!!
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(counterItem: CounterItem) {
        items.add(counterItem)
        notifyItemInserted(itemCount - 1)
    }

    inner class CounterItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var item: CounterItem? = null
        var name = itemView.findViewById(R.id.name) as TextView
        var value = itemView.findViewById(R.id.value) as TextView
        var deleteButton = itemView.findViewById(R.id.deleteButton) as IconicsButton
        var incrementButton = itemView.findViewById(R.id.incrementButton) as IconicsButton
        var decrementButton = itemView.findViewById(R.id.decrementButton) as IconicsButton
    }
}
