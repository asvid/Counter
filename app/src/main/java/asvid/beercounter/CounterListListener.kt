package asvid.beercounter

import asvid.beercounter.data.CounterItem

interface CounterListListener {
    fun onItemDelete(item: CounterItem, position: Int)

    fun onItemClicked(item: CounterItem, position: Int)

    fun onItemIncrement(item: CounterItem, position: Int)

    fun onItemDecrement(item: CounterItem, position: Int)
}
