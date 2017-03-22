package asvid.counter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import asvid.counter.charts.DayAxisValueFormatter
import asvid.counter.data.Change
import asvid.counter.data.CounterItem
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.mikepenz.iconics.view.IconicsButton
import io.realm.RealmList
import org.ocpsoft.prettytime.PrettyTime

/**
 * Created by adam on 15.01.17.
 */
class CounterListAdapter(private val items: MutableList<CounterItem>,
    private val listener: CounterListListener) : RecyclerView.Adapter<CounterListAdapter.CounterItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.counter_item_card, parent, false)
        return CounterItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CounterItemViewHolder, position: Int) {
        val item = items[position]
        if (item.isValid) {
            holder.item = item
            holder.name.text = item.name
            holder.value.text = item.value.toString()
            if (item.changes.isNotEmpty()) {
                holder.changeDate.text = PrettyTime().format(
                    item.changes[item.changes.lastIndex].date)
            } else {
                holder.changeDate.visibility = View.GONE
            }

            holder.deleteButton.setOnClickListener { listener.onItemDelete(item, position) }
            holder.editButton.setOnClickListener { listener.onItemEdit(item, position) }
            holder.decrementButton.setOnClickListener { listener.onItemDecrement(item, position) }
            holder.incrementButton.setOnClickListener { listener.onItemIncrement(item, position) }
            holder.cardView.setOnClickListener { listener.onItemClicked(item, position) }
            holder.detailsButton.setOnClickListener {
                listener.onDetailsClicked(item, position, holder)
            }
        }
    }

    private fun setChart(chart: LineChart, changes: RealmList<Change>) {
        val entries = ArrayList<Entry>()
        val refTime = changes[0].date!!.time
        for (change: Change in changes) {
            val calculatedTime = (change.date!!.time) - (refTime)
            entries.add(Entry(calculatedTime.toFloat(), change.postValue!!.toFloat()))
        }
        val dataSet = LineDataSet(entries, "data")
        val lineData = LineData(dataSet)
        chart.data = lineData
        val xAxisFormatter = DayAxisValueFormatter(chart)
        val xAxis = chart.xAxis
        xAxis.valueFormatter = xAxisFormatter
        chart.invalidate()
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

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class CounterItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var item: CounterItem? = null
        var cardView = itemView.findViewById(R.id.card_view) as CardView
        var name = itemView.findViewById(R.id.name) as TextView
        var value = itemView.findViewById(R.id.value) as TextView
        var deleteButton = itemView.findViewById(R.id.deleteButton) as Button
        var editButton = itemView.findViewById(R.id.editButton) as Button
        var detailsButton = itemView.findViewById(R.id.detailsButton) as Button
        var incrementButton = itemView.findViewById(R.id.incrementButton) as IconicsButton
        var decrementButton = itemView.findViewById(R.id.decrementButton) as IconicsButton
        var changeDate = itemView.findViewById(R.id.changeDate) as TextView
    }
}
