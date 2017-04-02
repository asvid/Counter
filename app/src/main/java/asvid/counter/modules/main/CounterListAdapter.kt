package asvid.counter.modules.main

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import asvid.counter.R.id
import asvid.counter.R.layout
import asvid.counter.data.counter.CounterItem
import asvid.counter.modules.main.ACTION.DECREMENT
import asvid.counter.modules.main.ACTION.DELETE
import asvid.counter.modules.main.ACTION.DETAILS
import asvid.counter.modules.main.ACTION.EDIT
import asvid.counter.modules.main.ACTION.INCREMENT
import asvid.counter.modules.main.ACTION.ITEM_CLICKED
import asvid.counter.modules.main.CounterListAdapter.CounterItemViewHolder
import com.mikepenz.iconics.view.IconicsButton
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.ocpsoft.prettytime.PrettyTime
import kotlin.properties.Delegates

/**
 * Created by adam on 15.01.17.
 */
class CounterListAdapter(
    private val items: MutableList<CounterItem>) : Adapter<CounterItemViewHolder>() {

    val onClickSubject: PublishSubject<OnClickAction> = PublishSubject.create()

    val prettyTime = PrettyTime()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout.counter_item_card, parent, false)
        return CounterItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CounterItemViewHolder, position: Int) {
        val item = items[position]
        if (item.isValid) {
            holder.item = item
            holder.name.text = item.name
            holder.value.text = item.value.toString()
            if (item.changes.isNotEmpty()) {
                holder.changeDate.text = prettyTime.format(
                    item.changes[item.changes.lastIndex].date)
            } else {
                holder.changeDate.visibility = View.GONE
            }

            holder.deleteButton.setOnClickListener {
                onClickSubject.onNext(OnClickAction(DELETE, holder.item, position, holder))
            }
            holder.editButton.setOnClickListener {
                onClickSubject.onNext(OnClickAction(EDIT, holder.item, position, holder))
            }
            holder.decrementButton.setOnClickListener {
                onClickSubject.onNext(OnClickAction(DECREMENT, holder.item, position, holder))
            }
            holder.incrementButton.setOnClickListener {
                onClickSubject.onNext(OnClickAction(INCREMENT, holder.item, position, holder))
            }
            holder.detailsButton.setOnClickListener {
                onClickSubject.onNext(OnClickAction(DETAILS, holder.item, position, holder))
            }
            holder.cardView.setOnClickListener {
                onClickSubject.onNext(OnClickAction(ITEM_CLICKED, holder.item, position, holder))
            }
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

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getPositionClicks(): Observable<OnClickAction> {
        return onClickSubject
    }

    inner class CounterItemViewHolder(itemView: View) : ViewHolder(itemView) {

        var item: CounterItem by Delegates.notNull()
        var cardView = itemView.findViewById(id.card_view) as CardView
        var name = itemView.findViewById(id.counterName) as TextView
        var value = itemView.findViewById(id.counterStartValue) as TextView
        var deleteButton = itemView.findViewById(id.deleteButton) as Button
        var editButton = itemView.findViewById(id.editButton) as Button
        var detailsButton = itemView.findViewById(id.detailsButton) as Button
        var incrementButton = itemView.findViewById(id.incrementButton) as IconicsButton
        var decrementButton = itemView.findViewById(id.decrementButton) as IconicsButton
        var changeDate = itemView.findViewById(id.changeDate) as TextView
    }
}
