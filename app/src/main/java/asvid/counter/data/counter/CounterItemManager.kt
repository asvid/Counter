package asvid.counter.data.counter

import android.appwidget.AppWidgetManager
import android.content.Intent
import asvid.counter.di.Di
import asvid.counter.di.Di.context
import asvid.counter.analytics.enums.Action.ADD
import asvid.counter.analytics.enums.Action.ALL_ITEMS
import asvid.counter.analytics.enums.Action.DELETE
import asvid.counter.analytics.enums.Category.COUNTER
import asvid.counter.widget.CounterWidgetProvider
import timber.log.Timber

object CounterItemManager {

    fun incrementAndSave(item: CounterItem) {
        item.incrementValue()
        saveAndUpdateWidget(item)
        Timber.i("incrementAndSave: $item")
    }

    fun updateWidget(id: Long?) {
        val intent = Intent(context, CounterWidgetProvider::class.java)
        intent.action = CounterWidgetProvider.UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id?.toInt())

        context.sendBroadcast(intent)
    }

    fun decrementAndSave(item: CounterItem) {
        item.decrementValue()
        saveAndUpdateWidget(item)
    }

    fun saveAndUpdateWidget(item: CounterItem) {
        Di.storage.saveItem(item)
        Di.storage.getWidgetsOfCounter(item).map { updateWidget(it.id) }
    }

    fun getAllCounterItems(): MutableList<CounterItem> {
        val allItems = Di.storage.allItems()
        Di.analyticsHelper.sendEvent(COUNTER, ALL_ITEMS, "${allItems.size}")
        return allItems
    }

    fun getCounterItem(id: Long): CounterItem {
        return Di.storage.getCounterItem(id)
    }

    fun saveCounterItem(counterItem: CounterItem) {
        Di.storage.saveItem(counterItem)
        Di.analyticsHelper.sendEvent(COUNTER, ADD, "")
    }

    fun deleteCounterItem(item: CounterItem) {
        Di.analyticsHelper.sendEvent(COUNTER, DELETE, "")
        Di.storage.getWidgetsOfCounter(item).map { updateWidget(it.id) }
        Di.storage.deleteCounter(item)
    }

}