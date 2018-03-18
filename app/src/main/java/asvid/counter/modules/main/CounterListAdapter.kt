package asvid.counter.modules.main

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import asvid.counter.R.id
import asvid.counter.R.layout
import asvid.counter.model.Counter
import asvid.counter.modules.main.ACTION.DECREMENT
import asvid.counter.modules.main.ACTION.DELETE
import asvid.counter.modules.main.ACTION.DETAILS
import asvid.counter.modules.main.ACTION.EDIT
import asvid.counter.modules.main.ACTION.INCREMENT
import asvid.counter.modules.main.ACTION.ITEM_CLICKED
import asvid.counter.modules.main.CounterListAdapter.CounterItemViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.ocpsoft.prettytime.PrettyTime
import kotlin.properties.Delegates

/**
 * Created by adam on 15.01.17.
 */
class CounterListAdapter(
    private var items: List<Counter>) : Adapter<CounterItemViewHolder>() {

  val onClickSubject: PublishSubject<OnClickAction> = PublishSubject.create()

  val prettyTime = PrettyTime()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterItemViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(layout.counter_item_card, parent, false)
    return CounterItemViewHolder(view)
  }

  override fun onBindViewHolder(holder: CounterItemViewHolder, position: Int) {
    val item = items[position]
    holder.item = item
    holder.name.text = item.name
    holder.value.text = item.value.toString()
//    if (item.changes.isNotEmpty()) {
//      holder.changeDate.text = prettyTime.format(
//          item.changes[item.changes.lastIndex].date)
//    } else {
//      holder.changeDate.visibility = View.GONE
//    }

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

  override fun getItemId(i: Int): Long {
    return items[i].id!!
  }

  override fun getItemCount(): Int {
    return items.size
  }

  fun removeItem(position: Int) {
    notifyItemRemoved(position)
  }

  fun getPositionClicks(): Observable<OnClickAction> {
    return onClickSubject
  }

  inner class CounterItemViewHolder(itemView: View) : ViewHolder(itemView) {

    var item: Counter by Delegates.notNull()
    var cardView: CardView = itemView.findViewById(id.card_view)
    var name: TextView = itemView.findViewById(id.counterName)
    var value: TextView = itemView.findViewById(id.counterStartValue)
    var deleteButton: Button = itemView.findViewById(id.deleteButton)
    var editButton: Button = itemView.findViewById(id.editButton)
    var detailsButton: Button = itemView.findViewById(id.detailsButton)
    var incrementButton: ImageView = itemView.findViewById(id.incrementButton)
    var decrementButton: ImageView = itemView.findViewById(id.decrementButton)
    var changeDate: TextView = itemView.findViewById(id.changeDate)
  }

  fun setItems(list: List<Counter>) {
    items = list
  }
}
