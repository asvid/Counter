package asvid.counter

import asvid.counter.data.CounterItem

interface CounterListListener {
    fun onItemDelete(item: CounterItem, position: Int)

    fun onItemClicked(item: CounterItem, position: Int)

    fun onItemIncrement(item: CounterItem, position: Int)

    fun onItemDecrement(item: CounterItem, position: Int)
}
