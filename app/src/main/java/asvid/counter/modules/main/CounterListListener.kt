package asvid.counter.modules.main

import asvid.counter.modules.main.CounterListAdapter.CounterItemViewHolder
import asvid.counter.data.counter.CounterItem

interface CounterListListener {
    fun onItemDelete(item: CounterItem, position: Int)
    fun onItemEdit(item: CounterItem, position: Int)
    fun onItemClicked(item: CounterItem, position: Int)
    fun onItemIncrement(item: CounterItem, position: Int)
    fun onItemDecrement(item: CounterItem, position: Int)
    fun onDetailsClicked(item: CounterItem, position: Int,
        holder: CounterItemViewHolder)
}
