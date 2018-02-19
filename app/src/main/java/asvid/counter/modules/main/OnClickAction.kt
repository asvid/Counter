package asvid.counter.modules.main

import asvid.counter.data.room.counter.CounterEntity
import asvid.counter.modules.main.CounterListAdapter.CounterItemViewHolder

data class OnClickAction(val action: ACTION, val item: CounterEntity, val position: Int,
    val holder: CounterItemViewHolder)

enum class ACTION {
  ITEM_CLICKED, DELETE, EDIT, DETAILS, INCREMENT, DECREMENT
}